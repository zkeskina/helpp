package com.helpp.app.models;

public class TokenRequest {

    private String user;
    private String deviceid;

    public TokenRequest(String user, String deviceid) {
        this.user = user;
        this.deviceid = deviceid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
}
