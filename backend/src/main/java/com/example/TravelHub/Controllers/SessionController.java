package com.example.TravelHub.Controllers;

import com.example.TravelHub.Entities.Session;
import com.example.TravelHub.Services.SessionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public List<Session> getAll() {
        return sessionService.findAll();
    }

    @PostMapping
    public Session create(@RequestBody Session session) {
        return sessionService.save(session);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        sessionService.delete(id);
    }
}
