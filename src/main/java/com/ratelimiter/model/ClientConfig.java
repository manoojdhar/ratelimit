package com.ratelimiter.model;

public class ClientConfig {

    private  String clientId;
    private int limitPerMinute;

    public ClientConfig() {}

    public ClientConfig(String clientId, int limitPerMinute) {
        this.clientId = clientId;
        this.limitPerMinute = limitPerMinute;
    }

    public String getClientId() {
        return clientId;
    }

    public  void  setClientId(String clientId) {
        this.clientId = clientId;
    }

    public  int getLimitPerMinute() {
        return limitPerMinute;
    }

    public void setLimitPerMinute(int limitPerMinute) {
        this.limitPerMinute = limitPerMinute;
    }
}
