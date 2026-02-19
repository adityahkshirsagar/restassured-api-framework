package CaseService;

import java.util.List;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import GenericUtilities.DataBaseUtility;
import GenericUtilities.EndPoint;
import GenericUtilities.Filepath;
import GenericUtilities.BaseClass;
import Listner.ListnerClass;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * This class is used to perform regression testing on Pending Case API.
 * 
 * @Endpoint /pending
 */

@Listeners(Listner.ListnerClass.class)
public class PendingCase extends BaseClass {
	
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
	 * This method is used to perform positive testing on Pending Case API without params for proprietorship case.
	 * @throws Throwable 
	 */
	@Test(dependsOnMethods = "GetcaseCode", priority=2)
	public void GET_PendingDocument() throws Throwable {
		setBaseURI("documents");

		ListnerClass.reportLog("Description - Get all pending documents for the Propritorship application <br>Testcase Type - Positive<br>API Endpoint - /pending");
	
		Response response = RestAssured.given()
				.param("case_code", case_code)
				.when().get(EndPoint.DOCUMENTPENDING);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");
		
		
        if(response.statusCode()==200) 
		{
			ListnerClass.reportLog("Pendind document API : success -" + SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Case Upload Failed with status code - " + response.statusCode());
	}
	}
	@Test (dependsOnMethods = "GetcaseCode", priority=3)
	public void GET_PendingDocPartnership() throws Throwable {
		
		setBaseURI("documents");

		ListnerClass.reportLog("Description - Get all pending documents for the Partnership application <br>Testcase Type - Positive<br>API Endpoint - /pending");
		String update_query_legStat_partnership = DataBaseUtility.getQuery("update_query_legStat_partnership", case_code);
		databaseutil.executeUpdateQuery(update_query_legStat_partnership);

		Response response = RestAssured.given()
				.param("case_code", case_code)
				.when().get(EndPoint.DOCUMENTPENDING);

		JsonPath jp = response.jsonPath();
		String PedingDocForPartnership_1 = jp.getString("data.pendingList.key");		
		List<String> PedingDocForPartnership_2= jp.getList("data.pendingList.find { it.key == 'incorporation_documents' }.required.key");
		String SuccessMessage=jp.getString("success");
	
		
		   if (PedingDocForPartnership_1.contains("incorporation_documents")&& PedingDocForPartnership_2.contains("valid_partnership_deed"))
		{
			   ListnerClass.reportLog("Pendind document API response verified for legal status : Partnership - Pendign List have Incorporation document");
		}
		
		else
		{
			ListnerClass.reportLog("Pendind document API response verified for legal status : Partnership - Pendign List does't have Incorporation document");
		}
		
		   
	        if(response.statusCode()==200) 
			{
				ListnerClass.reportLog("Pendind document API : success - " + SuccessMessage);
			}
			else {ListnerClass.reportLogFail("Case Upload Failed : success - " + response.statusCode()+" "+SuccessMessage);
		}
	}
	
	@Test (dependsOnMethods = "GetcaseCode", priority=4)
	public void GET_PendingDocPvtLtd() throws Throwable {
		
		setBaseURI("documents");

		ListnerClass.reportLog("Description - Get all pending documents for the Pvt. Ltd. application <br>Testcase Type - Positive<br>API Endpoint - /pending");

		String update_query_legStat_pvtLtd = DataBaseUtility.getQuery("update_query_legStat_pvtLtd", case_code);
		databaseutil.executeUpdateQuery(update_query_legStat_pvtLtd);
		
		Response response = RestAssured.given()
				.param("case_code", case_code)
				.when().get(EndPoint.DOCUMENTPENDING);

		JsonPath jp = response.jsonPath();
		String PedingDocForPvtLtd_1 = jp.getString("data.pendingList.key");		
		List<String> PedingDocForPvtLtd_2= jp.getList("data.pendingList.find { it.key == 'incorporation_documents' }.required.key");
		String SuccessMessage=jp.getString("success");
		
		   if (PedingDocForPvtLtd_1.contains("incorporation_documents")&& PedingDocForPvtLtd_2.contains("company_coi"))
		{
			   ListnerClass.reportLog("Pendind document API response verified for legal status : Pvt. Ltd. - Pendign List have Incorporation document's");
		}
		
		else
		{
			ListnerClass.reportLogFail("Pendind document API response verified for legal status : Pvt. Ltd. - Pendign List does't have Incorporation document's");
		}
		
		   
	        if(response.statusCode()==200) 
			{
				ListnerClass.reportLog("Pendind document API : success - " + SuccessMessage);
			}
			else {ListnerClass.reportLogFail("Case Upload Failed : success - " + response.statusCode()+" "+SuccessMessage);
		}
		
	}
	
	/**
	 * This method is used to perform positive testing on Pending Case API without params for Amt20L case.
	 * @throws Throwable 
	 */
	
	@Test  (dependsOnMethods = "GetcaseCode", priority=5)
	public void GET_PendingDocAmt20L() throws Throwable {
		
		setBaseURI("documents");

		ListnerClass.reportLog("Description - Get all pending documents for the case amount more than 20L application <br>Testcase Type - Positive<br>API Endpoint - /pending");		
		String update_query_caseAmt_2100000 = DataBaseUtility.getQuery("update_query_caseAmt_2100000", case_code);
		databaseutil.executeUpdateQuery(update_query_caseAmt_2100000);
		

		Response response = RestAssured.given()
				.param("case_code", case_code)
				.when().get(EndPoint.DOCUMENTPENDING);

		JsonPath jp = response.jsonPath();
		String PedingDocForPvtLtd_1 = jp.getString("data.pendingList.key");		
		List<String> PedingDocForPvtLtd_2= jp.getList("data.pendingList.find { it.key == 'financial' }.required.key");
		String SuccessMessage=jp.getString("success");
	
		
		   if (PedingDocForPvtLtd_1.contains("financial")&& PedingDocForPvtLtd_2.contains("itr_returns"))
		{
			   ListnerClass.reportLog("Pendind document API response verified for case Amount : More than 20L - Pendign List have financial doscument's");
		}
		
		else
		{
			ListnerClass.reportLogFail("Pendind document API response verified for case Amount : More than 20L - Pendign List does't have financial document's");
		}
		
		   
	        if(response.statusCode()==200) 
			{
				ListnerClass.reportLog("Pendind document API : success - " + SuccessMessage);
			}
			else {ListnerClass.reportLogFail("Case Upload Failed : success - " + response.statusCode()+" "+SuccessMessage);
		}
	}
	
	/**
	 * This method is used to perform positive testing on Pending Case API without params for Female case.
	 * @throws Throwable 
	 */
	
	@Test  (dependsOnMethods = "GetcaseCode", priority=6)
	public void GET_PendingDocForFemale() throws Throwable {
		
		setBaseURI("documents");
		
		ListnerClass.reportLog("Description - Get all pending documents for the case amount more than 20L application <br>Testcase Type - Positive<br>API Endpoint - /pending");
		String update_query_legStat_propritorship = DataBaseUtility.getQuery("update_query_legStat_propritorship", case_code);
		databaseutil.executeUpdateQuery(update_query_legStat_propritorship);
		String update_query_Gender_Female = DataBaseUtility.getQuery("update_query_Gender_Female", case_code);
		databaseutil.executeUpdateQuery(update_query_Gender_Female);
		

		Response response = RestAssured.given()
				.param("case_code", case_code)
				.when().get(EndPoint.DOCUMENTPENDING);

		JsonPath jp = response.jsonPath();
		String PedingDocForPvtLtd_1 = jp.getString("data.pendingList.key");		
		List<String> PedingDocForPvtLtd_2= jp.getList("data.pendingList.find {it.key == 'kyc'}.required.key");
		String SuccessMessage=jp.getString("success");	
		
		   if (PedingDocForPvtLtd_1.contains("kyc")&& PedingDocForPvtLtd_2.contains("pan_father_husband"))
		{
			   ListnerClass.reportLog("Pendind document API response verified for Gender : Female - Pendign List have pan_father_husband document's");
		}
		
		else
		{
			ListnerClass.reportLogFail("Pendind document API response verified for Gender : Female - Pendign List does't have pan_father_husband document's");
		}
		
		   
	        if(response.statusCode()==200) 
			{
				ListnerClass.reportLog("Pendind document API : success - " + SuccessMessage);
			}
			else {ListnerClass.reportLogFail("Case Upload Failed : success - " + response.statusCode()+" "+SuccessMessage);
		}
	}
	
	
	
	/**
	 * This method is used to perform positive testing on Pending Case API without source - CJP and list_type - normal.
	 * @throws Throwable 
	 */
	@Test(dependsOnMethods = "GetcaseCode",priority=7)
	public void GET_PendingDocumentV1() throws Throwable {
		
		setBaseURI("documents");

		ListnerClass.reportLog("Description - Get all pending documents for the application consumed at CJP's <br>Testcase Type - Positive<br>API Endpoint - v1/pending");		
		Response response = RestAssured.given()
				.multiPart("caseCode",case_code)
				.header("internal-access-token",System.getenv("m2m_token"))
				.when().post(EndPoint.DOCUMENTPENDINGV1);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");
		
		
        if(response.statusCode()==200) 
		{
			ListnerClass.reportLog("V1 Pendind document API : success - " + SuccessMessage);
		}
		else {ListnerClass.reportLogFail("V1 Pending document reposne : success - " + response.statusCode()+" "+SuccessMessage);
	}
		
	}
	
	
	/**
	 * This method is used to perform positive testing on Pending Case API with params list_type - post_sanction and source.
	 * @throws Throwable 
	 */
	@Test(dependsOnMethods = "GetcaseCode", priority=8)
	public void GET_PendingDoc_1() throws Throwable {
		
		setBaseURI("documents");

		ListnerClass.reportLog("Description - Get all pending documents for the application <br>Testcase Type - Positive<br>API Endpoint - /pending");

		Response response = RestAssured.given()
				.param("case_code", case_code)
				.param("list_type", "post_sanction")
				.param("source", "LOS")
				.when().get(EndPoint.DOCUMENTPENDING);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");

		
        if(response.statusCode()==200) 
		{
			ListnerClass.reportLog("Post sanction pendind document API : success - " + SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Post sanction pending document response : success - " + response.statusCode()+" "+SuccessMessage);
	}
	}
	
	
	/**
	 * This method is used to perform positive testing on Pending Case API with params colender-epimoney,list_type-normal and source.
	 * @throws Throwable 
	 */
	
	@Test(dependsOnMethods = "GetcaseCode", priority=9)
	public void GET_PendingDoc_2() throws Throwable {
		
		setBaseURI("documents");

		ListnerClass.reportLog("Description - Get all pending documents for the application with list_type - normal and colender - epimoney <br>Testcase Type - Positive<br>API Endpoint - /pending");

		Response response = RestAssured.given()
				.param("case_code", case_code)
				.param("list_type", "normal")
				.param("colender", "Epimoney")
				.param("source", "LOS")
				.when().get(EndPoint.DOCUMENTPENDING);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");

		
		if(response.statusCode()==200) 
		{
			ListnerClass.reportLog("Normal (pre-sanction) sanction pendind document API : success - " + SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Normal (pre-sanction) sanction pending document response : success - " + response.statusCode()+" "+SuccessMessage);
	}
	}
	
	/**
	 * This method is used to perform positive testing on Pending Hardcopy Case API.
	 * @throws Throwable 
	 */
	
	@Test(dependsOnMethods = "GetcaseCode", priority=10)
	public void GET_PendingHardcopy() throws Throwable {
		
		setBaseURI("documents");		
	
		ListnerClass.reportLog("Description - Pending hardcopy document API<br>Testcase Type - Positive<br>API Endpoint - /hardcopy/{caseCode}");
				
		Response response = RestAssured.given()
	            .when().get(EndPoint.DOCUMENTPENDINGHARDCOPY,case_code);
		
		
		
		if(response.statusCode()==200) 
		{
			ListnerClass.reportLog("Pendind hardcopy document API response is success");
		}
		else {ListnerClass.reportLogFail("Pendind hardcopy document response failed with status code " + response.statusCode());
	}
			
	}
	
	
	/**
	 * This method is used to perform positive testing to Ignore Case API.
	 * @throws Throwable 
	 */
	
	@Test(dependsOnMethods = "GetcaseCode", priority=11)
	public void POST_DocumentIgnore() throws Throwable {
		
		setBaseURI("documents");
		ListnerClass.reportLog("Description - Get all pending ignore documents with bank_statement for the application<br>Testcase Type - Positive<br>API Endpoint - /pending");

		query_case_document_ignore = DataBaseUtility.getQuery("query_case_document_ignore", case_code);

		JsonObject payload = new JsonObject();
		payload.addProperty("case_code", case_code);
		payload.addProperty("approval_type", "DOCUMENT_DEFERRAL");
		payload.addProperty("requested_from", 923809);
		payload.addProperty("requestor_remark", "Test Ignore document remarks");
		payload.addProperty("requested_to", 923809);
		payload.addProperty("approver_remark", "Test Case Ignored by Regression script");
		payload.addProperty("request_status", "APPROVED");
		payload.addProperty("requested_date", "2024-09-13 18:47:18");
		payload.addProperty("action_date", "2024-09-13 18:47:57");
		payload.addProperty("reminder_date", "2024-09-13 18:47:18");
		payload.addProperty("reminder_count", 1);
		payload.addProperty("source", "LOS");
		payload.addProperty("active", 1);
		payload.addProperty("code", "db3b5378-0852-4616-9");
		payload.addProperty("requested_name", "Automation Script");
		payload.addProperty("approval_name", "Automation Script");
		payload.addProperty("days", 0);
		payload.addProperty("lead_code", lead_code);
		payload.addProperty("application_status", "IP_FRESH_REGISTRATION");
		payload.addProperty("receiver", "sample@example.invalid");
		payload.addProperty("sender", "sample@example.invalid");

		JsonObject metadata = new JsonObject();
		JsonArray documentArray = new JsonArray();

		JsonObject document = new JsonObject();
		document.addProperty("type", "Bank statement of last 6 months (Current Account)");
		document.addProperty("label", "Bank statement of last 6 months (Current Account)");
		document.addProperty("category", "bank_statement");
		document.addProperty("doc_type", "bank_statement_current_6");
		document.addProperty("selected", true);
		document.addProperty("doc_category", "bank_statement");

		documentArray.add(document);
		metadata.add("document", documentArray);
		payload.add("metadata", metadata);

		Response response = RestAssured.given().contentType("application/json").body(payload.toString())
				.when().post(EndPoint.DOCUMENTIGNORE);

		String ignore_doc_case_code = databaseutil
				.returnValueByColumnname(query_case_document_ignore, "case_code");

		if (ignore_doc_case_code == null) 
		{
			ListnerClass.reportLog("Entry not get created in case_document_ignore with case_code");
		}

		else 
		{
			ListnerClass.reportLog("Entry get created in case_document_ignore with case_code - "+ ignore_doc_case_code);
		}
		
		
		if(response.statusCode()==200) 
		{
			ListnerClass.reportLog("Ignore document api response is success");
		}
		else {ListnerClass.reportLogFail("Ignore document api response failed with status code " + response.statusCode());
	}
	}
	
	/**
	 * This method is used to perform positive testing on Pending Case API with params list_type - post_sanction and source - CJP.
	 * @throws Throwable 
	 */
	
	@Test(dependsOnMethods = "GetcaseCode", priority=12)
	public void GET_PendingSummary_1() throws Throwable {
		
		setBaseURI("documents");
					
		ListnerClass.reportLog("Description - Pending summary API verification with list_type - post_sanction and source - CJP<br>Testcase Type - Positive<br>API Endpoint - /pending/summary");
				
		Response response = RestAssured.given()
	            .param("case_code", case_code)
				.param("list_type", "post_sanction")
				.param("source", "CJP")
	            .when().get(EndPoint.DOCUMENTPENDINGSUMMARY);
		
		
		if(response.statusCode()==200) 
		{
			ListnerClass.reportLog("Pending document summary response is success");
		}
		else {ListnerClass.reportLogFail("Pending document summary api response failed with status code " + response.statusCode());
	}	
	}
	
	/**
	 * This method is used to perform positive testing on Pending Case API with params list_type - normal and source - LOS.
	 * @throws Throwable 
	 */
	
	@Test(dependsOnMethods = "GetcaseCode", priority=13)
	public void GET_PendingSummary_2() throws Throwable {
		
		setBaseURI("documents");
		
		ListnerClass.reportLog("Description - Pending summary API verification with list_type - normal and source - LOS<br>Testcase Type - Positive<br>API Endpoint - /pending/summary");
				
		Response response = RestAssured.given()
	            .param("case_code", case_code)
				.param("list_type", "normal")
				.param("source", "LOS")
	            .when().get(EndPoint.DOCUMENTPENDINGSUMMARY);

		
		if(response.statusCode()==200) 
		{
			ListnerClass.reportLog("Pending document summary response is success");
		}
		else {ListnerClass.reportLogFail("Pending document summary api response failed with status code " + response.statusCode());
	}		
	}
}
