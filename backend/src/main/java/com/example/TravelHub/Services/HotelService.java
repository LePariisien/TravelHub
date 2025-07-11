package com.example.TravelHub.Services;

import com.example.TravelHub.Entities.Hotel;
import com.example.TravelHub.Repositories.HotelRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    public Hotel save(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public void delete(String id) {
        hotelRepository.deleteById(id);
    }
}
