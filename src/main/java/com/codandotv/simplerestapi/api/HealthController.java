package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.response.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<HealthResponse> getHealthStatus(){
        HealthResponse healthResponse = new HealthResponse("OK - Codando TV");
        return ResponseEntity.ok(healthResponse);
    }
}
