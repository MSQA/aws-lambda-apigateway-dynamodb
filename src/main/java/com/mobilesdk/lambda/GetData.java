package com.mobilesdk.lambda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mobilesdk.lambda.bean.GetRequest;
import com.mobilesdk.lambda.bean.GetResponse;
import com.mobilesdk.lambda.bean.PersistRequest;

public class GetData implements RequestHandler<GetRequest, GetResponse> {

    private DynamoDB dynamoDb;

    private final String DYNAMODB_TABLE_NAME = "ParentChildMap";
    private final Regions REGION = Regions.AP_SOUTHEAST_1;

    public GetResponse handleRequest(final GetRequest request, final Context context) {
        this.initDynamoDbClient();
        GetResponse response = new GetResponse();
        try {
            ArrayList<PersistRequest> parentChildMappingList = this.loadAllData();
            response.setParentChildMappingList(parentChildMappingList);
            response.setStatus("OK");
        } catch (final Exception exception) {
            context.getLogger().log("GetData - " + exception.getMessage());
            response.setStatus("ERROR");
            response.setError(exception.getMessage());
        }

        return response;
    }
    
    private ArrayList<PersistRequest> loadAllData() {
        ArrayList<PersistRequest> parentChildMappingList = new ArrayList<PersistRequest>();
        PersistRequest request = null;
        
        Table table = dynamoDb.getTable(DYNAMODB_TABLE_NAME);
        Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
        expressionAttributeValues.put(":lowerLimit", 0);

        ItemCollection<ScanOutcome> items = table.scan(
            "lb > :lowerLimit", //FilterExpression
            "lb, ub", //ProjectionExpression
            null, //ExpressionAttributeNames - not used in this example 
            expressionAttributeValues);
        
        Iterator<Item> iterator = items.iterator();
        Item item = null;
        while (iterator.hasNext()) {
            item = iterator.next();
            
            request = new PersistRequest();
            request.setParentId(item.getNumber("lb"));
            request.setChildId(item.getNumber("ub"));
            parentChildMappingList.add(request);
        }
        
        return parentChildMappingList;
    }

    private void initDynamoDbClient() {
        final AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }
}