package com.example.TravelHub.Controllers;

import com.example.TravelHub.Entities.Hotel;
import com.example.TravelHub.Services.HotelService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public List<Hotel> getAll() {
        return hotelService.findAll();
    }

    @PostMapping
    public Hotel create(@RequestBody Hotel hotel) {
        return hotelService.save(hotel);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        hotelService.delete(id);
    }
}
