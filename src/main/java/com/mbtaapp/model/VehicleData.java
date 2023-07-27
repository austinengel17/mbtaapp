package com.mbtaapp.model;

public class VehicleData {
    String id;
    String currentStatus;
    String stopId;

    String directionId;
    public VehicleData(String id, String currentStatus, String stopId, String directionId){
        this.id = id;
        this.currentStatus = currentStatus;
        this.stopId = stopId;
        this.directionId = directionId;
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

    public String getDirectionId() {
        return directionId;
    }

    public void setDirectionId(String directionId) {
        this.directionId = directionId;
    }
}
