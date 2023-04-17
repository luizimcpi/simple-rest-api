package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.response.HealthResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @GetMapping("/")
    public ResponseEntity<HealthResponse> getHealthStatus(){
        logger.info("Creating response...");
        HealthResponse healthResponse = new HealthResponse("Status OK - Codando TV");
        logger.info("Response has been created: {}", healthResponse);
        return ResponseEntity.ok(healthResponse);
    }
}
