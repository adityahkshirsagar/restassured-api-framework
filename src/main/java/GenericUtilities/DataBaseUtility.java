package GenericUtilities;

import org.bson.Document;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import PropertyFileConfig.PropertyFileReaders;
import org.testng.Reporter;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 *  This Class Contains all the generic methods related to database
 *
 */
public class DataBaseUtility
{

static ResultSet Result;
static Statement stmt;
static Connection Con;

	    public static void connectToDatabase() throws Throwable {

	    	try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            DriverManager.setLoginTimeout(10);

	            String username = System.getenv("username");
	            String password = System.getenv("password");
	            String url = System.getenv("url");
	            Con = DriverManager.getConnection(url, username, password);

	            stmt = Con.createStatement();
	            System.out.println("DB Connection Successfull");
	            Reporter.log(" DB Connection Successfull");
	        }
	        catch (Exception e){
	            e.printStackTrace();
	            throw new Exception("DataBase Connection was not successfull");
	        }
	    }

	public static MongoCollection<Document> connectMongoBD(String DBname,String collectionName)
	{
        String connectionString = "mongodb+srv://example.invalid";
        try {
            MongoClient mongoClient = MongoClients.create(connectionString);
            MongoDatabase database = mongoClient.getDatabase(DBname);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            System.out.println("Successfully connected to MongoDB collection: " + collectionName);
            return collection;
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

	}

	/**
	 * This method will   Execute the  Query & return the result set
	 * @param Query
	 * @throws Throwable
	 */
	public static String  ExecuteQuery(String Query) throws Throwable
	{

		try {
            if (Con == null || Con.isClosed()) {
            }
		ResultSet result= stmt.executeQuery(Query);
		String data=null;
		try {
			if(result.next()) {
			data = result.getString(1);
			}
		}catch (Exception e) {

			e.printStackTrace();
		}
		return data;
		}
		catch(SQLException e)
		{
			 System.out.println("Database error: " + e.getMessage());
	            Con = null;
	            return null;
		}

	}

	/**
	 * @Description : This method is used to execute query and validate data in database
	 */

	/**
	 * @Description: This method is used to execute the query and return the result as true/false
	 *
	 * @param Query
	 * @param data
	 * @param index
	 * @return result
	 * @throws SQLException
	 */
	public boolean verifyData(String Query,String data,int index)
	{

		boolean flag=false;

		try {
			ResultSet result = stmt.executeQuery(Query);
			while (result.next()) {
				String actualData = result.getString(index);

				if (actualData.equals(data)) {
					flag = true;
					break;
				}
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * This method will   Execute the  Query
	 * @param Query
	 */
	public void executeQuerytoread(String Query)
	{
		try {
			Result = stmt.executeQuery(Query);
		}catch (Exception e) {
		}
	}

	/**
	 * This method will   Execute the  Query & return the result set
	 * @param Query
	 * @throws Throwable
	 */
	public ResultSet executeQuery(String query) throws Throwable {
	    ResultSet result = null;

	    try {
	        if (Con == null || Con.isClosed()) {
	        }

	        try {
	            result = stmt.executeQuery(query);
	        } catch (SQLException e) {
	            System.out.println("SQL error: " + e.getMessage());
	            throw e;
	        }
	    } catch (SQLException e) {
	        System.out.println("Database error: " + e.getMessage());
	        Con = null;
	        throw e;
	    } catch (Exception e) {
	        System.out.println("An unexpected error occurred: " + e.getMessage());
	        throw e;
	    }

	    return result;
	}

	/**
	 * This method will   Execute the update Query
	 * @param Query
	 * @throws Throwable
	 */

	public static long executeUpdateQuery(String Query)
	{
		long row=0;
		try {
			row = stmt.executeLargeUpdate(Query);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return row;
	}

	/**
	 * This method will   Execute the  Query & return the result set
	 * @param resultSet
	 *  @param ColumnName
	 */
	public String fetchDataBycolumn(ResultSet resultSet,String ColumnName)  {
		String name = null;
		try {

			while (resultSet.next()) {
				name = resultSet.getString(ColumnName);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return name;

	}

	public static String returnValueByColumnname(String Query,String ColumnName)
	{
		String name = null;
		try {
			ResultSet resultSet = stmt.executeQuery(Query);

			while (resultSet.next()) {
				name = resultSet.getString(ColumnName);
				break;
			}
		}catch (Exception e) {
		}
		return name;

	}

	public static  String getTableName(String query)
	{
		String[] arr = query.replaceAll("( )+"," ").split(" ");
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equalsIgnoreCase("from")){
				return arr[i+1];
			}
		}
		return "";
	}

	public static String getQuery(String propertyFileKey, String... whereCondition){
		String query = String.format(
				PropertyFileReaders.getPropertiesData(propertyFileKey),
				(Object[]) whereCondition);
		return query;
	}

	public static String getQueryfile(String propertyFileKey) {
        return PropertyFileReaders.getPropertiesData(propertyFileKey);
    }

	/**
	 * This Method will give data based on the coloumn name
	 * @param coloumnName
	 */
	public void readDatafromDB(String coloumnName)
	{
		try {
			while(Result.next())
			{
				System.out.println(Result.getString(coloumnName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This Method is used to close the Con and Con
	 * @throws SQLException
	 */
	public static void closedatabase() throws SQLException

	{
		 try {
			 	if (Con != null && !Con.isClosed()) {
			 		Con.close();
			 	}
	            Reporter.log("DB Connection closed successfully");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	}

	/**
	 * This Method is used to Check the Con status
	 */

	public boolean isDbConnected() {
		boolean flag=false;
		try {
			if (Con != null && !Con.isClosed())
				flag= true;
			else
				flag= false;
		} catch (Exception e) {
			flag=false;
		}
		return flag;

	}

}


