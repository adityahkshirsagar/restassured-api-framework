package GenericUtilities;
import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

public class MongoDBUtility {

	static	String Mongo_username="<REDACTED_USERNAME>";
	static  String Mongo_password="<REDACTED_PASSWORD>";
	 private static final String CONNECTION_STRING =
			 "mongodb+srv://example.invalid";
	 private static final String CONNECTION_STRING1 =
			 "mongodb+srv://example.invalid";

		    /**
		     * Establishes a connection to the MongoDB server.
		     *
		     * @return the MongoClient object
		     */
		    public static MongoClient connectToMongoDB() {
		        try {
		            MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
		            System.out.println("Successfully connected to MongoDB server.");
		            return mongoClient;
		        } catch (Exception e) {
		            System.err.println("Error connecting to MongoDB: " + e.getMessage());
		            e.printStackTrace();
		            return null;
		        }
		    }

		    public static MongoClient connectToMongoDB1() {
		        try {
		            MongoClient mongoClient = MongoClients.create(CONNECTION_STRING1);
		            System.out.println("Successfully connected to MongoDB server.");
		            return mongoClient;
		        } catch (Exception e) {
		            System.err.println("Error connecting to MongoDB: " + e.getMessage());
		            e.printStackTrace();
		            return null;
		        }
		    }

		    /**
		     * Retrieves a collection from the specified database.
		     *
		     * @param mongoClient the MongoClient object
		     * @param dbName the name of the database
		     * @param collectionName the name of the collection
		     * @return the MongoCollection object
		     */
		    public static MongoCollection<Document> getCollection(MongoClient mongoClient, String dbName, String collectionName) {
		        try {
		            MongoDatabase database = mongoClient.getDatabase(dbName);
		            MongoCollection<Document> collection = database.getCollection(collectionName);
		            System.out.println("Successfully retrieved MongoDB collection: " + collectionName);
		            return collection;
		        } catch (Exception e) {
		            System.err.println("Error retrieving MongoDB collection: " + e.getMessage());
		            e.printStackTrace();
		            return null;
		        }
		    }

		    /**
		     * Updates a document in the specified collection based on a filter and update query.
		     *
		     * @param collection the MongoCollection to perform the update on
		     * @param filter the filter to identify the document(s) to update
		     * @param update the update operation to apply
		     * @return the result of the update operation
		     */
		    public static UpdateResult updateDocument(MongoCollection<Document> collection, Document filter, Document update) {
		        try {
		            UpdateResult result = collection.updateOne(filter, update);
		            System.out.println("Successfully updated document(s): " + result.getModifiedCount());
		            return result;
		        } catch (Exception e) {
		            System.err.println("Error updating document: " + e.getMessage());
		            e.printStackTrace();
		            return null;
		        }
		    }

		    /**
		     * Updates multiple documents in the specified collection based on a filter and update query.
		     *
		     * @param collection the MongoCollection to perform the update on
		     * @param filter the filter to identify the document(s) to update
		     * @param update the update operation to apply
		     * @return the result of the update operation
		     */
		    public static UpdateResult updateMultipleDocuments(MongoCollection<Document> collection, Document filter, Document update) {
		        try {
		            UpdateResult result = collection.updateMany(filter, update);
		            System.out.println("Successfully updated multiple document(s): " + result.getModifiedCount());
		            return result;
		        } catch (Exception e) {
		            System.err.println("Error updating documents: " + e.getMessage());
		            e.printStackTrace();
		            return null;
		        }
		    }

}


