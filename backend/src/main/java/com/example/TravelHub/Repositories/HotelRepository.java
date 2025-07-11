package com.example.TravelHub.Repositories;

import com.example.TravelHub.Entities.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HotelRepository extends MongoRepository<Hotel, String> {
}
