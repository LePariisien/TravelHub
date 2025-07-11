package com.example.TravelHub.Controllers;

import com.example.TravelHub.Dtos.OfferDto;
import com.example.TravelHub.Dtos.OfferWithRelatedDto;
import com.example.TravelHub.Entities.Offer;
import com.example.TravelHub.Services.DataGenerationService;
import com.example.TravelHub.Services.OfferService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    private final DataGenerationService dataGenerationService;

    public OfferController(DataGenerationService dataGenerationService, OfferService offerService) {
        this.dataGenerationService = dataGenerationService;
        this.offerService = offerService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OfferWithRelatedDto> getOfferDetails(@PathVariable String id) {
        long startTime = System.nanoTime();

        try {
            OfferWithRelatedDto offerDetail = offerService.findById(id);

            long endTime = System.nanoTime();
            long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            System.out.println("GET /offers/" + id + " executed in " + durationMs + " ms");

            return ResponseEntity.ok(offerDetail);
        } catch (ResponseStatusException e) {
            System.err.println("API Error for /offers/" + id + ": " + e.getReason());
            throw e;
        } catch (Exception e) {
            System.err.println("Internal Server Error for /offers/" + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OfferDto>> searchOffers(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(defaultValue = "10") int limit) {
        long startTime = System.nanoTime();

        try {
            List<OfferDto> offers = offerService.findOffers(from, to, limit);

            long endTime = System.nanoTime();
            long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            System.out.println("GET /offers?from=" + from + "&to=" + to + "&limit=" + limit + " executed in "
                    + durationMs + " ms");

            return ResponseEntity.ok(offers);
        } catch (IllegalArgumentException e) {
            System.err.println("Bad Request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            System.err.println("Internal Server Error for /offers: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/generate")
    public List<Offer> generateOffers(@RequestParam(defaultValue = "20") int count) {
        return dataGenerationService.generateOffers(count);
    }
}
