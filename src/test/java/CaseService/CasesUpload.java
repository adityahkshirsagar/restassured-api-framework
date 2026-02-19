package CaseService;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.Assert;
import com.aventstack.extentreports.Status;
import GenericUtilities.BaseClass;
import GenericUtilities.DataBaseUtility;
import GenericUtilities.EndPoint;
import GenericUtilities.Filepath;
import Listner.ListnerClass;
import TestCases.Create_casecode;
import WorkFlowLibrary.UploadHelper;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.io.File;

/**
 * This class is used to perform regression testing on Case Upload API.
 */

@Listeners(Listner.ListnerClass.class)
public class CasesUpload extends BaseClass{
	
	Create_casecode fresh_lead = new Create_casecode();
	String query_kyc;
	String query_res_address_proof;
	String query_company_kyc;
	String query_case_applicant_detail;
	UploadHelper uploadHelper = new UploadHelper();
    
		
	@BeforeSuite
	public void Fresh_Lead() throws Throwable
	{
			case_code=System.getenv("case_code");
		
			if (case_code== null || case_code.equals("NULL") || case_code.isEmpty())
			{		
			case_code=fresh_lead.Fresh_Lead();
	        
	        query_case_applicant_detail = DataBaseUtility.getQuery("query_case_applicant_detail", case_code);
	        String lead_code = DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "customer_code");
	        String mobile_no=DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "mobile_no");
	        ListnerClass.reportLog("Description - New case_code created <br>Testcase Type - Pre condition<br>case code - "+case_code+"<br>Lead code - "+lead_code+"<br>Mobile No - "+mobile_no);	        
			}
		else
			{ 
	        query_case_applicant_detail = DataBaseUtility.getQuery("query_case_applicant_detail", case_code);
	        String lead_code = DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "customer_code");
	        String mobile_no=DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "mobile_no");
	        ListnerClass.reportLog("Description - Custom case code provided by CI input <br>Testcase Type - Pre condition<br>case code - "+case_code+"<br>Lead code - "+lead_code+"<br>Mobile No - "+mobile_no);
			}
	}
	
	/**
	 * This method is used to upload documents with Case Upload API.
	 * @throws Throwable 
	 */
	
	@Test(dataProvider ="documentUploadDataProvider",priority = 1)//, dependsOnMethods = "Fresh_Lead")
	public void POST_UploadDocument(String documentCategory, String documentType, String filepath) throws Throwable {

		 ListnerClass.reportLog("Description - Upload documnets <br>Testcase Type - Positive<br>API Endpoint - /documentservice/");
		 query_kyc = DataBaseUtility.getQuery("query_kyc", case_code);
		 query_res_address_proof = DataBaseUtility.getQuery("query_res_address_proof", case_code);
		 query_company_kyc = DataBaseUtility.getQuery("query_company_kyc", case_code);
		
		Response response = uploadHelper.uploadCaseFile(documentCategory,documentType,filepath,case_code, "923809", "CJP");
	           
	        switch (documentCategory) {
            case "kyc":
            	String UploadedKYCDocCode = DataBaseUtility.returnValueByColumnname(query_kyc, "code");
            	if (UploadedKYCDocCode== null)
            	{
            		ListnerClass.reportLogFail("KYC Case not Uploaded -"+ documentCategory);
            	}
                break;
            case "res_address_proof":
            	String UploadedResAddProofDocCode = DataBaseUtility.returnValueByColumnname(query_res_address_proof, "code");
            	if (UploadedResAddProofDocCode== null)
            	{
            		ListnerClass.reportLogFail("Residential Address Proof Case not Uploaded -"+ documentCategory);
            	}
                break;
            case "company_kyc":
            	String UploadedCompanyKYCDocCode = DataBaseUtility.returnValueByColumnname(query_company_kyc, "code");
            	
            	if (UploadedCompanyKYCDocCode== null)
            	{
            		ListnerClass.reportLogFail(" Case not Uploaded -"+ documentCategory);
            	}
                break;
                
            default:
                throw new IllegalArgumentException("Case not present in DB for document category: " + documentCategory);
        }
	        	        
	        
	        if(response.statusCode()==200) 
			{
				ListnerClass.reportLog("Case Uploaded successfully - "+ documentCategory);
			}
			else {ListnerClass.reportLogFail("Case Upload Failed");
		}
	}
	
	/**
	 * This method is used as data provider for document upload API.
	 */
	@DataProvider(name = "documentUploadDataProvider")
	public Object[][] documentData() {
	    return new Object[][] {
	        {"kyc", "pan_personal", Filepath.pan},
	        {"company_kyc", "gst_registration",Filepath.GSTCertficate},
		    {"res_address_proof", "aadhar",Filepath.Aadhar},    
	    };
	}
		
	/**
	 * This method is used to upload existing documents with Case Upload API and verify failure response.
	 * @throws Throwable 
	 */
	@Test(dataProvider ="documentUploadDataProvider",priority = 2)//,dependsOnMethods = "Fresh_Lead")
	public void POST_UploadDocumentNeg_1(String documentCategory, String documentType, String filepath) throws Throwable {
		
		ListnerClass.reportLog("Description - Upload document with existing set of Case category, Case type and File Name <br>Testcase Type - Negative<br>API Endpoint - /documentservice/");
		Response response = uploadHelper.uploadCaseFile(documentCategory,documentType,filepath,case_code, "923809", "CJP");
		
		JsonPath jp = response.jsonPath();
		String ErrorMessage = jp.getString("error.message");
		if(response.statusCode()==400) 
		{
			ListnerClass.reportLog("Existing document not uploaded with error - " + ErrorMessage);
		} 
	}
		
	/**
	 * This method is used to verify error response with Case Upload API with mismatch doc_type and doc_category.
	 * @throws Throwable 
	 */
	
	@Test (priority = 3)//, dependsOnMethods = "Fresh_Lead")
	public void POST_UploadDocumentNeg_2() throws Throwable {
		
		
		ListnerClass.reportLog("Description - Upload remaining documnets with Case type not matching with Case Category with config<br>Testcase Type - Negative<br>API Endpoint - /documentservice/");
		
		Response response = uploadHelper.uploadCaseFile("res_address_proof","itr_returns",Filepath.Aadhar,case_code, "923809", "CJP");
		
		
		JsonPath jp = response.jsonPath();
		String ErrorMessage = jp.getString("error.message");
		if(response.statusCode()==400) 
		{
			ListnerClass.reportLog("Case not get uploaded if document type and document category not matching with error - "+ErrorMessage);
		}
	}

	@Test(priority = 4)
	public void POST_UploadDocument_ContractValidation() throws Throwable {
		ListnerClass.reportLog("Description - Validate upload response contract fields <br>Testcase Type - Contract<br>API Endpoint - /documentservice/");
		Response response = uploadHelper.uploadCaseFile("kyc", "pan_personal", Filepath.pan, case_code, "923809", "CJP");
		JsonPath jp = response.jsonPath();
		Assert.assertEquals(response.statusCode(), 200, "Expected upload success status");
		Assert.assertNotNull(jp.get("success"), "Missing 'success' in response");
		Assert.assertNotNull(jp.get("message"), "Missing 'message' in response");
	}

	@Test(priority = 5)
	public void POST_UploadDocumentNeg_3_InvalidCategoryBoundary() throws Throwable {
		ListnerClass.reportLog("Description - Validate invalid category boundary handling <br>Testcase Type - Negative Boundary<br>API Endpoint - /documentservice/");
		StringBuilder invalidCategoryBuilder = new StringBuilder();
		for (int i = 0; i < 130; i++) {
			invalidCategoryBuilder.append('x');
		}
		String invalidCategory = invalidCategoryBuilder.toString();
		Response response = uploadHelper.uploadCaseFile(invalidCategory, "pan_personal", Filepath.pan, case_code, "923809", "CJP");
		Assert.assertTrue(response.statusCode() == 400 || response.statusCode() == 422,
				"Expected validation failure for oversized document_category");
	}

	@Test(priority = 6)
	public void POST_UploadDocumentNeg_Auth_InvalidToken() throws Throwable {
		ListnerClass.reportLog("Description - Validate unauthorized behavior for upload with invalid token <br>Testcase Type - Security<br>API Endpoint - /documentservice/");
		setBaseURI("documents");
		File file = new File(Filepath.pan);
		Response response = RestAssured.given()
				.contentType("multipart/form-data")
				.multiPart("file", file)
				.multiPart("document_category", "kyc")
				.multiPart("case_code", case_code)
				.multiPart("document_type", "pan_personal")
				.multiPart("user_id", "CJP")
				.multiPart("source", "LOS")
				.header("Authorization", "Bearer invalid_token")
				.when().post(EndPoint.DOCUMENTUPLOAD);
		Assert.assertTrue(response.statusCode() == 401 || response.statusCode() == 403,
				"Expected 401/403 for invalid token");
	}
}
