package com.mbtaapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mbtaapp.model.VehicleSseObject;
import com.mbtaapp.service.MbtaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequestMapping("mbta/v1/livemap")
@Controller
public class MbtaLiveMapController {
    private MbtaApiService service;

    @Autowired
    MbtaLiveMapController(MbtaApiService service){this.service = service;}

    @GetMapping(path="/stops/line/{line}")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public Mono<Map<JsonNode, Map>> getStopsByLine(@PathVariable("line") String line) {
        System.out.println("request");
        return service.getStopsByLine(line);
    }

    //Returns map of <child_stop.id, parent_station.id>
    @GetMapping(path="/stops/child-parent-relation")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public Mono<Map<String, String>> getChildParentStopRelation() {
        return service.getChildParentStopRelation();
    }

    @GetMapping(path="/vehicle/location/{line}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    public Flux<VehicleSseObject> vehicleLocationSubscription(@PathVariable("line") String line) {
        return service.subscribeVehiclesSse(Map.of(
				"route_type","0,1",
				"route", ""+line
		));
    }

    @GetMapping(path="/hi")
    @ResponseBody
    public String getLineData(Model model) {
        return "hi";
    }
}
