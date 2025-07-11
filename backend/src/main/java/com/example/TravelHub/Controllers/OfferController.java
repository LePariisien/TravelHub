package com.example.TravelHub.Controllers;

import com.example.TravelHub.Entities.Offer;
import com.example.TravelHub.Services.OfferService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {
    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public List<Offer> getAll() {
        return offerService.findAll();
    }

    @PostMapping
    public Offer create(@RequestBody Offer offer) {
        return offerService.save(offer);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        offerService.delete(id);
    }
}
