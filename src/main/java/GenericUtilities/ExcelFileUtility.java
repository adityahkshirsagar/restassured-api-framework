package GenericUtilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import org.apache.poi.*;
import org.apache.poi.ss.usermodel.*;

public class ExcelFileUtility {

	DataFormatter df;
	FileInputStream fisexcel;
	Workbook workbook;
	FileOutputStream fisout;

	public static final  String REQUEST_BODY_DATA_SHEET_1 ="project_01";
	Sheet sheet;

	/**
	 * @Description: This method is used to open/load ExcelFile.
	 * @param filepathString
	 */
	public void openExcelFile(String filepathString) {
		try {
			fisexcel = new FileInputStream(filepathString);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			workbook = WorkbookFactory.create(fisexcel);
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Description: This Method is used to close the ExcelFile.
	 */
	public void closeExcelfile() {
		try {
			fisexcel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description: This method used to read the data form Excel
	 * @param sheet
	 * @param row
	 * @param cell
	 * @return
	 */
	public String getExcelData(String sheet, int row, int cell) {
		df = new DataFormatter();

		String exceldata = df.formatCellValue(workbook.getSheet(sheet).getRow(row).getCell(cell));

		return exceldata;

	}

	/**
	 * @Description: This Method will give last Row number
	 * @param sheet
	 * @return
	 */
	public int getLastrownum(String sheet) {

		return workbook.getSheet(sheet).getLastRowNum();
	}

	/**
	 * @Description: This Method will give Last cell number
	 *
	 * @param sheet
	 * @param row
	 * @return
	 */
	public int getLastcellnum(String sheet, int row) {

		return workbook.getSheet(sheet).getRow(row).getLastCellNum();
	}

	/**
	 * @Description: This method is used to write and save the data in Excel
	 * @param filePath
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param Value
	 */

	public void opentheExcelFiletoWriteData(String filePath, String sheet, int row, int cell, String Value) {

		workbook.getSheet(sheet).getRow(row).createCell(cell).setCellValue(Value);
		try {
			fisout = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			workbook.write(fisout);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Description: This Method is used to fetch the Multiple  data to the data provider
	 * @return
	 */
	public String[][] getDataforDataprovider(String Sheet) {

		int lastrow = getLastrownum(Sheet);

		int lastcell = getLastcellnum(Sheet, 0);
		String[][] arr = new String[lastrow][lastcell];

		for (int i = 0; i < lastrow; i++) {

			for (int j = 0; j < lastcell; j++) {
				arr[i][j] = (getExcelData(Sheet, i + 1, j));

			}

		}
		return arr;
	}

	/**
	 * @Description: This method is used to read multiple data from Excel
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */

	public String[][] readMutilpledata(String sheet, String key) {

		String[][] arr = null;
		int lastrow = getLastrownum(sheet);
		System.out.println(lastrow);
		for (int i = 0; i < lastrow; i++) {

			int lastcell = getLastcellnum(sheet, i);
			System.out.println(lastcell);
			arr = new String[lastrow][lastcell];

			for (int j = 0; j < lastcell; j++) {

				String value = getExcelData(sheet, i, j);
				System.out.println(value);
				arr[i][j] = value;
			}
		}

		return arr;
	}

	/**
	 * @Description: read the multiple data from excel based on data provider
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public String[][] getdatafordataprovider() throws EncryptedDocumentException, IOException {

		Sheet sh = workbook.getSheet("Sheet1");
		int lastrow = sh.getLastRowNum();

		int lastcell = sh.getRow(0).getLastCellNum();

		String[][] arr = new String[lastrow][lastcell];

		for (int i = 0; i < lastrow; i++) {

			for (int j = 0; j < lastcell; j++) {

				arr[i][j] = df.formatCellValue(sh.getRow(i + 1).getCell(j));

			}

		}
		return arr;

	}

	/**
	 * Method read data based on testcase id and the column name/test data cell
	 *
	 * @param sheetName
	 * @param columnName
	 * @return Cell String value
	 */
	public String getExcelData(String sheetName, String key, String columnName) {
		try {
			Sheet sheet = workbook.getSheet(sheetName);
			int lastRow = sheet.getLastRowNum();
			int testRow = 0;
			for (int i = 0; i <= lastRow; i++) {
				try {
					String testcaseNum = sheet.getRow(i).getCell(0).getStringCellValue();
					if (testcaseNum.equalsIgnoreCase(key)) {
						testRow = i;
						break;
					}
				} catch (NullPointerException e) {
				}
			}
			int lastCell = sheet.getRow(testRow - 1).getLastCellNum();
			int testcell = 0;
			for (int i = 0; i <= lastCell; i++) {
				try {
					String cellData = sheet.getRow(0).getCell(i).getStringCellValue();
					if (cellData.equalsIgnoreCase(columnName)) {
						testcell = i;
						break;
					}
				} catch (NullPointerException e) {
				}
			}
			return getExcelData(sheetName, testRow, testcell);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "No Data found";
	}

	/**
	 * @description: This method is used to initialize the sheetName
	 * @param SheetName
	 */
	public void initSheet(String sheetName){
		try {
		df = new DataFormatter();
		sheet = workbook.getSheet(sheetName);}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @description: This method will read the data by keeping row name as reference
	 * @param TCID
	 * @param header
	 * @return
	 */
	public String getExcelDataForSingleRow(String TCID, String header) {
		int lastRow = sheet.getLastRowNum();
		String actValue = null;
		for (int i = 0; i <= lastRow; i++) {
			try {
				String testcaseName = df.formatCellValue(sheet.getRow(i).getCell(0));
				if (testcaseName.equalsIgnoreCase(TCID)) {
					boolean flag=false;
					for (int j = 1; j < sheet.getRow(i).getLastCellNum(); j++) {
						String actkey = df.formatCellValue(sheet.getRow(i).getCell(j));
						if(actkey.equals("")){
							flag=true;
							break;
						}
						if (actkey.endsWith("#")){
							actkey=actkey.substring(0,actkey.length()-1);
						}
						if (actkey.startsWith("?")){
							actkey=actkey.substring(1);
						}
						if (actkey.equals(header)) {
							actValue = df.formatCellValue(sheet.getRow(i + 1).getCell(j));
							flag=true;
							break;
						}
					}
					if (flag)break;
				}
			} catch (NullPointerException e) {
			}
		}
		return actValue;

	}
	public void setExcelDataForSingleRow(String TCID, String header, String sheetName,String data) {
		initSheet(sheetName);
		int lastRow = sheet.getLastRowNum();
		for (int i = 0; i <= lastRow; i++) {
			try {
				String testcaseName = df.formatCellValue(sheet.getRow(i).getCell(0));
				if (testcaseName.equalsIgnoreCase(TCID)) {
					for (int j = 1; j < sheet.getRow(i).getLastCellNum(); j++) {
						String actkey = df.formatCellValue(sheet.getRow(i).getCell(j));
						if (actkey.equals("")) {
							try {
								sheet.getRow(i).createCell(j).setCellValue(header);
								try {
									sheet.getRow(i + 1).createCell(j).setCellValue(header);
								} catch (Exception e) {
									sheet.createRow(i + 1).createCell(j).setCellValue(header);
								}
							} catch (Exception e) {
								sheet.createRow(i).createCell(j).setCellValue(header);
								try {
									sheet.getRow(i + 1).createCell(j).setCellValue(header);
								} catch (Exception e1) {
									sheet.createRow(i + 1).createCell(j).setCellValue(header);
								}
							}
							break;
						}
						else if (actkey.equals(header)) {
							try {
								sheet.getRow(i + 1).createCell(j).setCellValue(data);
							} catch (Exception e) {
								sheet.createRow(i + 1).createCell(j).setCellValue(data);
							}
						}

					}
				}
			} catch (NullPointerException e) {
			}
		}
		try {
			workbook.write(new FileOutputStream(Filepath.QAEXCELFILE));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public Map<String, String> getExcelDataForSingleRow(String TCID) {

		int lastRow = sheet.getLastRowNum();
		Map<String, String> map=new HashMap<>();

		for (int i = 0; i <= lastRow; i++) {
			try {
				String testcaseName = df.formatCellValue(sheet.getRow(i).getCell(0));
				if (testcaseName.equalsIgnoreCase(TCID)) {
					for (int j = 1; j < sheet.getRow(i).getLastCellNum(); j++) {
						String actkey = df.formatCellValue(sheet.getRow(i).getCell(j));
						String actValue = df.formatCellValue(sheet.getRow(i + 1).getCell(j));
						if(actkey.equals("")||actValue.equals("")){
							break;
						}
						map.put(actkey,actValue);
					}
				}
			} catch (NullPointerException e) {
			}
		}
		return map;

	}

}


