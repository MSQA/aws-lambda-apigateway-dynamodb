package com.mobilesdk.lambda;

import org.joda.time.DateTime;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mobilesdk.lambda.bean.PersistRequest;
import com.mobilesdk.lambda.bean.PersistResponse;
import com.mobilesdk.lambda.constants.Constants;

public class PersistData implements RequestHandler<PersistRequest, PersistResponse>{
	private DynamoDB dynamoDb;
	
	public PersistResponse handleRequest(final PersistRequest request, final Context context) {
		context.getLogger().log("Request: " + request);

		this.initDynamoDbClient();

		try {
			this.persistData(request.getParentId(), request.getChildId());
		} catch (final ConditionalCheckFailedException conditionalCheckFailedException) {
			context.getLogger().log("PersistRequest - Duplicate Entry Received. PersistRequest JSON RAW PayLoad: " + request);

			throw conditionalCheckFailedException;
		}

		return (PersistResponse) new PersistResponse().withStatus("OK");
	}

	private PutItemOutcome persistData(final String parentId, final String childId) throws ConditionalCheckFailedException {

		return this.dynamoDb
				.getTable(Constants.DYNAMODB_TABLE_NAME)
				.putItem(new PutItemSpec()
						.withItem(new Item().withString("lb", parentId)
								.withString("ub", childId)
								.withString("creationDateTime", new DateTime().toString())));
	}

	private void initDynamoDbClient() {
		final AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
		this.dynamoDb = new DynamoDB(client);
	}
}