package GenericUtilities;

import com.aventstack.extentreports.ExtentTest;

import PropertyFileConfig.PropertyFileReaders;

public class ThreadLocalClass
{
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> testLevel = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> classLevel = new ThreadLocal<>();
    private static final ThreadLocal<ExcelFileUtility> excelUtil = new ThreadLocal<>();
    private static final ThreadLocal<DataBaseUtility> databaseUtil = new ThreadLocal<>();
    private static final ThreadLocal<PropertyFileReaders> propFile = new ThreadLocal<>();
    private static final ThreadLocal<String> token = new ThreadLocal<>();
    public static  ThreadLocal<PropertyFileReaders>  propfile=new ThreadLocal<>();
    public static ExtentTest getTest() {
        return test.get();
    }

    public static void setTest(ExtentTest sTest) {
        test.set(sTest);
    }

    public static void removeTest() {
        test.remove();
    }

    public static PropertyFileReaders getpropfile() { return propfile.get(); }
    public static ExtentTest getTestLevel() {
        return testLevel.get();
    }

    public static void setTestLevel(ExtentTest sTestLevel) {
        testLevel.set(sTestLevel);
    }

    public static void removeTestLevel() {
        testLevel.remove();
    }
    public static ExtentTest getClassLevel() {
        return classLevel.get();
    }

    public static void setClassLevel(ExtentTest sClassLevel) {
        classLevel.set(sClassLevel);
    }

    public static void removeClassLevel() {
        classLevel.remove();
    }
    public static ExcelFileUtility getExcelUtil() {
        return excelUtil.get();
    }

    public static void setExcelUtil(ExcelFileUtility sExcelUtil) {
        excelUtil.set(sExcelUtil);
    }

    public static void removeExcelUtil() {
        excelUtil.remove();
    }
    public static DataBaseUtility getDatabaseUtil() {
        return databaseUtil.get();
    }

    public static void setDatabaseUtil(DataBaseUtility sDatabaseUtil) {
        databaseUtil.set(sDatabaseUtil);
    }

    public static void removeDatabaseUtil() {
        databaseUtil.remove();
    }
    public static PropertyFileReaders getPropFile() {
        return propFile.get();
    }

    public static void setPropFile(PropertyFileReaders sPropFile) {
        propFile.set(sPropFile);
    }

    public static void removePropFile() {
        propFile.remove();
    }
    public static String getToken() {
        return token.get();
    }

    public static void setToken(String sToken) {
        token.set(sToken);
    }

    public static void removeToken() {
        token.remove();
    }

}

