package com.mobilesdk.lambda.bean;

import com.google.gson.Gson;

public class GetRequest {
    public String toString() {
        final Gson gson = new Gson();
        return gson.toJson(this);
    }
}
