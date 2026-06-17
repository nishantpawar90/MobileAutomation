package com.enterprise.mobile.data;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.enterprise.mobile.config.ConfigManager;
import com.enterprise.mobile.exceptions.TestDataException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * MongoDB client for test data validation.
 * Supports querying test data and verifying backend state.
 */
public final class MongoDBClient {

    private static final Logger logger = LogManager.getLogger(MongoDBClient.class);
    private static volatile MongoDBClient instance;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private MongoDBClient() {
        ConfigManager config = ConfigManager.getInstance();
        String connectionString = config.getMongoConnectionString();
        String databaseName = config.getMongoDatabase();

        try {
            this.mongoClient = MongoClients.create(connectionString);
            this.database = mongoClient.getDatabase(databaseName);
            logger.info("MongoDB connection established to database: {}", databaseName);
        } catch (Exception e) {
            throw new TestDataException("Failed to connect to MongoDB", e);
        }
    }

    public static MongoDBClient getInstance() {
        if (instance == null) {
            synchronized (MongoDBClient.class) {
                if (instance == null) {
                    instance = new MongoDBClient();
                }
            }
        }
        return instance;
    }

    /**
     * Finds a single document by field value.
     */
    public Document findOne(String collectionName, String field, Object value) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document result = collection.find(Filters.eq(field, value)).first();
        logger.debug("MongoDB query: {}.find({}={})", collectionName, field, value);
        return result;
    }

    /**
     * Finds all documents matching a filter.
     */
    public List<Document> findAll(String collectionName, String field, Object value) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.find(Filters.eq(field, value)).into(new java.util.ArrayList<>());
    }

    /**
     * Counts documents matching a filter.
     */
    public long count(String collectionName, String field, Object value) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.countDocuments(Filters.eq(field, value));
    }

    /**
     * Inserts a document (for test setup).
     */
    public void insertOne(String collectionName, Document document) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.insertOne(document);
        logger.debug("Inserted document into collection: {}", collectionName);
    }

    /**
     * Deletes documents matching a filter (for test cleanup).
     */
    public long deleteMany(String collectionName, String field, Object value) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        long deletedCount = collection.deleteMany(Filters.eq(field, value)).getDeletedCount();
        logger.debug("Deleted {} documents from {}", deletedCount, collectionName);
        return deletedCount;
    }

    /**
     * Checks if a document exists.
     */
    public boolean exists(String collectionName, String field, Object value) {
        return findOne(collectionName, field, value) != null;
    }

    /**
     * Closes the MongoDB connection.
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.info("MongoDB connection closed");
        }
    }

    public static void reset() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }
}
