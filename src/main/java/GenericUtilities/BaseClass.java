package GenericUtilities;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import PropertyFileConfig.ObjectReaders;
import io.restassured.RestAssured;

/**
 * This class consists of all basic configuration annotations of testNG
 * to perform common functionalities.
 */

public class BaseClass {

	public DataBaseUtility databaseutil = new DataBaseUtility();
	public ExcelFileUtility excelUtil = new ExcelFileUtility();
	public JavaUtility javaUtil = new JavaUtility();

	public static String case_code;
	public static String lead_code;

	/**
	 * This method is used to made a connection with DataBase & generate token .
	 * @throws Throwable
	 */
	@BeforeSuite
	public void ConnectToDatabase() throws Throwable {
		ThreadLocalClass.setExcelUtil(excelUtil);
		excelUtil.openExcelFile(Filepath.QAEXCELFILE);
		DataBaseUtility.connectToDatabase();
		ThreadLocalClass.setDatabaseUtil(databaseutil);
	}

	public void setBaseURI(String service) throws Throwable {
		switch (service) {
		case "kyc":
			RestAssured.baseURI = ObjectReaders.readers.getKey("host_kyc");
			break;
		case "casefiles":
			RestAssured.baseURI = ObjectReaders.readers.getKey("host_casefile");
			break;
		default:
			throw new IllegalArgumentException("Invalid service type");
		}
	}
	/**
	 * This method is used to close data base connection.
	 * @throws Throwable
	 */
	@AfterSuite
	public void Disconnetdatabase() throws Throwable {
		DataBaseUtility.closedatabase();
	}
}


