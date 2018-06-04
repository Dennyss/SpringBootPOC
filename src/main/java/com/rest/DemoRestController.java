package com.rest;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by denys.kovalenko on 5/23/2018.
 */
@RestController
public class DemoRestController {

    @GetMapping("/askHello")
    public String getDemoEndpoint(@RequestParam String name){
        ThreadContext.clearAll();
        return "Hello " + name;
    }

    @PostMapping("/postDemo")
    public ResponseEntity<String> postDemoEndpoint(@RequestBody String body, @RequestHeader(value = "Content-Type") String contentType){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");
        ThreadContext.clearAll();
        return new ResponseEntity<String>("{Simple demo reply body}", responseHeaders, HttpStatus.OK);
    }
}
