package PropertyFileConfig;

import java.io.FileInputStream;
import java.util.Properties;
import GenericUtilities.Filepath;

public  class PropertyFileReaders implements ConfigReaders{

	String filepath;
     Properties prop;

     public String getEnvPropertiesData(String env,String key) {
         try {
             FileInputStream file = new FileInputStream(env);
             Properties properties = new Properties();
             properties.load(file);
             return properties.getProperty(key);
         } catch (Exception e) {
             e.printStackTrace();
         }
         return "No Such Key in property file: " + key;
     }

     @Override
     public String getDBUSERNAME() throws Throwable
     {
         filepath=Filepath.QAPropertiesPath;
          FileInputStream file = new FileInputStream(filepath);
          Properties prop = new Properties();
          prop.load(file);
        return prop.getProperty("QA_DBUSERNAME");
    }

     @Override
     public String getDBPASSWORD() throws Throwable
    {
         filepath=Filepath.QAPropertiesPath;
           FileInputStream file = new FileInputStream(filepath);
           Properties prop = new Properties();
           prop.load(file);
        return prop.getProperty("QA_DBPASSWORD");
    }

     @Override
     public String getQADBURL() throws Throwable
     {
         filepath=Filepath.QAPropertiesPath;
           FileInputStream file = new FileInputStream(filepath);
           Properties prop = new Properties();
           prop.load(file);
        return prop.getProperty("QA_DBURL");
    }

     public String getQuery(String key) throws Throwable
     {
          return prop.getProperty(key);
     }

     /**
       * @Description: This method return the value associated with key in property file and all the key value are defined under folder Test data
       * with file name DbQuery.properties
       * @param key
       * @return value
       */

      public static String getPropertiesData(String key) {

          try {
              FileInputStream file = new FileInputStream(Filepath.DBQueryfilePath);
              Properties properties = new Properties();
              properties.load(file);
              return properties.getProperty(key);
          } catch (Exception e) {
              e.printStackTrace();
          }
          return "No Such Key in property file: " + key;
      }

          public String getName(String fullName) throws Throwable {

              FileInputStream file = new FileInputStream(Filepath.QAPropertiesPath);
              Properties properties = new Properties();
              properties.load(file);
                   return properties.getProperty(fullName);
        }

        @Override
          public String getKey(String key) throws Throwable {

              filepath=Filepath.QAPropertiesPath;
                FileInputStream file = new FileInputStream(filepath);
                Properties prop = new Properties();
                prop.load(file);
              return prop.getProperty(key);

          }
		}


