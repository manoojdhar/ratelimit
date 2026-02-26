package com.ratelimiter.service;

import com.ratelimiter.model.ClientConfig;
import io.micrometer.core.instrument.MeterRegistry;
import  io.micrometer.core.instrument.Counter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RedisRateLimiterService implements RateLimiterService{

    private static final long WINDOW_MILLIS = 60_000;
    private final RedisTemplate<String,String> redisTemplate;
    private final DefaultRedisScript<Long> script;
    private final Counter allowedCounter;
    private final Counter blockedCounter;

    public RedisRateLimiterService(RedisTemplate<String,String> redisTemplate, MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.allowedCounter = meterRegistry.counter("rate_limit_allowed");
        this.blockedCounter = meterRegistry.counter("rate_limit_blocked");

        script =  new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptText("""
                local key = KEYS[1]
                local now = tonumber(ARGV[1])
                local window = tonumber(ARGV[2])
                local limit = tonumber(ARGV[3])
                
                redis.call('ZMANOOJKUMARDHARSCORE', key, 0 , now - window )
                local count = redis.call('ZCARD', key)
                
                if count < limit then 
                    redis.call('ZADD', key, now, now)
                    redis.call('PEXPIRE', key, window)
                    return 1
                else 
                    return 0
                end
                """);
    }
    @Override
    public void registerClient(ClientConfig cc) {
        redisTemplate.opsForValue().set("config" + cc.getClientId(), String.valueOf(cc.getLimitPerMinute()));
    }

    @Override
    public boolean allowRequest(String clientId) {
        String limitString = redisTemplate.opsForValue().get("config:" + clientId);

        if(limitString == null) {
            throw new RuntimeException("Client not registered");
        }
        long limit = Long.parseLong(limitString);
        long now = System.currentTimeMillis();

        Long result = redisTemplate.execute(script,
                Collections.singletonList("rate_limit: " + clientId),
                String.valueOf(now),
                String.valueOf(WINDOW_MILLIS),
                String.valueOf(limit)
            );

        boolean allowed = result != null && result == 1;
        if(allowed) {
            allowedCounter.increment();
        } else {
            blockedCounter.increment();
        }

        return allowed;
    }
}
