package com.example.TravelHub.Services;

import com.example.TravelHub.Dtos.OfferDto;
import com.example.TravelHub.Dtos.OfferWithRelatedDto;
import com.example.TravelHub.Dtos.RelatedOfferDto;
import com.example.TravelHub.Entities.Offer;
import com.example.TravelHub.Repositories.OfferRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
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
    private final Neo4jService neo4jService;

    public OfferService(OfferRepository offerRepository, StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
            Neo4jService neo4jService) {
        this.offerRepository = offerRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.neo4jService = neo4jService;
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

    public OfferWithRelatedDto findById(String id) {
        String cacheKey = "offers:" + id;
        OfferDto offerDto;

        // Cache Redis
        String cachedOfferCompressed = redisTemplate.opsForValue().get(cacheKey);
        if (cachedOfferCompressed != null && !cachedOfferCompressed.isEmpty()) {
            try {
                byte[] compressedBytes = cachedOfferCompressed.getBytes();
                ByteArrayInputStream bis = new ByteArrayInputStream(compressedBytes);
                GZIPInputStream gis = new GZIPInputStream(bis);
                byte[] decompressedBytes = gis.readAllBytes();
                gis.close();
                bis.close();

                offerDto = objectMapper.readValue(decompressedBytes, OfferDto.class);
                System.out.println("Cache hit for offer details key: " + cacheKey);
                return new OfferWithRelatedDto(offerDto);
            } catch (IOException e) {
                System.err.println("Error reading or decompressing from Redis cache for offer " + id
                        + ", fetching from MongoDB: " + e.getMessage());
            }
        }

        Offer offer = offerRepository.findById(id).get();
        if (offer == null) {
            System.err.println("Offer with ID " + id + " not found in MongoDB.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer not found");
        }

        List<String> relatedCityCodes = neo4jService.findRelatedCities(offer.getFrom(), 3);
        List<RelatedOfferDto> relatedOffers = findSampleRelatedOffers(offer.getId(), relatedCityCodes,
                offer.getDepartDate(), offer.getReturnDate());

        OfferWithRelatedDto offerWithRelatedDto = new OfferWithRelatedDto(offer, relatedOffers);

        try {
            String jsonOfferDetail = objectMapper.writeValueAsString(offerWithRelatedDto);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gos = new GZIPOutputStream(bos);
            gos.write(jsonOfferDetail.getBytes());
            gos.close();
            String compressedJson = new String(bos.toByteArray());

            redisTemplate.opsForValue().set(cacheKey, compressedJson, 300, TimeUnit.SECONDS);
            System.out.println("Cache miss for offer " + id + ", data fetched from MongoDB/Neo4j and stored in Redis.");
        } catch (IOException e) {
            System.err.println("Error writing or compressing to Redis cache for offer " + id + ": " + e.getMessage());
        }

        return offerWithRelatedDto;
    }

    private List<RelatedOfferDto> findSampleRelatedOffers(String currentOfferId, List<String> relatedCityCodes,
            LocalDate departDate, LocalDate returnDate) {
        return relatedCityCodes.stream()
                .map(cityCode -> {
                    return new RelatedOfferDto("simulated-id-" + cityCode.toLowerCase(), cityCode, "SampleProvider",
                            100.0);
                })
                .limit(3)
                .collect(Collectors.toList());
    }

}
