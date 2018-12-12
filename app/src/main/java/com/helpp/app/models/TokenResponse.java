package com.helpp.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponse {


    @SerializedName("message")
    @Expose
    private String message ;
    @SerializedName("page")
    @Expose
    private String page ;
    @SerializedName("resultcode")
    @Expose
    private String resultcode ;
    @SerializedName("user")
    @Expose
    private String user ;

    public TokenResponse(String message, String page, String resultcode, String user) {
        this.message = message;
        this.page = page;
        this.resultcode = resultcode;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
