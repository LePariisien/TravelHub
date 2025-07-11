package com.example.TravelHub.Controllers;

import com.example.TravelHub.Entities.City;
import com.example.TravelHub.Services.CityService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<City> getAll() {
        return cityService.findAll();
    }

    @PostMapping
    public City create(@RequestBody City city) {
        return cityService.save(city);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        cityService.delete(id);
    }
}
