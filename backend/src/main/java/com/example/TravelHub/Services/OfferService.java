package com.example.TravelHub.Services;

import com.example.TravelHub.Dtos.OfferDto;
import com.example.TravelHub.Entities.Offer;
import com.example.TravelHub.Repositories.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class OfferService {
    private final OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<OfferDto> findAll() {
        return offerRepository.findAll().stream()
                .map(OfferDto::new)
                .toList();
    }

    public OfferDto save(Offer offer) {
        return new OfferDto(offerRepository.save(offer));
    }

    public void delete(String id) {
        offerRepository.deleteById(id);
    }

    public OfferDto findById(String id) {
        return offerRepository.findById(id)
                .map(OfferDto::new)
                .orElseThrow(() -> new RuntimeException("Offer not found with id: " + id));
    }

    public Collection<OfferDto> findByFromAndToWithLimit(String from, String to, int limit) {
        return offerRepository.findByFromAndTo(from, to).stream()
                .limit(limit)
                .toList();
    }
}
