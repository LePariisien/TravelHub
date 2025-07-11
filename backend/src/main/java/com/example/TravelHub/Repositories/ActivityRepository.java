package com.example.TravelHub.Repositories;

import com.example.TravelHub.Entities.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityRepository extends MongoRepository<Activity, String> {
}
