package upb.ida.provider;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rivescript.macro.Subroutine;
import upb.ida.bean.cluster.ClusterParam;
import upb.ida.util.DataDumpUtil;
/**
 * ParamsHandler is a subroutine that fetches paramameters
 * for a gven algorithm.
 * 
 * @author Faisal
 *
 */
@Component
public class ParamsHandler implements Subroutine {
	
	@Autowired
	private DataDumpUtil DataDumpUtil;
	/**
	 * Method to send parameters list with optiional tags to the user
	 * as a response when user selects an algorithm for clustering
	 * @param rs
	 *            - {@link call#rs}
	 * @param args
	 *            - {@link call#args}
	 * @return - String "paramaters List as string"  or "fail"
	 */
	public String call (com.rivescript.RiveScript rs, String[] args) {

		try {
		
			List<ClusterParam> paramList=  new ArrayList<>();
			 /**
             * getting List of type ClusterParam for parameters of an algorithm
             */
			paramList=DataDumpUtil.getClusterAlgoParams(args[0]);
            String algoStr = null;
            /**
             * checking if parameter is optional
             */
            if(!paramList.get(0).isOptional()) {
            	//algoStr="<br>"+paramList.get(0).getName()+"[Mandatory]";
            	algoStr="<br>";
            }
            
            else{
            	algoStr="<br>"+ 1 +" :- " + paramList.get(0).getName() + "  [ Optional ] ";
            }
            for (int i = 0; i < paramList.size()-1; i++) {
            	if(paramList.get(i+1).isOptional()) {
            	algoStr=algoStr+"<br>"+(i+1)+ ":- " + paramList.get(i+1).getName() + "  [Optional] ";
            	}
//            	else {
//            		algoStr=algoStr+"<br>"+paramList.get(i+1).getName();
//            	}
            }
            /**
             * returning paramters as string
             */
            return algoStr+"<br><br>Which optional parameters you want?<br><br>";
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 /**
         * return fail if no parameters
         */
		return "fail";
	
	}
}


