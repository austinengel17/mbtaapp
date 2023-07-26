package com.mbtaapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VehicleSseObject {

    String event;
    ArrayList<VehicleData> vehicleData;

    public VehicleSseObject(){
        vehicleData = new ArrayList<>();
    }
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public ArrayList<VehicleData> getVehicleData() {
        return vehicleData;
    }


}
