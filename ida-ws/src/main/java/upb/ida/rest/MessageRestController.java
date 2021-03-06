package upb.ida.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import upb.ida.bean.ResponseBean;
import upb.ida.intent.Orchestrator;
import upb.ida.intent.model.ChatbotContext;
import upb.ida.service.DataService;
import upb.ida.service.RiveScriptService;

import java.util.HashMap;
import java.util.Map;

/**
 * Exposes RESTful RPCs for the IDA Chatbot
 *
 * @author Nikit
 *
 */
@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
@RequestMapping("/message")
public class MessageRestController {
	@Autowired
	private ResponseBean response;
	@Autowired
	private RiveScriptService rsService;
	@Autowired
	private DataService dataService;
	@Autowired
	private Orchestrator orchestrator;
	@Value("${intent.classifier.active}")
	private boolean useNLE;


	@RequestMapping("/sayhello")
	public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "Hello " + name + "!";
	}

	/**
	 * Method to accept queries for the chatbot
	 * @param msg - query in natural language
	 * @param actvScrId - id of the active screen
	 * @param actvTbl - name of the active data table on screen
	 * @param actvDs - name of the active dataset on screen
	 * @return - instance of {@link ResponseBean}
	 * @throws Exception
	 */
	@RequestMapping("/sendmessage")
	public ResponseBean sendmessage(@RequestParam(value = "msg") String msg,
			@RequestParam(value = "actvScrId") String actvScrId, @RequestParam(value = "actvTbl") String actvTbl,
			@RequestParam(value = "actvDs") String actvDs) throws Exception {

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("actvScrId", actvScrId);
		dataMap.put("actvTbl", actvTbl);
		dataMap.put("actvDs", actvDs);
		response.setPayload(dataMap);

		// Intercept the message and send it to NLE orchestrator. If the orchestrator cannot process it (i.e. returns
		// null), then it will be processed by Rivescript.
		ChatbotContext chatbotContext = null;
		if (useNLE)
			chatbotContext = orchestrator.processMessage(msg);

		String reply;
		if (chatbotContext == null)
			reply = rsService.getRSResponse(msg);
		else {
			// Join multiple responses from NLE and make one string
			reply = String.join(" ", chatbotContext.getChatbotResponses());
		}
		response.setChatmsg(reply);
		return response;
	}

	@RequestMapping("/getdatatable")
	public ResponseBean getDataTable(@RequestParam(value = "actvScrId") String actvScrId, @RequestParam(value = "actvTbl") String actvTbl,
			@RequestParam(value = "actvDs") String actvDs) throws Exception {

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("actvScrId", actvScrId);
		dataMap.put("actvTbl", actvTbl);
		dataMap.put("actvDs", actvDs);
		response.setPayload(dataMap);
		dataService.getDataTable(actvDs, actvTbl);
		return response;
	}

}
