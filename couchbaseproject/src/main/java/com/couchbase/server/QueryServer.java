package com.couchbase.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;

public class QueryServer {

	private static String userName;
	private static String password;
	private static String bucketName;
	private static String serverUrl;
	
	//Reading from the server.properties file to get server details
    static {
    	try {
    		InputStream input = new FileInputStream("src/main/resources/server.properties");
            
    		Properties prop = new Properties();
            prop.load(input);
            
            serverUrl = prop.getProperty("serverurl");
            
            userName = prop.getProperty("username");
            
            password = prop.getProperty("password");
            
            bucketName= prop.getProperty("bucketname");          
           
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
  //Creating the connection and returning the bucket
    public static Bucket getBucket()
    {	
    	Cluster cluster = CouchbaseCluster.create(serverUrl);
        
    	cluster.authenticate(userName,password);
        
    	Bucket bucket = cluster.openBucket(bucketName);
    	
    	return bucket;
    }
  
}
