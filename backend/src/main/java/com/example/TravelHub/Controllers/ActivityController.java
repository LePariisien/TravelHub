package com.example.TravelHub.Controllers;

import com.example.TravelHub.Entities.Activity;
import com.example.TravelHub.Services.ActivityService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public List<Activity> getAll() {
        return activityService.findAll();
    }

    @PostMapping
    public Activity create(@RequestBody Activity activity) {
        return activityService.save(activity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        activityService.delete(id);
    }
}
