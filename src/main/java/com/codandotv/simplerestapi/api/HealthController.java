package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.response.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> getHealthStatus(){
        HealthResponse healthResponse = new HealthResponse("Status OK - Codando TV");
        return ResponseEntity.ok(healthResponse);
    }
}
