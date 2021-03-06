# The librarian/scraper Architecture

The configurable scraper requires a scrape configuration file as input.
Additionally it uses an [*ecosystem model*](../../model) which gives it knowledge about specific programming languages and also a general understanding of paradigms like object orientation or functional programming.
Using a scrape configuration and the ecosystem model the scraper then goes through three phases:
1. **Scraping:** First it crawls through the documentation pages of the target library and extracts relevant information from the DOM trees of those pages.
	This phase yields an initial concept graph where concepts like `class` or `method` are nodes and attributes like `class/method` or `method/name` are edges.
	The set of available concept and attribute types is provided by the ecosystem model.
1. **Inference:** The initial concept graph is then refined via basic inference.
	Using the ecosystem model the scraper will infer basic facts like <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathit{method}(C,&space;M)&space;\land&space;\mathit{name}(M,&space;\texttt{\_\_init\_\_})&space;\rightarrow&space;\mathit{constructor}(C,&space;M)"> if a Python library is scraped.
1. **Model Checking:** After refining the concept graph in the inference phase, the scraper then validates all scraped concepts using the ecosystem model.
	Invalid concepts, e.g. a method without a name, will be removed automatically to guarantee consistency and correctness of the scrape to a certain extent.
	This final phase is important to compensate for errors and inconsistencies in documentations.

<img src="lib-scraper-overview.svg">

