package com.example.TravelHub.Repositories;

import com.example.TravelHub.Entities.FlightLeg;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FlightLegRepository extends MongoRepository<FlightLeg, String> {
}
