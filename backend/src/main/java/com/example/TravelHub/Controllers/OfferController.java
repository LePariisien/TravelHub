package com.example.TravelHub.Controllers;

import com.example.TravelHub.Dtos.OfferDto;
import com.example.TravelHub.Entities.Offer;
import com.example.TravelHub.Services.DataGenerationService;
import com.example.TravelHub.Services.OfferService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    private final DataGenerationService dataGenerationService;

    public OfferController(DataGenerationService dataGenerationService, OfferService offerService) {
        this.dataGenerationService = dataGenerationService;
        this.offerService = offerService;
    }

    @GetMapping("/{id}")
    public OfferDto getById(@PathVariable String id) {
        return offerService.findById(id);
    }

    @GetMapping
    public List<OfferDto> getAll() {
        return offerService.findAll();
    }

    @GetMapping(params = { "from", "to", "limit" })
    public Collection<OfferDto> getOffersByCitiesAndLimit(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("limit") int limit) {
        return offerService.findByFromAndToWithLimit(from, to, limit);
    }

    @PostMapping
    public OfferDto create(@RequestBody Offer offer) {
        return offerService.save(offer);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        offerService.delete(id);
    }

    @PostMapping("/generate")
    public List<Offer> generateOffers(@RequestParam(defaultValue = "20") int count) {
        return dataGenerationService.generateOffers(count);
    }
}
