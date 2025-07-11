package com.example.TravelHub.Services;

import com.example.TravelHub.Entities.Offer;
import com.example.TravelHub.Repositories.OfferRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OfferService {
    private final OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<Offer> findAll() {
        return offerRepository.findAll();
    }

    public Offer save(Offer offer) {
        return offerRepository.save(offer);
    }

    public void delete(String id) {
        offerRepository.deleteById(id);
    }
}
