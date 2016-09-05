package com.mobilesdk.lambda.bean;

import com.google.gson.Gson;

public class PersistRequest {
	private String parentId;
	private String childId;
	
	public static void main(String[] args) {
		PersistRequest request = new PersistRequest();
		request.setParentId("1");
		request.setChildId("2");
		System.out.println(request);
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String toString() {
		final Gson gson = new Gson();
		return gson.toJson(this);
	}
}
