package WorkFlowLibrary;
import static io.restassured.RestAssured.given;

import java.io.File;
import org.testng.annotations.Test;
import GenericUtilities.BaseClass;
import GenericUtilities.DataBaseUtility;
import GenericUtilities.EndPoint;
import GenericUtilities.Filepath;
import PropertyFileConfig.ObjectReaders;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class UploadHelper extends BaseClass{

	   public String documentWorkFlow(String filePath)

	    {

		 File file= new File(System.getProperty("user.dir")+filePath);
		 String path=file.getAbsolutePath();
		 return path;

	    }

		public ValidatableResponse upload_BankStatement(String casecode, String document_category, String document_type , String file_path,String name) throws Throwable
		{
			setBaseURI("casefiles");

			String token="Bearer <REDACTED_TOKEN>";
			String query3="update case_applicant_detail set name='"+name+"' where case_code='"+casecode+"'";
			DataBaseUtility.executeUpdateQuery(query3);

			String b_name="update case_business_detail set business_name='"+name+"' where case_code='"+casecode+"'";
			DataBaseUtility.executeUpdateQuery(b_name);

			String status="UPDATE  case_application SET application_status  = 'IP_FRESH_REGISTRATION'  WHERE  code = '"+casecode+"'";
	        DataBaseUtility.executeUpdateQuery(status);

		     File  file= new File(file_path);
			ValidatableResponse response = given().contentType("multipart/form-data").multiPart("file",file)
					.multiPart("document_category", document_category)
					.multiPart("case_code", casecode)
					.multiPart("document_type", document_type)
					.multiPart("source","LOS")
					.header("Authorization", token)
					.when().post(EndPoint.CASEFILEUPLOAD).then().log().all();
			return response;

		}

		public ValidatableResponse upload_BankStatement_password(String casecode, String document_category, String document_type , String file_path,String name,String password) throws Throwable
		{
			setBaseURI("casefiles");

			String token="Bearer <REDACTED_TOKEN>";
			String query3="update case_applicant_detail set name='"+name+"' where case_code='"+casecode+"'";
			DataBaseUtility.executeUpdateQuery(query3);

			String b_name="update case_business_detail set business_name='"+name+"' where case_code='"+casecode+"'";
			DataBaseUtility.executeUpdateQuery(b_name);

			String status="UPDATE  case_application SET application_status  = 'IP_FRESH_REGISTRATION'  WHERE  code = '"+casecode+"'";
	        DataBaseUtility.executeUpdateQuery(status);

		     File  file= new File(file_path);
			ValidatableResponse response = given().contentType("multipart/form-data").multiPart("file",file)
					.multiPart("document_category", document_category)
					.multiPart("case_code", casecode)
					.multiPart("document_type", document_type)
					.multiPart("source","LOS")
					.multiPart("metadata","{\"password\":\""+password+"\"}")
					.header("Authorization", token)
					.when().post(EndPoint.CASEFILEUPLOAD).then().log().all();
			return response;

		}

		public Response uploadCaseFile(String documentCategory, String documentType, String file_path, String casecode,String name,String userId) throws Throwable{

			setBaseURI("casefiles");
			File  file= new File(file_path);

	        Response response = given().contentType("multipart/form-data")
	        		.multiPart("file",file)
	                .multiPart("document_category", documentCategory)
	                .multiPart("case_code", casecode)
	                .multiPart("document_type", documentType)
	                .multiPart("user_id",userId)
	                .multiPart("source","LOS")
	                .multiPart("json", "{\"key\":\"value\", \"metadata\":\"" + documentType + "\"}")
	                .when().post(EndPoint.CASEFILEUPLOAD);
	        return response;
	    }
}


