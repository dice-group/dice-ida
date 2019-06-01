//package upb.ida.rest;
//
//import java.io.ByteArrayOutputStream;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//import org.apache.jena.query.QueryExecution;
//import org.apache.jena.query.QueryExecutionFactory;
//import org.apache.jena.query.QuerySolution;
//import org.apache.jena.query.ResultSet;
//import org.apache.jena.query.ResultSetFormatter;
//import org.apache.jena.rdf.model.RDFNode;
//import org.apache.jena.rdfconnection.RDFConnectionFuseki;
//import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
//import org.apache.jena.update.UpdateFactory;
//import org.apache.jena.update.UpdateRequest;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import upb.ida.domains.User;
//
//@RestController
//public class UserController {
//	private RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create().destination("http://127.0.0.1:3030/user");
//
//	@RequestMapping("/user/new")
//	public String insert(@RequestBody User record) throws NoSuchAlgorithmException {
//		//getLastname
//		String firstname = record.getFirstname();
//		String lastname = record.getLastname();
//		String userName = record.getUsername();
//		String password = record.getPassword();
//		String newHashPass = hashPassword(password);
//		password = newHashPass;
//		System.out.println("SHA-256 HASH:" + password);
//        
//		// In this variation, a connection is built each time.
//		try (RDFConnectionFuseki conn = (RDFConnectionFuseki) builder.build()) {
//
//			UpdateRequest request = UpdateFactory.create();
//			request.add("PREFIX dc: <http://www.w3.org/2001/vcard-rdf/3.0#>\r\n" + "PREFIX ab:<http://userdata/#>\r\n"
//					+ "INSERT DATA{ab:" + userName + " dc:firstname \"" + firstname + "\"; dc:lastname \"" + lastname + "\";  dc:username \"" + userName
//					+ "\" ; dc:password \"" + password + "\";  .}");
//			conn.update(request);
//			System.out.println(request);
//		}
//		return "Record Inserted Successfully";
//	}
//
//	@RequestMapping("/delete")
//	public String delete(@RequestBody User record) throws NoSuchAlgorithmException {
//
//		String firstname = record.getFirstname();
//		String lastname = record.getLastname();
//		String userName = record.getUsername();
//		String password = record.getPassword();
//		String newHashPass = hashPassword(password);
//		password = newHashPass;
//
//		// In this variation, a connection is built each time.
//		try (RDFConnectionFuseki conn = (RDFConnectionFuseki) builder.build()) {
//			UpdateRequest request = UpdateFactory.create();
//
//			request.add("PREFIX dc: <http://www.w3.org/2001/vcard-rdf/3.0#>\r\n" + "PREFIX ab:<http://userdata/#>\r\n"
//					+ "DELETE DATA\r\n" + "{\r\n" + "  ab:" + userName + " dc:firstname \"" + firstname + "\" ;\r\n"
//					+ "dc:password \"" + password + "\" ;\r\n" + "dc:lastname\"" + lastname + "\"; \r\n" + "dc:username \"" + userName
//					+ "\".\r\n" + "}");
//
//			conn.update(request);
//		}
//		return "Record Deleted Successfully";
//	}
//
//	@RequestMapping("/user/update")
//	public String update(@RequestBody User record) throws NoSuchAlgorithmException {
//
//		String newPassword = record.getNewpassword();
//		String firstname = record.getFirstname();
//		String lastname = record.getLastname();
//		String userName = record.getUsername();
//		String password = record.getPassword();
//		String newHashPass = hashPassword(password);
//		String newPasswordHash = hashPassword(newPassword);
//		password = newHashPass;
//		newPassword = newPasswordHash;
//
//		// In this variation, a connection is built each time.
//		try (RDFConnectionFuseki conn = (RDFConnectionFuseki) builder.build()) {
//
//			UpdateRequest request = UpdateFactory.create();
//
//			// The idea is SPARQL is not for relational data! Its for graph data
//			// So here we are just deleting the old recorded and inserting new one
//
//			request.add("PREFIX dc: <http://www.w3.org/2001/vcard-rdf/3.0#>\r\n" + "PREFIX ab:<http://userdata/#>\r\n"
//					+ "DELETE DATA\r\n" + "{\r\n" + "  ab:" + userName + "    dc:firstname \"" + firstname + "\" ;\r\n"
//					+ "dc:password \"" + password + "\" ;\r\n" + "dc:lastname\"" + lastname + "\"; \r\n" + "}");
//			request.add("PREFIX dc: <http://www.w3.org/2001/vcard-rdf/3.0#>\r\n" + "PREFIX ab:<http://userdata/#>\r\n"
//					+ "INSERT DATA{ab:" + userName + " dc:firstname \"" + firstname + "\" ; \r\n" + "dc:lastname\"" +lastname + "\"; dc:password \"" + newPassword
//					+ "\" .}");
//			conn.update(request);
//		}
//		return "Record Updated Successfully";
//	}
//
//	@GetMapping("/user/list")
//	public String select() {
//		// In this variation, a connection is built each time.
//		try (RDFConnectionFuseki conn = (RDFConnectionFuseki) builder.build()) {
//			QueryExecution qExec = conn.query("prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
//					+ "prefix owl: <http://www.w3.org/2002/07/owl#> "
//					+ "SELECT ?subject ?predicate ?object WHERE {?subject ?predicate ?object }");
//			ResultSet rs = qExec.execSelect();
//
//			// Converting results into JSON
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			ResultSetFormatter.outputAsJSON(outputStream, rs);
//			return new String(outputStream.toByteArray());
//		}
//	}
//
////	@RequestMapping("/selectb") // one by one
////	public String select2(@RequestBody User record) {
////		String userName = record.getUsername();
////		// In this variation, a connection is built each time.
////
////		try (RDFConnectionFuseki conn = (RDFConnectionFuseki) builder.build()) {
////			QueryExecution qExec = conn.query("prefix ab:<http://userdata/#" + userName + "> \r\n"
////					+ "prefix cd: <http://www.w3.org/2001/vcard-rdf/3.0#>\r\n" + "select ?name ?username ?password \r\n"
////					+ "	where {ab: cd:name ?name ; cd:password ?password ; cd:username ?username .}\r\n" + "");
////
////			ResultSet rs = qExec.execSelect();
////			System.out.println("result is:" + rs);
////
////			// Converting results into JSON
////			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
////			ResultSetFormatter.outputAsJSON(outputStream, rs);
////			String jsonOutput = new String(outputStream.toByteArray());
////
////			return jsonOutput;
////		}
////	}
//	
//	@RequestMapping("/selectb") // one by one
//	public User select2(@RequestBody User record) {
//	String userName = record.getUsername();
//	String serviceURI = "http://127.0.0.1:3030/user";
//	String query1	=	"prefix ab:<http://userdata/#" + userName + "> \r\n"
//			+ "prefix cd: <http://www.w3.org/2001/vcard-rdf/3.0#>\r\n" + "select ?firstname ?lastname ?username ?password \r\n"
//			+ "	where {ab: cd:firstname ?firstname ;cd:lastname ?lastname ; cd:password ?password ; cd:username ?username .}\r\n" + "";
//		
//	String fetchedUserName = null;
//	String fetchedFirstName = null;
//	String fetchedLastName = null;
//	String fetchedPassword = null;
//	User obj = null;
//	
//	QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query1);
//	ResultSet results = q.execSelect();
//
//	while (results.hasNext()) {
//		QuerySolution soln = results.nextSolution();
//		// assumes that you have an "?x" in your query
//		RDFNode x = soln.get("username");
//		RDFNode y = soln.get("firstname");
//		RDFNode w = soln.get("lastname");
//		RDFNode z = soln.get("password");
//		System.out.println("username"+x);
//		System.out.println("firstname"+y);
//		System.out.println("lastname"+w);
//		System.out.println("password"+z);
//		
//		fetchedUserName = x.toString();
//		fetchedFirstName = y.toString();
//		fetchedLastName = w.toString();
//		fetchedPassword = z.toString();
//		obj = new User(fetchedUserName,fetchedPassword, fetchedFirstName,fetchedLastName);
//	}
//	return obj;
//}
//	
//	public static User list(String clientUserName) {
//		String userName = clientUserName;
//		String serviceURI = "http://localhost:3030/user";
//		String query1	=	"prefix ab:<http://userdata/#" + userName + "> \r\n"
//				+ "prefix cd: <http://www.w3.org/2001/vcard-rdf/3.0#>\r\n" + "select ?name ?username ?password \r\n"
//				+ "	where {ab: cd:name ?name ; cd:password ?password ; cd:username ?username .}\r\n" + "";
//		String fetchedUserName = null;
//		String fetchedFirstName = null;
//		String fetchedLastName = null;
//		String fetchedPassword = null;
//		User obj = null;
//		
//		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query1);
//		ResultSet results = q.execSelect();
//
//		while (results.hasNext()) {
//			QuerySolution soln = results.nextSolution();
//			// assumes that you have an "?x" in your query
//			RDFNode x = soln.get("username");
//			RDFNode y = soln.get("firstname");
//			RDFNode w = soln.get("lastname");
//			RDFNode z = soln.get("password");
//			System.out.println("username"+x);
//			System.out.println("firstname"+y);
//			System.out.println("lastname"+w);
//			System.out.println("password"+z);
//			
//			fetchedUserName = x.toString();
//			fetchedFirstName = y.toString();
//			fetchedPassword = z.toString();
//			fetchedLastName = w.toString();
//			
//			obj = new User(fetchedUserName,fetchedFirstName,fetchedLastName,fetchedPassword);
//		}
//		
//		return obj;
//	}
//	// password hashing
//
//	public static String hashPassword(String Pass) throws NoSuchAlgorithmException {
//
//		String data = Pass;
//
//		String algorithm = "SHA-256"; 
//		generateHash(data, algorithm);
//		// System.out.println("SHA-256 HASH:"+ generateHash(data, algorithm));
//		return generateHash(data, algorithm);
//
//	}
//
//	private static String generateHash(String data, String algorithm) throws NoSuchAlgorithmException {
//		MessageDigest digest = MessageDigest.getInstance(algorithm);
//		digest.reset();
//		byte[] hash = digest.digest(data.getBytes());
//		return bytesToStringHex(hash);
//
//	}
//
//	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
//
//	private static String bytesToStringHex(byte[] bytes) {
//		char[] hexChars = new char[bytes.length * 2];
//		for (int j = 0; j < bytes.length; j++) {
//			int v = bytes[j] & 0xFF;
//			hexChars[j * 2] = hexArray[v >>> 4];
//			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//		}
//
//		return new String(hexChars);
//	}
//
//}
