package com.example.TravelHub.Controllers;

import com.example.TravelHub.Entities.FlightLeg;
import com.example.TravelHub.Services.FlightLegService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/flightlegs")
public class FlightLegController {
    private final FlightLegService flightLegService;

    public FlightLegController(FlightLegService flightLegService) {
        this.flightLegService = flightLegService;
    }

    @GetMapping
    public List<FlightLeg> getAll() {
        return flightLegService.findAll();
    }

    @PostMapping
    public FlightLeg create(@RequestBody FlightLeg flightLeg) {
        return flightLegService.save(flightLeg);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        flightLegService.delete(id);
    }
}
