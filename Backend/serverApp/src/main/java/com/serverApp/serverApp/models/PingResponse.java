package com.serverApp.serverApp.models;

public class PingResponse {

    public PingResponse(String id){
        this.id = id;
    }

    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