Now follows a short overview of the technologies and tools used.
The page crawling itself is done using the [Crawler4j](https://github.com/yasserg/crawler4j) library.
The crawled pages are parsed using [Hickory](https://github.com/davidsantiago/hickory) and converted to Clojure [zippers](https://en.wikipedia.org/wiki/Zipper_(data_structure)), an efficient immutable datastructure to describe tree traversal states.
The scraped facts are stored in a [Datascript](https://github.com/tonsky/datascript) in-memory database;
it essentially is triple store consisting of B-tree-based covering indices.
The scraper stores the scraped facts as a gzipped [EDN](https://github.com/edn-format/edn) serialization of the Datascript database.

## Configuration of the Library Scraper

The core requirements for the scraper were that it should be flexible enough to scrape any typical library documentation while also being fairly simple to configure.
A "hook"-based configuration format was chosen to trade-off those competing requirements.
In the scraping phase those hooks are used to guide the scraper:
1. The scraper starts by creating a set of DOM trees, i.e. a DOM "forest" <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{F}&space;=&space;(V,&space;E)">, by recursively crawling through all the pages that can be reached from a starting page and whose URL fits a given pattern.
	The starting page and URL pattern are given in the configuration.
	To reduce the hardware requirements, <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{F}"> will not actually be held in memory in its entirety;
	since this optimization is however not visible to the user it can be ignored for now.
	In the following <img src="https://latex.codecogs.com/svg.latex?\inline&space;V&space;=&space;N_V&space;\cup&space;A_V"> will denote the set of DOM nodes <img src="https://latex.codecogs.com/svg.latex?\inline&space;N_V"> and attribute values <img src="https://latex.codecogs.com/svg.latex?\inline&space;A_V"> in all scraped pages,
	<img src="https://latex.codecogs.com/svg.latex?\inline&space;E&space;=&space;N_E&space;\cup&space;A_E"> will denote the set of directed child edges <img src="https://latex.codecogs.com/svg.latex?\inline&space;N_E&space;\subseteq&space;N_V&space;\times&space;N_V"> and attribute edges <img src="https://latex.codecogs.com/svg.latex?\inline&space;A_E&space;\subseteq&space;N_V&space;\times&space;A_V">.
	Also the existence of functions that assign metadata to the nodes and edges (tag names, attribute names, child node orderings etc.) is assumed.
2. The basic working principle of the scraper is to overlay <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{F}"> with a concept graph <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{G}&space;=&space;(C,&space;R)"> where <img src="https://latex.codecogs.com/svg.latex?\inline&space;C"> is a set of concepts and <img src="https://latex.codecogs.com/svg.latex?\inline&space;R&space;\subseteq&space;C&space;\times&space;C"> is a set of concept relations.
	More formally it finds nodes in <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{F}"> that represent concepts and builds a mapping <img src="https://latex.codecogs.com/svg.latex?\inline&space;f:&space;C&space;\rightarrow&space;V"> from concepts to DOM nodes.
	Each found concept <img src="https://latex.codecogs.com/svg.latex?\inline&space;c"> and each relation <img src="https://latex.codecogs.com/svg.latex?\inline&space;r"> has a type <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathit{type}(c)&space;\in&space;T_C"> and <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathit{type}(r)&space;\in&space;T_R"> respectively with <img src="https://latex.codecogs.com/svg.latex?\inline&space;T&space;:=&space;T_C&space;\cup&space;T_R"> being the set of all node types (e.g. `class` or `method`) and relation types (e.g. `class/method` or `method/name`).
3. To find the concepts and relations in <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{F}"> the scraper recursively applies a set of "hooks" <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{H}"> that is provided by the configuration.
	A hook <img src="https://latex.codecogs.com/svg.latex?\inline&space;h&space;=&space;(t,&space;e,&space;s)&space;\in&space;\mathcal&space;{H}"> consists of a trigger type <img src="https://latex.codecogs.com/svg.latex?\inline&space;t&space;\in&space;T"> an effect type <img src="https://latex.codecogs.com/svg.latex?\inline&space;e&space;\in&space;T"> and a selector function <img src="https://latex.codecogs.com/svg.latex?\inline&space;s:&space;(\mathcal{F},&space;v)&space;\mapsto&space;V'"> with <img src="https://latex.codecogs.com/svg.latex?\inline&space;v&space;\in&space;V"> and <img src="https://latex.codecogs.com/svg.latex?\inline&space;V'&space;\subseteq&space;V">.
	The selector function essentially finds a set of DOM nodes <img src="https://latex.codecogs.com/svg.latex?\inline&space;V'"> relative to a given node <img src="https://latex.codecogs.com/svg.latex?\inline&space;v">;
	in the configuration file <img src="https://latex.codecogs.com/svg.latex?\inline&space;s"> is described using a DSL with an expressive power comparable to XPath.
	The application of the hooks <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{H}"> works like this:
	1. Initially an effect queue <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{Q}"> is initialized with a set of effects <img src="https://latex.codecogs.com/svg.latex?\inline&space;\{(\texttt{document},&space;r)\,|\,&space;r&space;\textrm{&space;root&space;node&space;in&space;}&space;\mathcal{F}\}">.
		An effect essentially describes the arrival of new information which hooks can hook into to recursively produce new information.
		At the beginning the only available information is the existence of the crawled documents.
	2. If <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{Q}"> is not empty, the first effect <img src="https://latex.codecogs.com/svg.latex?\inline&space;e&space;=&space;(t,&space;v)"> is taken from the queue. The scraping phase is completed if <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{Q}"> is empty.
	3. All hooks <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{H}'&space;=&space;\{h&space;\in&space;\mathcal&space;{H}\,|\,&space;h&space;=&space;(t_h,&space;e_h,&space;s_h)&space;\land&space;t_h&space;=&space;t\}"> with trigger type <img src="https://latex.codecogs.com/svg.latex?\inline&space;t"> are then triggered.
		For each triggered hook <img src="https://latex.codecogs.com/svg.latex?\inline&space;h&space;=&space;(t,&space;e_h,&space;s_h)&space;\in&space;\mathcal{H}'"> the scraper applies <img src="https://latex.codecogs.com/svg.latex?\inline&space;s_h(\mathcal{F},&space;v)"> to find a set of selected nodes <img src="https://latex.codecogs.com/svg.latex?\inline&space;V_h">.
	4. For each selected node <img src="https://latex.codecogs.com/svg.latex?\inline&space;v_h&space;\in&space;V_h"> the concept graph <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{G}"> is then extended with a new concept of type <img src="https://latex.codecogs.com/svg.latex?\inline&space;e_h"> iff. <img src="https://latex.codecogs.com/svg.latex?\inline&space;e_h&space;\in&space;T_C"> or a new relation of type <img src="https://latex.codecogs.com/svg.latex?\inline&space;e_h"> iff. <img src="https://latex.codecogs.com/svg.latex?\inline&space;e_h&space;\in&space;T_R">.
		Additionally other concept nodes or relations can be added as part of this extension of <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{G}">.
		How this works is explained in the [description of the configuration DSL](../README.md) but the basic idea is that the scraper will extract data from <img src="https://latex.codecogs.com/svg.latex?\inline&space;v_h">, transform it (e.g. select text using a regular expression), connect the transformed data to other nodes or use it to unify nodes via uniqueness constraints that are provided by its ecosystem model.
	5. Since the extension of the concept graph <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{G}"> produced new information, new effects have to be created.
		Each selected node <img src="https://latex.codecogs.com/svg.latex?\inline&space;v_h&space;\in&space;V_h"> of each triggered hook <img src="https://latex.codecogs.com/svg.latex?\inline&space;h&space;\in&space;\mathcal{H}'"> is used to add an effect <img src="https://latex.codecogs.com/svg.latex?\inline&space;(e_h,&space;v_h)"> to <img src="https://latex.codecogs.com/svg.latex?\inline&space;\mathcal{Q}">.
	6. Goto the second step.

Using this approach, writing a scrape configuration only requires the user to specify how to locate a certain concept or attribute relative to another concept or attribute, i.e. specifying a set of annotated XPath-like tree selectors.
