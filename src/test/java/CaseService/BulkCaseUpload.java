package CaseService;

import java.io.File;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;

import GenericUtilities.BaseClass;
import GenericUtilities.DataBaseUtility;
import GenericUtilities.EndPoint;
import GenericUtilities.Filepath;
import Listner.ListnerClass;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@Listeners(Listner.ListnerClass.class)
public class BulkCaseUpload extends BaseClass {

	String query_case_applicant_detail;
	String query_case_document_ignore;
	String query_case_applicantion;

	@Test()
	public void GetcaseCode() {
		query_case_applicant_detail = DataBaseUtility.getQuery("query_case_applicant_detail", case_code);
		lead_code = DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "customer_code");
        String mobile_no=DataBaseUtility.returnValueByColumnname(query_case_applicant_detail, "mobile_no");
        ListnerClass.reportLog("Description - Get pre created case_code to use as case code <br>Testcase Type - Pre condition<br>case code - "+case_code+"<br>Lead code - "+lead_code+"<br>Mobile No - "+mobile_no);
	}

	/**
	 * This method is used to upload bulk document.
	 * @throws Throwable
	 */

	@Test (dependsOnMethods = "GetcaseCode",priority=1)
	public void DocBulkUpload() throws Throwable
	{
		setBaseURI("documents");

		ListnerClass.reportLog("Description - Upload document in bulk manner for the application <br>Testcase Type - Positive<br>API Endpoint - /documentservice/bulk-upload");

		File filepath_1= new File(Filepath.Aadhar);
		File filepath_2= new File(Filepath.Aadhar);

		Response response = RestAssured.given()
				.contentType("multipart/form-data")
				.multiPart("files[0]",filepath_1)
				.multiPart("files[1]",filepath_2)
				.multiPart("case_code", case_code)
				.multiPart("source", "Email")
				.multiPart("section-type", "normal")
				.multiPart("document_category", "bank-statement")
				.multiPart("document_type", "bank_statement_current_6")
				.header("content-type","multipart/form-data")
				.when().post(EndPoint.DOCUMENTBULKUPLOAD);

		JsonPath jp = response.jsonPath();
		String SuccessMessage = jp.getString("success");
		String RespMessage = jp.getString("message");

        if(response.statusCode()==200)
		{
			ListnerClass.reportLog("Bulk document upload api response : success -" + SuccessMessage);
		}
		else {ListnerClass.reportLogFail("Bulk document upload api failed with response : success - " + RespMessage +"& response code - "+response.statusCode());
	}
	}

}

