package com.example.TravelHub.Services;

import com.example.TravelHub.Entities.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.time.Duration;

@Service
public class SessionService {
    private static final String SESSION_KEY_PREFIX = "session:";

    private final RedisTemplate<String, Session> redisTemplate;
    private final ValueOperations<String, Session> valueOps;

    @Autowired
    public SessionService(RedisTemplate<String, Session> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    public List<Session> findAll() {
        Set<String> keys = redisTemplate.keys(SESSION_KEY_PREFIX + "*");
        List<Session> sessions = new ArrayList<>();
        if (keys != null) {
            for (String key : keys) {
                Session session = valueOps.get(key);
                if (session != null) {
                    sessions.add(session);
                }
            }
        }
        return sessions;
    }

    public Session save(Session session) {
        if (session.getId() == null) {
            session.setId(java.util.UUID.randomUUID().toString());
        }
        valueOps.set(SESSION_KEY_PREFIX + session.getId(), session);
        return session;
    }

    public void delete(String id) {
        redisTemplate.delete(SESSION_KEY_PREFIX + id);
    }
    // Ajout pour le controller UserController
    public void saveSession(String token, String userId, long expiresIn) {
        Session session = new Session();
        session.setId(token);
        session.setSessionToken(token);
        session.setUserId(userId);
        valueOps.set(SESSION_KEY_PREFIX + token, session);
        // Optionnel : gérer l'expiration si besoin
        redisTemplate.expire(SESSION_KEY_PREFIX + token, Duration.ofSeconds(expiresIn));
    }
}
