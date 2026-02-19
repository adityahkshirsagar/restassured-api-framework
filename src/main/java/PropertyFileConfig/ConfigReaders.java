package PropertyFileConfig;

public interface ConfigReaders 
{

	String getQADBURL() throws Throwable;
    String getDBUSERNAME() throws Throwable;
    String getDBPASSWORD() throws Throwable;
    String getQuery(String key) throws Throwable;
    String getName(String fullname) throws Throwable;
    String getKey(String key) throws Throwable;

	
	
	
	
	
	

	

	

	
	
	
	
	
}
