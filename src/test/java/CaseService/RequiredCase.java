package CaseService;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.Assert;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import GenericUtilities.*;
import Listner.ListnerClass;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@Listeners(Listner.ListnerClass.class)
public class RequiredCase extends BaseClass {
	
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
	 * This method is used to create requirement of document.
	 */
	
	@Test (priority=1,dependsOnMethods = "GetcaseCode")
	public void POST_RequiredDoc()
	
	{
		
		ListnerClass.reportLog("Description - Required document api can create pendency of perticular document <br>Testcase Type - Positive<br>API Endpoint - /required");
		
		Map<String, Object> ReqBody = new HashMap<>();
		ReqBody.put("case_code", case_code);
		ReqBody.put("document_category", "declarations");
		ReqBody.put("document_type", "dual_name_declaration");
		
		Response response = RestAssured.given()
				.body(ReqBody)
				.header("content-type","application/json" )
				.when().post(EndPoint.DOCUMENTREQUIRED);
		
		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");
		String RespMessage = jp.getString("message");
		
		String query_custom_required_document = DataBaseUtility.getQuery("query_custom_required_document",case_code);		
		String DocPendEntryForcaseCode = DataBaseUtility.returnValueByColumnname(query_custom_required_document, "case_code");
		
		if (SuccessMessage.equals("true")&&DocPendEntryForcaseCode.equals(case_code))
		{
			ListnerClass.reportLog("Case custom pendency created successfully with : Message - "+RespMessage);
		}
		else
		{
			ListnerClass.reportLogFail("Case custom pendency not created with : Message - "+RespMessage);
		}
		
        if(response.statusCode()==200) 
		{
			ListnerClass.reportLog("Case requirement created successfully : Success -" + RespMessage);
		}
		else {ListnerClass.reportLogFail("Case requirement generation failed - "+response.statusCode()+ " & "+RespMessage);
	}
	}
	
	/**
	 * This method is used to verify negative scenario of adding requirement of document which is already exists.
	 */
	
	@Test (priority=2)
	public void POST_RequiredDoc_01()
	
	{	
		ListnerClass.reportLog("Description - Required document does not create requirement if document already covered in pending document <br>Testcase Type - Negative<br>API Endpoint - /required");
		
		Map<String, Object> ReqBody = new HashMap<>();
		ReqBody.put("case_code", case_code);
		ReqBody.put("document_category", "res_address_proof");
		ReqBody.put("document_type", "aadhar");
		
		Response response = RestAssured.given()
				.body(ReqBody)
				.header("content-type","application/json" )//	            .log().all() // Log request and response details
				.when().post(EndPoint.DOCUMENTREQUIRED);
		
		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");
		String RespMessage = jp.getString("error.message");
		
		if (SuccessMessage.equals("false"))
		{
			ListnerClass.reportLog("Case custom pendency not created for existing pending document with : Message - "+RespMessage);
		}
		else
		{
			ListnerClass.reportLogFail("Case custom pendency get created for existing pending document with : Message - "+RespMessage);
		}
		
        if(response.statusCode()==400) 
		{
			ListnerClass.reportLog("Case requirement not created successfully : Success -" + RespMessage);
		}
		else {ListnerClass.reportLogFail("Case requirement generation failed - "+response.statusCode()+ " & "+RespMessage);
	}
	}
	
	/**
	 * This method is used to create requirement of document in bulk.
	 */
	
	@Test (priority=3)
	public void POST_RequiredDocBulk()
	
