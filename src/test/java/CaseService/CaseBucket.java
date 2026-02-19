package CaseService;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.google.gson.JsonObject;
import GenericUtilities.*;
import Listner.ListnerClass;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@Listeners(Listner.ListnerClass.class)
public class CaseBucket extends BaseClass {
	
	String unverified_doc_code_1;
	String UnverifiedCode_1;
	String UnverifiedCode_2;
	String UnverifiedType_1;
	String UnverifiedType_2;
	String UnverifiedCate_1;
	String UnverifiedCate_2;
	String query_unverified_case_document;
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
	
	@Test  (priority=1, dependsOnMethods = "GetcaseCode")
	public void GET_UnverifiedDocuments() throws Throwable {
		
		setBaseURI("documents");
		ListnerClass.reportLog("Description - GET unverified document for the case application <br>Testcase Type - Positive<br>API Endpoint - /documentservice/unverified");

		Response response = RestAssured.given()
				.param("case_code", case_code)
				.header("internal-access-token","Bearer <REDACTED_TOKEN>")
				.when().get(EndPoint.DOCUMENTGETUNVERIFIED);
		
		
		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");

		UnverifiedType_1 = jp.getString("data.unverified[0].document_type");
		UnverifiedCode_1 = jp.getString("data.unverified[0].code");
		UnverifiedCate_1 = jp.getString("data.unverified[0].document_category");
		UnverifiedType_2 = jp.getString("data.unverified[1].document_type");		
		UnverifiedCode_2 = jp.getString("data.unverified[1].code");
		UnverifiedCate_2 = jp.getString("data.unverified[1].document_category");
		
		
        if(response.statusCode()==200) 
        {
			ListnerClass.reportLog("Bulk document upload api response : success -" + SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Bulk document upload api failed with response : success - " + SuccessMessage +"& response code - "+response.statusCode());
	}
	}
	
	@Test (priority = 2, dependsOnMethods = "GetcaseCode")
	public void PUT_MovetoDescrepant() throws Throwable
	{	
		setBaseURI("documents");
		ListnerClass.reportLog("Description - Move document from Unverified to Descrepant bucket <br>Testcase Type - Positive<br>API Endpoint - /doc/{DcoCode}/?multi_map_id=0");			
		query_unverified_case_document = DataBaseUtility.getQuery("query_unverified_case_document", case_code);		
		unverified_doc_code_1 = DataBaseUtility.returnValueByColumnname(query_unverified_case_document, "code");
		
		 JsonObject payload = new JsonObject();
	        payload.addProperty("verify", false);
	        payload.addProperty("remarks", "blur");
	        payload.addProperty("verified_by", "923809");
	        
	        JsonObject reasonMetadata = new JsonObject();
	        reasonMetadata.addProperty("blur_doc", "Your photograph is not clear. Please share a new copy with clear photograph.");
	        payload.add("reason_metadata", reasonMetadata);
	        payload.addProperty("document_type", UnverifiedType_1);
	        payload.addProperty("document_category", UnverifiedCate_1);
	        payload.addProperty("ops_remarks", "Test Unverified to Descrepant");
		
		Response response = RestAssured.given()
				.param("multi_map_id", 0)
	            .contentType("application/json")
	            .body(payload.toString())
	            .header("internal-access_token", System.getenv("m2m_token"))
	            .when().put(EndPoint.DOCUMENTBUCKETMOVEPUT, UnverifiedCode_1);
				
        String query_case_doc_by_docCode = DataBaseUtility.getQuery("query_case_doc_by_docCode", unverified_doc_code_1);		
		String docCodeStatus = DataBaseUtility.returnValueByColumnname(query_case_doc_by_docCode, "Status");
		
		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("message");
		String is_VerifiedValue = jp.getString("data.is_verified");
		
		if (response.statusCode()==200 && is_VerifiedValue.equals("false"))
		{
			ListnerClass.reportLog("Case moved to descrepant bucket with message - "+ SuccessMessage);
		}
		
		else
		{
			ListnerClass.reportLogFail("Case not moved to descrepant bucket with status code - "+ response.statusCode()+" message - "+SuccessMessage);
		}	
		
	}
	
	
	
	@Test (priority = 3)
	public void PUT_MovetoCleared()
	{	
		ListnerClass.reportLog("Description - Move document from Descrepant to Cleared bucket <br>Testcase Type - Positive<br>API Endpoint - /doc/{DcoCode}/?multi_map_id=0");
		
		query_unverified_case_document = DataBaseUtility.getQuery("query_unverified_case_document", case_code);		
		String unverified_doc_code_2 = DataBaseUtility.returnValueByColumnname(query_unverified_case_document, "code");
		
		
		 JsonObject payload = new JsonObject();
	        payload.addProperty("verify", true);
	        payload.addProperty("remarks", "");
	        payload.addProperty("verified_by", "923809");
	        
	        JsonObject reasonMetadata = new JsonObject();
	        reasonMetadata.addProperty("blur_doc", "");
	        
	        payload.add("reason_metadata", reasonMetadata);
	        
	        payload.addProperty("document_type", UnverifiedType_2);
	        payload.addProperty("document_category", UnverifiedCate_2);
	        payload.addProperty("ops_remarks", "Test Descrepant to Cleared");

		
		Response response = RestAssured.given()
				.param("multi_map_id", 0)
	            .contentType("application/json")
	            .body(payload.toString())
	            .header("internal-access_token", System.getenv("m2m_token"))
	            .when().put(EndPoint.DOCUMENTBUCKETMOVEPUT, UnverifiedCode_2);
        
        String query_case_doc_by_docCode = DataBaseUtility.getQuery("query_case_doc_by_docCode", unverified_doc_code_2);		
		String docCodeStatus = DataBaseUtility.returnValueByColumnname(query_case_doc_by_docCode, "Status");
		
		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("message");
		String is_VerifiedValue = jp.getString("data.is_verified");
		
		if (response.statusCode()==200 && is_VerifiedValue.equals("true"))
		{
			ListnerClass.reportLog("Case moved to descrepant bucket with message - "+ SuccessMessage);
		}
		
		else
		{
			ListnerClass.reportLogFail("Case not moved to descrepant bucket with status code - "+ response.statusCode()+" message - "+SuccessMessage);
		}
	}
	
	@Test (priority=4)
	public void GET_VerifiedDocuments() {

		ListnerClass.reportLog("Description - GET Verified document uploaded for the application. <br>Testcase Type - Positive<br>API Endpoint - /v1/verified-documents");

		Response response = RestAssured.given()
				.param("case_code", case_code)
				.header("internal-access-token",System.getenv("m2m_token"))
				.when().get(EndPoint.DOCUMENTGETVERIFIED);
		
		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");		
		
		if (response.statusCode()==200 && SuccessMessage.equals("true"))
		{
			ListnerClass.reportLog("All descrepant and Cleared document received in GET api response - success : "+SuccessMessage);
		}
		else
		{
			ListnerClass.reportLogFail("Descrepant and Cleared document not received in GET api response with status code - "+response.statusCode()+" & success : "+SuccessMessage);
		}
		
	}
	
	@Test(dependsOnMethods = "GET_UnverifiedDocuments" ,priority = 5)
	public void DELETE_MovetoDeleted()
	{	
		ListnerClass.reportLog("Description - Move document from Unverified to Deleted bucket <br>Testcase Type - Positive<br>API Endpoint - /doc/{DcoCode}/?multi_map_id=0");
		
		 JsonObject payload = new JsonObject();
	        payload.addProperty("multi_map_id", 0);
		
		Response response = RestAssured.given()
	            .contentType("application/json")
	            .body(payload.toString())
	            .header("internal-access_token", System.getenv("m2m_token"))
	            .when().delete(EndPoint.DOCUMENTBUCKETMOVEPUT, UnverifiedCode_1);
				
        JsonPath jp = response.jsonPath();
		String MessageStateUpdate = jp.getString("message");
		String is_VerifiedValue = jp.getString("data.is_verified");
		
		if (response.statusCode()==200 && is_VerifiedValue.equals("false"))
		{
			ListnerClass.reportLog("Case moved to Deleted bucket with message - "+ MessageStateUpdate);
		}
		
		else
		{
			ListnerClass.reportLogFail("Case not moved to deleted bucket with message - "+MessageStateUpdate + " & status code - " + response.statusCode());
		}        
	}
	
	@Test (priority=6)
	public void GET_DeletedDocuments() {

		ListnerClass.reportLog("Description - GET deleted document for the case application <br>Testcase Type - Positive<br>API Endpoint - /documentservice/deleted");

		Response response = RestAssured.given()
				.param("case_code", case_code)
				.header("internal-access-token",System.getenv("m2m_token"))
				.when().get(EndPoint.DOCUMENTGETDELETED);
		
		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");		
		
		if (response.statusCode()==200 && SuccessMessage.equals("true"))
		{
			ListnerClass.reportLog("Deleted document received in GET api response - success : "+SuccessMessage);
		}
		else
		{
			ListnerClass.reportLogFail("Deleted document not received in GET api response - success : "+SuccessMessage+ " & status code - "+ response.statusCode());
		}
	}
}
