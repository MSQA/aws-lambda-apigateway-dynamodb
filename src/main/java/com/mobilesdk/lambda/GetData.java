package com.mobilesdk.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mobilesdk.lambda.bean.GetRequest;
import com.mobilesdk.lambda.bean.GetResponse;
import com.mobilesdk.lambda.bean.PersistRequest;
import com.mobilesdk.lambda.constants.Constants;

public class GetData implements RequestHandler<GetRequest, GetResponse> {

	private AmazonDynamoDBClient client;

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

		ScanRequest scanRequest = new ScanRequest();
		scanRequest.withTableName(Constants.DYNAMODB_TABLE_NAME);
		ScanResult scan = client.scan(scanRequest);
		System.out.println("scan: " + scan.getCount());
		List<Map<String, AttributeValue>> items = scan.getItems();
		for (Map<String, AttributeValue> item : items) {
			request = new PersistRequest();
			request.setParentId(item.get("lb").getS());
			request.setChildId(item.get("ub").getS());
			parentChildMappingList.add(request);
		}

		return parentChildMappingList;
	}

	private void initDynamoDbClient() {
		client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
	}
}