	{	
		
		ListnerClass.reportLog("Description - Required bulk document API does create requirement if document already not covered in pending list in bulk manner <br>Testcase Type - Positive<br>API Endpoint - /required/bulk");
		
		JsonObject payload = new JsonObject();
        payload.addProperty("case_code", case_code);
        payload.addProperty("user_id", "923809");
        
        JsonArray requestDocsArray = new JsonArray();
        
        JsonObject requestDocs_1 = new JsonObject();
        requestDocs_1.addProperty("document_category", "insurance_documents");
        requestDocs_1.addProperty("document_type", "health_insurance_policy");
        
        JsonObject requestDocs_2 = new JsonObject();
        requestDocs_2.addProperty("document_category", "declarations");
        requestDocs_2.addProperty("document_type", "form_60");
        
        requestDocsArray.add(requestDocs_1);
        requestDocsArray.add(requestDocs_2);
        
        payload.add("requestDocs", requestDocsArray);
        	
		Response response = RestAssured.given()
				.body(payload.toString())
				.header("content-type","application/json" )
				.when().post(EndPoint.DOCUMENTREQUIREDBULK);
		
		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");
		String RespMessage = jp.getString("message");
		
		String query_custom_required_document = DataBaseUtility.getQuery("query_custom_required_document",case_code);

		String PendencyCreatedCategory = DataBaseUtility.returnValueByColumnname(query_custom_required_document, "documents");
		
		
		if (PendencyCreatedCategory.contains("declarations")&&PendencyCreatedCategory.contains("insurance_documents")&&SuccessMessage.equals("true"))
		{
			ListnerClass.reportLog("Case custom pendency created in bulk manner with : Message - "+RespMessage);
		}
		else
		{
			ListnerClass.reportLogFail("Case custom pendency not created in bulk manner with : Message - "+RespMessage);
		}
		
		
        if(response.statusCode()==200) 
		{
			ListnerClass.reportLog("Case custom pendency created in bulk manner with : Success -" + RespMessage);
		}
		else {ListnerClass.reportLogFail("Case bulk pendency creation failed with - "+response.statusCode()+ " & "+RespMessage);
	}
	}

	@Test(priority = 4, dependsOnMethods = "GetcaseCode")
	public void POST_RequiredDoc_ContractValidation() {
		ListnerClass.reportLog("Description - Validate required-case response contract fields <br>Testcase Type - Contract<br>API Endpoint - /required");
		Map<String, Object> reqBody = new HashMap<>();
		reqBody.put("case_code", case_code);
		reqBody.put("document_category", "declarations");
		reqBody.put("document_type", "dual_name_declaration");

		Response response = RestAssured.given()
				.body(reqBody)
				.header("content-type", "application/json")
				.when().post(EndPoint.DOCUMENTREQUIRED);

		JsonPath jp = response.jsonPath();
		Assert.assertNotNull(jp.get("success"), "Missing 'success' in response");
		Assert.assertNotNull(jp.get("message"), "Missing 'message' in response");
	}

	@Test(priority = 5, dependsOnMethods = "GetcaseCode")
	public void POST_RequiredDocNeg_MissingCaseCode() {
		ListnerClass.reportLog("Description - Validate missing case_code request handling <br>Testcase Type - Negative Boundary<br>API Endpoint - /required");
		Map<String, Object> reqBody = new HashMap<>();
		reqBody.put("document_category", "declarations");
		reqBody.put("document_type", "dual_name_declaration");

		Response response = RestAssured.given()
				.body(reqBody)
				.header("content-type", "application/json")
				.when().post(EndPoint.DOCUMENTREQUIRED);

		Assert.assertTrue(response.statusCode() == 400 || response.statusCode() == 422,
				"Expected validation failure when case_code is missing");
	}

	@Test(priority = 6, dependsOnMethods = "GetcaseCode")
	public void POST_RequiredDocNeg_Auth_InvalidToken() {
		ListnerClass.reportLog("Description - Validate unauthorized behavior for required-case with invalid token <br>Testcase Type - Security<br>API Endpoint - /required");
		Map<String, Object> reqBody = new HashMap<>();
		reqBody.put("case_code", case_code);
		reqBody.put("document_category", "declarations");
		reqBody.put("document_type", "dual_name_declaration");

		Response response = RestAssured.given()
				.body(reqBody)
				.header("content-type", "application/json")
				.header("Authorization", "Bearer invalid_token")
				.when().post(EndPoint.DOCUMENTREQUIRED);

		Assert.assertTrue(response.statusCode() == 401 || response.statusCode() == 403,
				"Expected 401/403 for invalid token");
	}

}
