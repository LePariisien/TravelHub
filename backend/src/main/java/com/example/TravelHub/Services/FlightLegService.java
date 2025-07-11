package com.example.TravelHub.Services;

import com.example.TravelHub.Entities.FlightLeg;
import com.example.TravelHub.Repositories.FlightLegRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FlightLegService {
    private final FlightLegRepository flightLegRepository;

    public FlightLegService(FlightLegRepository flightLegRepository) {
        this.flightLegRepository = flightLegRepository;
    }

    public List<FlightLeg> findAll() {
        return flightLegRepository.findAll();
    }

    public FlightLeg save(FlightLeg flightLeg) {
        return flightLegRepository.save(flightLeg);
    }

    public void delete(String id) {
        flightLegRepository.deleteById(id);
    }
}
