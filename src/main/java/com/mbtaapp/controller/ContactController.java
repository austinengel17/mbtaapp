package com.mbtaapp.controller;

import com.mbtaapp.model.EmailRequest;
import com.mbtaapp.service.ContactService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@RequestMapping("website/v1/contact")
@Controller
public class ContactController {
    private ContactService service;
    private final Bucket bucket;
    private final int BUCKET_CAPACITY = 20;


    @Autowired
    ContactController(ContactService service){
        this.service = service;

        Bandwidth limit = Bandwidth.classic(BUCKET_CAPACITY, Refill.intervally(10, Duration.ofMinutes(60)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping(path="/email")
    @CrossOrigin(origins = "*")
    public Mono<ResponseEntity<Object>> postEmail(@RequestBody EmailRequest request) {
        //TODO: input validation/sanitation
        if(bucket.tryConsume(1)) {
            return service.sendEmail(request);
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests! (> 20 in the past hour), try again later or reach out on LinkedIn!"));
        }
    }
}
