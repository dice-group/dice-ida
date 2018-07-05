package upb.ida.provider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rivescript.macro.Subroutine;
import com.rivescript.util.StringUtils;

import upb.ida.bean.ResponseBean;
import upb.ida.constant.IDALiteral;
import upb.ida.temp.DemoMain;

@Component
public class LoadDataContent implements Subroutine {
	@Autowired
	private DemoMain demoMain;
	@Autowired
	private ResponseBean responseBean;

	public String call(com.rivescript.RiveScript rs, String[] args) {
		String message = StringUtils.join(args, " ").trim();
		// String user = rs.currentUser();
		if (DemoMain.datasetExists(message)) {
			try {
				Map<String, Object> dataMap = responseBean.getPayload();
				dataMap.put("label", message);
				dataMap.put("dsName", message);
				dataMap.put("dataset", demoMain.getDatasetContent(message));
				responseBean.setPayload(dataMap);
				responseBean.setActnCode(IDALiteral.UIA_LOADDS);
				return IDALiteral.RESP_PASS_ROUTINE;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return IDALiteral.RESP_FAIL_ROUTINE;

	}
}
