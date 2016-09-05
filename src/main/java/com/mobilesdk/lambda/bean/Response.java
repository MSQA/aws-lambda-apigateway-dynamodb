package com.mobilesdk.lambda.bean;

import com.google.gson.Gson;

public class Response {
	private String status;
	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Response withError(String error) {
		this.error = error;

		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Response withStatus(String status) {
		this.status = status;

		return this;
	}

	public String toString() {
		final Gson gson = new Gson();
		return gson.toJson(this);
	}
}
