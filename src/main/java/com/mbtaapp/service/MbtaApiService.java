package com.mbtaapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbtaapp.model.VehicleData;
import com.mbtaapp.model.VehicleSseObject;
import com.mbtaapp.tools.PropertiesReader;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class MbtaApiService {
    private static final String API_URL = "https://api-v3.mbta.com";
    private static final String FILTER_BY = "&filter[%s]=%s";
    private static final String API_KEY = PropertiesReader.getProperty("MBTA_API_KEY"); //change to be in property file

    public MbtaApiService() {
    }

    public Mono<Map<JsonNode,Map>> getStopsByLine(String line){
        WebClient client = WebClient.create();
        return client.get()
                .uri(API_URL + "/stops?filter[route]=" + line)
                .header("x-api-key","d546460f66f44da7b3b05278c30d3769")
                .retrieve()
                .bodyToMono(JsonNode.class).map(jsonNode -> jsonNode.get("data")).map(jsonArr -> {
                    Map<JsonNode, Map> stops = new LinkedHashMap<>();
                    for (JsonNode node : jsonArr) {
                        Map<String, JsonNode> stop = new HashMap<>();
                        stop.put("name", node.get("attributes").get("name"));
                        stop.put("latitude", node.get("attributes").get("latitude"));
                        stop.put("longitude", node.get("attributes").get("longitude"));
                        stops.put(node.get("id"), stop);
                    }
                    return stops;
                })
                .onErrorResume(throwable -> {
                    // Handle WebClient-related exceptions
                    throwable.printStackTrace();
                    return Mono.error(throwable);
                });
    }
    public Mono<Map<String, String>> getChildParentStopRelation(){
        WebClient client = WebClient.create();
        return client.get()
                .uri(API_URL + "/stops?include=child_stops&filter[route_type]=0,1")
                .header("x-api-key",API_KEY)
                .retrieve()
                .bodyToMono(JsonNode.class).map(data -> {
                    Map<String, String> childParentStopRelation = new HashMap<>();
                    for(JsonNode node : data.get("data")){
                        String id = node.get("id").asText();
                        String parentId = node.get("relationships").get("parent_station").get("data").get("id").asText();
                        childParentStopRelation.put(id, parentId);
                    }
                    return childParentStopRelation;
                })
                .onErrorResume(throwable -> {
                    // Handle WebClient-related exceptions
                    throwable.printStackTrace();
                    return Mono.error(throwable);
                });
    }
    public Flux<VehicleSseObject> subscribeVehiclesSse(Map<String, String> params){
        ArrayList<String> urlPath = new ArrayList<>();
        urlPath.add("vehicles");
        String reqUrl = reqUrlBuilder(urlPath, params);
        System.out.println(reqUrl);
        WebClient client = WebClient.create();
        return client.get()
                .uri(API_URL + reqUrl)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .header("x-api-key",API_KEY)
                .retrieve()
                .bodyToFlux(ServerSentEvent.class)
                .map(event -> {
                    System.out.println(API_URL + reqUrl);
                    VehicleSseObject vehicleSseObject = new VehicleSseObject();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode json;
                    vehicleSseObject.setEvent(event.event());
                    ArrayList vehicles = vehicleSseObject.getVehicleData();
                    try {
                        String jsonString = mapper.writeValueAsString(event.data());
                        json = mapper.readTree(jsonString);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    if(event.event().equals("reset"))
                        for(JsonNode node : json){
                            VehicleData vehicleData = vehicleDataAsObject(node);
                            vehicles.add(vehicleData);
                        }
                    if(event.event().equals("update") || event.event().equals("add")){
                        if(event.event().equals("add")) System.out.println("added vehicle: " + vehicleDataAsObject(json).getId());
                        vehicles.add(vehicleDataAsObject(json));
                    }
                    if(event.event().equals("remove")){
                        System.out.println("REMOVAL OF : " + json);
                        vehicles.add(vehicleDataAsObject(json));
                    }
                    return vehicleSseObject;
                })
                .onErrorResume(throwable -> {
                    // Handle WebClient-related exceptions
                    throwable.printStackTrace();
                    return Flux.error(throwable);
                });
    }


    private String reqUrlBuilder(ArrayList<String> urlPath, Map<String, String> params){
        String path = "";
        for(String s : urlPath){
            path += ("/" + s);
        }
        if(!params.isEmpty()) {
            path += "?";
            for (Map.Entry<String, String> mapElement : params.entrySet()) {
                path += String.format(FILTER_BY, mapElement.getKey(), mapElement.getValue());
            }
        }
        return path;
    }

    private VehicleData vehicleDataAsObject(JsonNode vehicleDataNode){ //make into mapper class
        String id = "";
        String currentStatus = "";
        String stopId = "";
        String directionId = "";
        try {
            System.out.println(vehicleDataNode);
            id = vehicleDataNode.get("id").asText();
            if(vehicleDataNode.get("attributes") != null) {
                currentStatus = vehicleDataNode.get("attributes").get("current_status").asText();
                directionId = vehicleDataNode.get("attributes").get("direction_id").asText();
                stopId = vehicleDataNode.get("relationships").get("stop").get("data").get("id").asText();
            }
        } catch (Exception e){
            System.out.println("ERROR mapping vehicle data as object " + vehicleDataNode);
            throw e;
        }
        finally{
            return new VehicleData(id, currentStatus, stopId, directionId);
        }
    }
}
