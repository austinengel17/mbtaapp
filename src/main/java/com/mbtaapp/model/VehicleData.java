package com.mbtaapp.model;

public class VehicleData {
    String id;
    String currentStatus;
    String stopId;
    public VehicleData(String id, String currentStatus, String stopId){
        this.id = id;
        this.currentStatus = currentStatus;
        this.stopId = stopId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }
}
