package com.example.TravelHub.Services;

import com.example.TravelHub.Entities.Activity;
import com.example.TravelHub.Repositories.ActivityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    public void delete(String id) {
        activityRepository.deleteById(id);
    }
}
