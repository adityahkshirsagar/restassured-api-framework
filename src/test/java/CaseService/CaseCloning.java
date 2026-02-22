package CaseService;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import Listner.ListnerClass;
import TestCases.Create_casecode;
import WorkFlowLibrary.UploadHelper;
import GenericUtilities.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@Listeners(Listner.ListnerClass.class)
public class CaseCloning extends BaseClass {
	String new_case_code;
	String docuement_cate_for_caseCode;
	String query_case_applicant_detail;
	Create_casecode fresh_lead = new Create_casecode();
	UploadHelper uploadHelper = new UploadHelper();

	@Test
	public void GetcaseCode() {
		query_case_applicant_detail = DataBaseUtility.getQuery("query_case_applicant_detail", case_code);
        lead_code = DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "customer_code");
        String mobile_no=DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "mobile_no");
        ListnerClass.reportLog("Description - Get pre created case_code to use as old case code while clonning  <br>Testcase Type - Pre condition<br>case code - "+case_code);
	}

	@Test
	public void caseCode_2() throws Throwable
	{

		    new_case_code=fresh_lead.Fresh_Lead(); // New case_code created to clone document from one case_code to another case_code
	        query_case_applicant_detail = DataBaseUtility.getQuery("query_case_applicant_detail", new_case_code);
	        String new_lead_code = DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "customer_code");
	        String new_mobile_no=DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "mobile_no");
	        ListnerClass.reportLog("Description - New case_code is created for the clonning operation <br>Testcase Type - Pre condition<br>case code - "+new_case_code+"<br>Lead code - "+new_lead_code+"<br>Mobile No - "+new_mobile_no);
	}

	/**
	 * This method is used to perform cloning function between two case codes with document_type.
	 * @throws Throwable
	 */

	@Test (priority=1,dependsOnMethods = {"caseCode_2","GetcaseCode"})
	public void POST_DocClonByType() throws Throwable
	{
			Response Uploadresponse = uploadHelper.uploadCaseFile("res_address_proof","aadhar",Filepath.Aadhar,case_code, "923809", "CJP");
			ListnerClass.reportLog("Description - Clone document from old case_code to new case_code with document_type <br>Testcase Type - Negative<br>API Endpoint - /services/case/clone");

			Map<String, Object> ReqBody = new HashMap<>();
			ReqBody.put("old_case_code", case_code);
			ReqBody.put("new_case_code", new_case_code);
			ReqBody.put("document_type", "aadhar");

			Response response = RestAssured.given()
					.body(ReqBody)
					.header("content-type","application/json" )
					.header("internal-access-token",System.getenv("m2m_token"))
					.when().post(EndPoint.DOCUMENTCLONNING);

			JsonPath jp = response.jsonPath();
			String SuccValue = jp.getString("success");
			String RespMessage = jp.getString("message");

	        if(response.statusCode()==200 && SuccValue.equals("true"))
			{
				ListnerClass.reportLog("Case clonning by type successfull : success -" + RespMessage);
			}
			else {ListnerClass.reportLogFail("Case clonning Failed" + RespMessage +" & response code - "+response.statusCode());
		}
	}

	/**
	 * This method is used to perform cloning function between two case codes with document_category.
	 */

	@Test(priority=2,dependsOnMethods = {"caseCode_2","GetcaseCode","POST_DocClonByType"})
	public void POST_DocClonByCat()

	{
			ListnerClass.reportLog("Description - Clone document from old case_code to new case_code with document_category <br>Testcase Type - Negative<br>API Endpoint - /services/case/clone");

			Map<String, Object> ReqBody = new HashMap<>();
			ReqBody.put("old_case_code", case_code);
			ReqBody.put("new_case_code", new_case_code);
			ReqBody.put("document_category", "res_address_proof");

			Response response = RestAssured.given()
					.body(ReqBody)
					.header("content-type","application/json" )
					.header("internal-access-token",System.getenv("m2m_token") )
					.when().post(EndPoint.DOCUMENTCLONNING);

			JsonPath jp = response.jsonPath();
			String SuccValue = jp.getString("success");
			String RespMessage = jp.getString("message");

	        if(response.statusCode()==200 && SuccValue.equals("true"))
			{
				ListnerClass.reportLog("Case clonning by category successfull : success -" + RespMessage);
			}
			else {ListnerClass.reportLogFail("Case clonning Failed" + RespMessage +" & response code - "+response.statusCode());
		}

	}

	@Test (priority=3)
	public void POST_DocReallocate()

	{
				ListnerClass.reportLog("Description - Reallocate document for the case_code <br>Testcase Type - Negative<br>API Endpoint - /services/case/reallocate");

	        	JsonObject payload = new JsonObject();
	        	payload.addProperty("type", "SOME");
	        	JsonArray idsArray = new JsonArray();
	        	idsArray.add(1);
	        	payload.add("to_ids", idsArray);
	        	payload.add("from_ids", new JsonArray());
	        	JsonArray casecodeArray = new JsonArray();
	        	casecodeArray.add(case_code);
	        	payload.add("case_codes", casecodeArray);

	        		Response response = RestAssured.given()
					.body(payload.toString())
					.header("content-type","application/json" )
					.header("internal-access-token",System.getenv("m2m_token"))
					.when().post(EndPoint.DOCUMENTREALLOCATE);

			JsonPath jp = response.jsonPath();
			String SuccValue = jp.getString("success");
			String RespMessage = jp.getString("message");

			if (SuccValue.equals("true") && response.statusCode()==200)
			{
				ListnerClass.reportLog("Case reallocated successfully with : Message - "+RespMessage);
			}
			else
			{
				ListnerClass.reportLogFail("Case not reallocated successfully with : Message - " + RespMessage +" & response code - "+response.statusCode());
			}
	}

}

