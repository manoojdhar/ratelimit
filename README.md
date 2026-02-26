# Distributed ratelimitter using SpringBoot
A production grade horizontally scalable
- SpringBoot 3
- Redis 
- Lua Script(Atomic execution)
- Micrometer + Prometheus
- Docker & Docker Compose
- Gradle


#Architecture
 Client
    |
Rest Controller
    |
Ratelimiter Service
    |
Redis (ZSET + Lua Script)
