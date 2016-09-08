package com.mobilesdk.lambda;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.Context;

public abstract class Base {
	protected AmazonDynamoDBClient client;
	protected DynamoDB dynamoDb;
	protected Context context;
	
	protected final static String DYNAMODB_TABLE_NAME = "ParentChildMap"; 
	protected final static Regions REGION = Regions.US_WEST_2;
	
	protected void initDynamoDbClient() {
		this.client = new AmazonDynamoDBClient();
		this.client.setRegion(Region.getRegion(REGION));
		this.dynamoDb = new DynamoDB(client);
	}
}
