package com.ratelimiter.controller;

import com.ratelimiter.model.ClientConfig;
import com.ratelimiter.service.RateLimiterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class RateLimiterController {

    private final RateLimiterService service;

    public RateLimiterController(RateLimiterService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok("App is working");
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/clients")
    public ResponseEntity<?> registerClient(@RequestBody ClientConfig config) {
        service.registerClient(config);
        return ResponseEntity.ok("Client registered");
    }

    @GetMapping("/allow/{clientId}")
    public ResponseEntity<?> allow(@PathVariable String clientId){
        boolean allowed = service.allowRequest(clientId);

        if(!allowed) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .header("X-RateLimit-Exceeded", "true").body(Map.of("allowed", false));
        }
        return ResponseEntity.ok(Map.of("allowed", true));
    }
}
