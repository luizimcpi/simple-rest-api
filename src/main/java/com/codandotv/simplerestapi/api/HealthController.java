package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.response.HealthResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private static final Log LOGGER = LogFactory.getLog(HealthController.class);

    @GetMapping("/")
    public ResponseEntity<HealthResponse> getHealthStatus(){
        LOGGER.info("Creating response...");
        HealthResponse healthResponse = new HealthResponse("Status OK - Codando TV");
        LOGGER.info("Response has been created: "+ healthResponse);
        return ResponseEntity.ok(healthResponse);
    }
}
