package com.mobilesdk.lambda.bean;

import java.math.BigDecimal;

import com.google.gson.Gson;

public class PersistRequest {
    private BigDecimal parentId;
    private BigDecimal childId;

    public static void main(String[] args) {
        PersistRequest request = new PersistRequest();
        request.setParentId(new BigDecimal(1));
        request.setChildId(new BigDecimal(2));
        System.out.println(request);
    }

    public BigDecimal getParentId() {
        return parentId;
    }

    public void setParentId(BigDecimal parentId) {
        this.parentId = parentId;
    }

    public BigDecimal getChildId() {
        return childId;
    }

    public void setChildId(BigDecimal childId) {
        this.childId = childId;
    }

    public String toString() {
        final Gson gson = new Gson();
        return gson.toJson(this);
    }
}
