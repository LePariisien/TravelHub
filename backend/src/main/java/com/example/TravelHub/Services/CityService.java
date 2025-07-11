package com.example.TravelHub.Services;

import com.example.TravelHub.Entities.City;
import com.example.TravelHub.Repositories.CityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public City save(City city) {
        return cityRepository.save(city);
    }

    public void delete(String id) {
        cityRepository.deleteById(id);
    }
}
