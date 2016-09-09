package com.mobilesdk.lambda;

import java.math.BigDecimal;

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

public class PersistData implements RequestHandler<PersistRequest, PersistResponse> {
    
    private DynamoDB dynamoDb;
    
    private final String DYNAMODB_TABLE_NAME = "ParentChildMap";
    private final Regions REGION = Regions.AP_SOUTHEAST_1;
    
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

    private PutItemOutcome persistData(final BigDecimal parentId, final BigDecimal childId) throws ConditionalCheckFailedException {
        return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME).putItem(new PutItemSpec().withItem(new Item().withNumber("lb", parentId).withNumber("ub", childId).withString("creationDateTime", new DateTime().toString())));
    }
    
    private void initDynamoDbClient() {
        final AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }
}