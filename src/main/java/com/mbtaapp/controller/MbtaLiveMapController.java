package com.mbtaapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mbtaapp.model.VehicleSseObject;
import com.mbtaapp.service.MbtaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping("mbta/v1/livemap")
@Controller
public class MbtaLiveMapController {
    private MbtaApiService service;
    private final Map<String, Flux<VehicleSseObject>> sseFluxMap;
    private final Map<String, AtomicInteger> subscriberMap;
    @Autowired
    MbtaLiveMapController(MbtaApiService service){
        this.service = service;
        this.sseFluxMap = new ConcurrentHashMap<>();
        this.subscriberMap = new ConcurrentHashMap<>();
    }

    @GetMapping(path="/stops/line/{line}")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public Mono<Map<JsonNode, Map>> getStopsByLine(@PathVariable("line") String line) {
        System.out.println("request");
        try {
            return service.getStopsByLine(line);

        } catch (Exception e) {
                // Handle service-related exceptions
                e.printStackTrace();
                return Mono.error(e);
        }
    }

    //Returns map of <child_stop.id, parent_station.id>
    @GetMapping(path="/stops/child-parent-relation")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public Mono<Map<String, String>> getChildParentStopRelation() {
        try {
            return service.getChildParentStopRelation();
        } catch (Exception e) {
            // Handle service-related exceptions
            e.printStackTrace();
            return Mono.error(e);
        }
    }

    @GetMapping(path="/vehicle/location/{line}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = {"http://localhost:3000", "https://www.austinengel.com"})
    @ResponseBody
    public Flux<VehicleSseObject> vehicleLocationSubscription(@PathVariable("line") String line) {
        System.out.println(sseFluxMap.size());
        AtomicInteger subscriberCount = subscriberMap.computeIfAbsent(line, key->new AtomicInteger(0));
        subscriberCount.incrementAndGet();
        Flux<VehicleSseObject> flux = null;
        try {
            flux = sseFluxMap.computeIfAbsent(line, key -> service.subscribeVehiclesSse(Map.of(
                    "route_type", "0,1",
                    "route", "" + line
            )))
            .doFinally((signalType) -> {
                if (subscriberCount.decrementAndGet() == 0) {
                    sseFluxMap.remove(line);
                    subscriberMap.remove(line);
                }
                System.out.println("Subscriber count is: " + subscriberCount.get());
                System.out.println("Subs per line : ");
                System.out.println("Green E : " + (subscriberMap.get("Green-E") != null ? subscriberMap.get("Green-E").get() : null));
                System.out.println("Green C : " + (subscriberMap.get("Green-C") != null ? subscriberMap.get("Green-C").get() : null));
                System.out.println("Streams open : ");
                System.out.println("count : " + sseFluxMap.size());
            });
            System.out.println("Subscriber count is: " + subscriberCount.get());
            System.out.println("Subs per line : ");
            System.out.println("Green E : " + (subscriberMap.get("Green-E") != null ? subscriberMap.get("Green-E").get() : null));
            System.out.println("Green C : " + (subscriberMap.get("Green-C") != null ? subscriberMap.get("Green-C").get() : null));
            System.out.println("Streams open : ");
            System.out.println("count : " + sseFluxMap.size());
            return flux;
        } catch (Exception e) {
            // Handle service-related exceptions
            e.printStackTrace();
            if (subscriberCount.decrementAndGet() == 0) {
                subscriberMap.remove(line);
            }
            return Flux.error(e);
        }
    }

    @GetMapping(path="/hi")
    @ResponseBody
    public String getLineData(Model model) {
        return "hi";
    }
}
