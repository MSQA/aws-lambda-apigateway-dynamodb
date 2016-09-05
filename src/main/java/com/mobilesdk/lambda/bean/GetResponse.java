package com.mobilesdk.lambda.bean;

import java.util.ArrayList;

public class GetResponse extends Response {

	private ArrayList<PersistRequest> parentChildMappingList;

	public ArrayList<PersistRequest> getParentChildMappingList() {
		return parentChildMappingList;
	}

	public void setParentChildMappingList(ArrayList<PersistRequest> parentChildMappingList) {
		this.parentChildMappingList = parentChildMappingList;
	}

}
