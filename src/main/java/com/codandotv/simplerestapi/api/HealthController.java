package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.response.HealthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/")
    public ResponseEntity<HealthResponse> getHealthStatus(){
        log.info("Creating response...");
        HealthResponse healthResponse = new HealthResponse("Status OK - Codando TV");
        log.info("Response has been created: {}", healthResponse);
        return ResponseEntity.ok(healthResponse);
    }
}
