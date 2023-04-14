package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.response.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class HealthController {

    private static final Logger LOG = Logger.getLogger(HealthController.class.getName());

    @GetMapping("/")
    public ResponseEntity<HealthResponse> getHealthStatus(){
        LOG.info("Creating response...");
        HealthResponse healthResponse = new HealthResponse("Status OK - Codando TV");
        return ResponseEntity.ok(healthResponse);
    }
}
