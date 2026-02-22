package CaseService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import GenericUtilities.*;
import Listner.ListnerClass;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@Listeners(Listner.ListnerClass.class)
public class RawCase extends BaseClass {

	String RawDocIdinResp;
	String lead_code;
	String query_case_raw_document;
	String query_case_doc_by_RawDocId;
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
	 * This method is used to perform positive testing on Upload Raw document API.
	 */

	@Test (dependsOnMethods = "GetcaseCode", priority=1)
	public void POST_RawDocument() {

		ListnerClass.reportLog("Description - Upload raw document for the application <br>Testcase Type - Positive<br>API Endpoint - /caseservice/raw");
		File filepath= new File(Filepath.Aadhar);
		Response response = RestAssured.given()
				.multiPart("file",filepath)
				.multiPart("case_code", case_code)
				.multiPart("source", "emailservice")
				.header("content-type","multipart/form-data")
				.when().post(EndPoint.CASEFILEUPLOADRAW);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");
		RawDocIdinResp = jp.getString("data.id");

		query_case_raw_document=DataBaseUtility.getQuery("query_case_raw_document", lead_code);
		String RawDocIdinDB=DataBaseUtility.returnValueByColumnname(query_case_raw_document, "id");

		if (SuccessMessage.equals("true")&& RawDocIdinResp.equals(RawDocIdinDB))
		{
			ListnerClass.reportLog("Raw document uploaded successssfully : Success - "+SuccessMessage+" <br>Raw document code - "+RawDocIdinResp);
		}
		else
		{
			ListnerClass.reportLogFail("Raw document not get uploaded - Success : "+SuccessMessage);
		}

        if(response.statusCode()==200)
		{
			ListnerClass.reportLog("Raw document uploaded successssfully : Success -" + SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Raw document uploaded Failed with status code - "+response.statusCode()+ " & "+SuccessMessage);
	}
	}

	/**
	 * This method is used to perform to GET raw Cases uploaded for the application (lead_code).
	 */

	@Test (dependsOnMethods = "POST_RawDocument", priority=2)
	public void GET_RawDocument() {

		ListnerClass.reportLog("Description - GET raw document uploaded for the application (lead_code) and consumed on LOS <br>Testcase Type - Positive<br>API Endpoint - /caseservice/raw");

		Response response = RestAssured.given()
				.param("case_code", case_code)
				.when().get(EndPoint.CASEFILEUPLOADRAW);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");
		String DocIdinGetResp = jp.getString("data.documents.id");

		if (SuccessMessage.equals("true") && DocIdinGetResp.equals("["+RawDocIdinResp+"]"))
		{
			ListnerClass.reportLog("Uploaded Raw document received in GET response - success : "+SuccessMessage);
		}
		else
		{
			ListnerClass.reportLogFail("Uploaded Raw document not received in GET response - success : "+SuccessMessage);
		}

        if(response.statusCode()==200)
		{
			ListnerClass.reportLog("Raw document received successssfully : Success -" + SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Raw document receiving Failed with status code - "+response.statusCode()+ " & "+SuccessMessage);
	}
	}

	/**
	 * This method is used to perform positive testing on map Raw document with documentservice/map API and verify : false.
	 */

	@Test (dependsOnMethods = "POST_RawDocument", priority=3)
	public void POST_MapDocumentUnverified() {

		ListnerClass.reportLog("Description - Raw document mapping to unverified bucket. <br>Testcase Type - Positive<br>API Endpoint - /caseservice/map");

		Map<String, Object> ReqBody = new HashMap<>();
		ReqBody.put("case_code", case_code);
		ReqBody.put("source", "API");
		ReqBody.put("document_type", "aadhar");
		ReqBody.put("document_category", "res_address_proof");
		ReqBody.put("user_id", "923809");
		ReqBody.put("raw_document_id", RawDocIdinResp);
		ReqBody.put("verify", false);

		Response response = RestAssured.given()
				.body(ReqBody)
				.header("content-type","application/json" )
				.when().post(EndPoint.CASEFILEMAP);

		JsonPath jp = response.jsonPath();
		String SuccValue = jp.getString("success");
		String SuccessMessage = jp.getString("message");

		query_case_raw_document=DataBaseUtility.getQuery("query_case_raw_document", lead_code);
		String IsMapp=DataBaseUtility.returnValueByColumnname(query_case_raw_document, "is_mapped");

		String query_case_doc_by_RawDocId=DataBaseUtility.getQuery("query_case_doc_by_RawDocId", RawDocIdinResp);
		String RawDocMapinDB =DataBaseUtility.returnValueByColumnname(query_case_doc_by_RawDocId, "document_category");

		if (SuccValue.equals("true") && IsMapp.equals("1"))
		{
			ListnerClass.reportLog("Raw document uploaded successssfully with : Message - "+SuccessMessage+" <br>Case mapped with doc category and doc type");
		}
		else
		{
			ListnerClass.reportLogFail("Raw document not mapped successssfully with : Message - "+SuccessMessage);
		}

        if(response.statusCode()==200)
		{
			ListnerClass.reportLog("Raw document received successssfully : Success -" + SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Raw document receiving Failed with status code - "+response.statusCode()+ " & "+SuccessMessage);
	}
	}

	/**
	 * This method is used to perform positive testing on map Raw document and mark verify : true (VERIFIED).
	 */

	@Test (dependsOnMethods = "GetcaseCode", priority=4)
	public void POST_MapDocumentVerified() {

		ListnerClass.reportLog("Description - Raw document mapping to verified bucket. <br>Testcase Type - Positive<br>API Endpoint - /caseservice/map");
		File filepath= new File(Filepath.Aadhar);

		Response response_1 = RestAssured.given()
				.multiPart("file",filepath)
				.multiPart("case_code", case_code)
				.multiPart("source", "emailservice")
				.header("content-type","multipart/form-data")
				.when().post(EndPoint.CASEFILEUPLOADRAW);
		        JsonPath jp_1 = response_1.jsonPath();
		 		String RawDocIdinRespforVerified = jp_1.getString("data.id");

		Map<String, Object> ReqBody = new HashMap<>();
		ReqBody.put("case_code", case_code);
		ReqBody.put("source", "API");
		ReqBody.put("document_type", "itr_returns");
		ReqBody.put("document_category", "financial");
		ReqBody.put("user_id", "923809");
		ReqBody.put("raw_document_id", RawDocIdinRespforVerified);
		ReqBody.put("verify", true);

		Response response = RestAssured.given()
				.body(ReqBody)
				.header("content-type","application/json" )
				.when().post(EndPoint.CASEFILEMAP);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");
		String Message = jp.getString("message");

		query_case_raw_document=DataBaseUtility.getQuery("query_case_raw_document", lead_code);
		String IsMapp=DataBaseUtility.returnValueByColumnname(query_case_raw_document, "is_mapped");

		query_case_doc_by_RawDocId=DataBaseUtility.getQuery("query_case_doc_by_RawDocId", RawDocIdinRespforVerified);
		String RawDocMapVerDocCategory =DataBaseUtility.returnValueByColumnname(query_case_doc_by_RawDocId, "document_category");
		String RawDocMapVeriStatus =DataBaseUtility.returnValueByColumnname(query_case_doc_by_RawDocId, "status");

		if (SuccessMessage.equals("true") && IsMapp.equals("1") && RawDocMapVerDocCategory.equals("financial") && RawDocMapVeriStatus.equals("VERIFIED"))
		{
			ListnerClass.reportLog("Raw document MAPPED successssfully and marked as VERIFIED with : Message - "+Message+" <br>Case mapped with doc category and doc type");
		}
		else
		{
			ListnerClass.reportLogFail("Raw document not mapped correctly with : Message - "+Message);
		}

        if(response.statusCode()==200)
		{
			ListnerClass.reportLog("Raw document mapped successssfully : Success -" + SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Raw document mapping Failed with status code - "+response.statusCode()+ " & "+SuccessMessage);
	}
	}

	/**
	 * This method is used to perform negative testing on map raw document api response for duplicate document_category.
	 */

	@Test (dependsOnMethods = "GetcaseCode", priority=5)
	public void POST_MapExistingRawDocument() {

		ListnerClass.reportLog("Description - Map raw document for existing document_category is not allowed with resposne 'duplicate'. <br>Testcase Type - Negative<br>API Endpoint - /caseservice/map");

		Map<String, Object> ReqBody = new HashMap<>();
		ReqBody.put("case_code", case_code);
		ReqBody.put("source", "API");
		ReqBody.put("document_type", "aadhar");
		ReqBody.put("document_category", "financial");
		ReqBody.put("user_id", "923809");
		ReqBody.put("raw_document_id", RawDocIdinResp);
		ReqBody.put("verify", true);

		Response response = RestAssured.given()
				.body(ReqBody)
				.header("content-type","application/json" )
				.when().post(EndPoint.CASEFILEMAP);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");
		String duplicateCode = jp.getString("data.code");

		if (SuccessMessage.equals("true") && duplicateCode.equals("duplicate"))
		{
			ListnerClass.reportLog("Duplicate document mapping attempted and message received : Message - "+duplicateCode);
		}
		else
		{
			ListnerClass.reportLogFail("Duplicate document mapping attempted and message received : Message - "+duplicateCode);
		}

        if(response.statusCode()==200)
		{
			ListnerClass.reportLog("Raw document mapped successssfully : Success -" + SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Raw document mapping Failed with status code - "+response.statusCode()+ " & "+SuccessMessage);
	}
	}
}



