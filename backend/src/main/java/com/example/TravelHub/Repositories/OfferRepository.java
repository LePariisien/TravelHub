package com.example.TravelHub.Repositories;

import com.example.TravelHub.Entities.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferRepository extends MongoRepository<Offer, String> {
}
