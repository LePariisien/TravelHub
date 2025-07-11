package com.example.TravelHub.Repositories;

import com.example.TravelHub.Entities.City;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CityRepository extends Neo4jRepository<City, String> {
}
