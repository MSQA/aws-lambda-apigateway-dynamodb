package com.mobilesdk.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mobilesdk.lambda.bean.GetRequest;
import com.mobilesdk.lambda.bean.GetResponse;
import com.mobilesdk.lambda.bean.PersistRequest;

public class GetData extends Base implements RequestHandler<GetRequest, GetResponse> {

	public GetResponse handleRequest(final GetRequest request, final Context context) {
		this.context = context;
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
		scanRequest.withTableName(DYNAMODB_TABLE_NAME);
		ScanResult scan = client.scan(scanRequest);
		context.getLogger().log("scan: " + scan.getCount());
		List<Map<String, AttributeValue>> items = scan.getItems();
		for (Map<String, AttributeValue> item : items) {
			request = new PersistRequest();
			request.setParentId(item.get("lb").getS());
			request.setChildId(item.get("ub").getS());
			parentChildMappingList.add(request);
		}

		return parentChildMappingList;
	}
}