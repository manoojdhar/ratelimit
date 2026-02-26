package com.ratelimiter.service;

import com.ratelimiter.model.ClientConfig;

public interface RateLimiterService {
    void registerClient(ClientConfig cc);
    boolean allowRequest(String clientId);
}
