package CaseService;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import GenericUtilities.BaseClass;
import Listner.ListnerClass;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import GenericUtilities.*;

public class CaseConfig extends BaseClass {

	String query_case_applicant_detail;
	String query_case_document_ignore;
	String query_case_applicantion;

	@Test
	public void GetcaseCode() {
		query_case_applicant_detail = DataBaseUtility.getQuery("query_case_applicant_detail", case_code);
        lead_code = DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "customer_code");
        String mobile_no=DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "mobile_no");
        ListnerClass.reportLog("Description - Get pre created case_code to use as case code <br>Testcase Type - Pre condition<br>case code - "+case_code+"<br>Lead code - "+lead_code+"<br>Mobile No - "+mobile_no);
	}

	/**
	 * This method is used to perform positive testing on Case Config API with params case_code and source.
	 */

	@Test (dependsOnMethods = "GetcaseCode", priority=1)
	public void GET_DocumentConfig() {

		ListnerClass.reportLog("Description - Get document config for the application <br>Testcase Type - Positive<br>API Endpoint - /v1/config");

		Response response = RestAssured.given()
				.param("case_code", case_code)
				.param("source", "LOS")
				.when().get(EndPoint.DOCUMENTCONFIG);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");

		if (SuccessMessage.equals("true"))
		{
			ListnerClass.reportLog("Case config API response verified for Success : "+SuccessMessage);
		}
		else
		{
			ListnerClass.reportLogFail("Case config API response in invalid for Success : "+SuccessMessage);
		}

		if(response.statusCode()==200)
		{
			ListnerClass.reportLog("Case config API response is success");
		}
		else {ListnerClass.reportLogFail("Case config API response failed with status code " + response.statusCode() +" & Success Message - "+ SuccessMessage);
	}
	}

	/**
	 * This method is used to perform positive testing on Case Config v1 API with params case_code and source.
	 */

	@Test (dependsOnMethods = "GetcaseCode", priority=2)
	public void GET_DocumentConfigV1() {

		ListnerClass.reportLog("Description - Get document config V1 for the application consumed in CJP <br>Testcase Type - Positive<br>API Endpoint - /v1/config");

		Map<String, Object> ReqBody = new HashMap<>();
		ReqBody.put("caseCode", case_code);
		ReqBody.put("source", "cjp");

		Response response = RestAssured.given()
				.body(ReqBody)
				.header("content-type","application/json" )
				.header("internal-access-token",System.getenv("m2m_token"))
				.when().post(EndPoint.DOCUMENTCONFIGV1);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");

		if (SuccessMessage.equals("true"))
		{
			ListnerClass.reportLog("Case config V1 API response verified for Success : "+SuccessMessage);
		}
		else
		{
			ListnerClass.reportLogFail("Case config V1 API response in invalid for Success : "+SuccessMessage);
		}

		if(response.statusCode()==200)
		{
			ListnerClass.reportLog("Case config API response is success - "+ SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Case config API response failed with status code " + response.statusCode() +" & Success Message - "+ SuccessMessage);
	}
	}

	/**
	 * This method is used to perform positive testing on Case lookup API with params source.
	 */

	@Test (dependsOnMethods = "GetcaseCode", priority=3)
	public void GET_DocumentLookup() {

		ListnerClass.reportLog("Description - Get document lookup for the application <br>Testcase Type - Positive<br>API Endpoint - /lookup");

		Response response = RestAssured.given()
				.param("source", "LOS")
				.when().get(EndPoint.DOCUMENTLOOKUP);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");

		if (SuccessMessage.equals("true"))
		{
			ListnerClass.reportLog("Case lookup API response verified for Success : "+SuccessMessage);
		}
		else
		{
			ListnerClass.reportLogFail("Case lookup API response in invalid for Success : "+SuccessMessage);
		}

		if(response.statusCode()==200)
		{
			ListnerClass.reportLog("Case lookup API response is success - "+ SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Case lookup API response failed with status code " + response.statusCode() +" & Success Message - "+ SuccessMessage);
	}
	}

}

