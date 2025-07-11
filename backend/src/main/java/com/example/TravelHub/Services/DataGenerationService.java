package com.example.TravelHub.Services;

import com.example.TravelHub.Entities.*;
import com.example.TravelHub.Repositories.OfferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class DataGenerationService {

    private final OfferRepository offerRepository;
    private final Random random = new Random();

    public DataGenerationService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<Offer> generateOffers(int count) {
        String[] cities = { "Paris", "Rome", "Berlin", "Madrid", "Lisbon", "New York", "Tokyo", "Cairo", "Dubai",
                "Toronto" };
        String[] providers = { "Skyscanner", "Expedia", "TravelPro", "GoFly", "Booking.com" };
        String[] hotelNames = { "Grand Palace", "Sunset Inn", "Ocean View", "City Lodge", "Elite Suites" };
        String[] activityNames = { "Visite guidée", "Croisière", "Atelier cuisine", "Musée", "Randonnée" };

        List<Offer> offers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String from = cities[random.nextInt(cities.length)];
            String to;
            do {
                to = cities[random.nextInt(cities.length)];
            } while (to.equals(from));

            LocalDate departDate = LocalDate.now().plusDays(random.nextInt(30));
            LocalDate returnDate = departDate.plusDays(3 + random.nextInt(7));

            Hotel hotel = new Hotel(
                    UUID.randomUUID().toString(),
                    hotelNames[random.nextInt(hotelNames.length)],
                    to + ", " + (100 + random.nextInt(900)) + " Rue de l’hôtel",
                    3 + random.nextInt(3));

            Activity activity = new Activity(
                    UUID.randomUUID().toString(),
                    activityNames[random.nextInt(activityNames.length)],
                    "Profitez d’une activité inoubliable à " + to + " !",
                    30 + random.nextDouble() * 70);

            List<FlightLeg> legs = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                legs.add(new FlightLeg(
                        UUID.randomUUID().toString(),
                        "FL" + (1000 + random.nextInt(9000)),
                        j == 0 ? from : "Transit" + j,
                        j == 1 ? to : "Transit" + (j + 1),
                        "08:00", "10:30"));
            }

            Offer offer = Offer.builder()
                    .id(UUID.randomUUID().toString())
                    .from(from)
                    .to(to)
                    .departDate(departDate)
                    .returnDate(returnDate)
                    .provider(providers[random.nextInt(providers.length)])
                    .price(BigDecimal.valueOf(100 + random.nextInt(900)))
                    .currency("EUR")
                    .legs(legs)
                    .hotel(hotel)
                    .activity(activity)
                    .build();

            offers.add(offerRepository.save(offer));
        }

        return offers;
    }
}
