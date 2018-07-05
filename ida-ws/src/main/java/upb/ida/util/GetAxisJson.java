package upb.ida.util;

//import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.node.ObjectNode;

public class GetAxisJson {

	// To initialize variables with parameterized constructor
	public Object newJsonObjct(String x, String y, List<Map<String, String>> lstt) throws java.io.IOException {
		List<Map<String, String>> listt = new ArrayList<Map<String, String>>();
//		ArrayList<String> x_axis = new ArrayList<String>();
		for (int i = 0; i < lstt.size(); i++) {
			HashMap<String, String> mMap = new HashMap<String, String>();
			if (lstt.get(i).containsKey(x) && lstt.get(i).containsKey(y)) {
				mMap = new HashMap<String, String>(); // create a new one!
				mMap.put(x, lstt.get(i).get(x));
				mMap.put(y, lstt.get(i).get(y));
				listt.add(mMap);
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValueAsString(listt);
		ArrayNode nodeArr1 = mapper.createArrayNode();
		nodeArr1.add(mapper.readTree(mapper.writeValueAsString(listt)));
		
//		String jjson = new Gson().toJson(listt);
//	    Object ar[] = new String[2];
//		x_axis.add(x);
//		String xaxis = new Gson().toJson(x_axis);
//		ar[0] = xaxis;
//		ar[1] = jjson;
		return nodeArr1;
	}

}