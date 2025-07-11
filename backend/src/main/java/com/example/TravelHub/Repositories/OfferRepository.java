package com.example.TravelHub.Repositories;

import com.example.TravelHub.Dtos.OfferDto;
import com.example.TravelHub.Entities.Offer;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferRepository extends MongoRepository<Offer, String> {
    List<OfferDto> findByFromAndTo(String from, String to);
}
