package com.example.TravelHub.Services;

import com.example.TravelHub.Dtos.OfferDto;
import com.example.TravelHub.Entities.Offer;
import com.example.TravelHub.Repositories.OfferRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public OfferService(OfferRepository offerRepository, StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.offerRepository = offerRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
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

    public List<OfferDto> findOffers(String from, String to, int limit) {
        String cacheKey = "offers:" + from + ":" + to;
        List<OfferDto> offers;

        // Cache Redis
        String cachedOffersCompressed = redisTemplate.opsForValue().get(cacheKey);
        if (cachedOffersCompressed != null && !cachedOffersCompressed.isEmpty()) {
            try {
                byte[] compressedBytes = cachedOffersCompressed.getBytes();
                ByteArrayInputStream bis = new ByteArrayInputStream(compressedBytes);
                GZIPInputStream gis = new GZIPInputStream(bis);
                byte[] decompressedBytes = gis.readAllBytes();
                gis.close();
                bis.close();

                offers = objectMapper.readValue(decompressedBytes, new TypeReference<List<OfferDto>>() {
                });
                System.out.println("Cache hit for key: " + cacheKey);
                return offers;
            } catch (IOException e) {
                System.err.println(
                        "Error reading or decompressing from Redis cache, fetching from MongoDB: " + e.getMessage());
            }
        }

        List<Offer> mongoOffers = offerRepository.findByFromAndToOrderByPriceAsc(from, to, limit);
        offers = mongoOffers.stream()
                .map(OfferDto::new)
                .collect(Collectors.toList());

        if (!offers.isEmpty()) {
            try {
                String jsonOffers = objectMapper.writeValueAsString(offers);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                GZIPOutputStream gos = new GZIPOutputStream(bos);
                gos.write(jsonOffers.getBytes());
                gos.close();
                String compressedJson = new String(bos.toByteArray());

                redisTemplate.opsForValue().set(cacheKey, compressedJson, 60, TimeUnit.SECONDS);
                System.out.println("Cache miss, data fetched from MongoDB and stored in Redis for key: " + cacheKey);
            } catch (IOException e) {
                System.err.println("Error writing or compressing to Redis cache: " + e.getMessage());
            }
        }

        return offers;
    }
}
