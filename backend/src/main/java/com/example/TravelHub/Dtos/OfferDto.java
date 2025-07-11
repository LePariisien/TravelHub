package com.example.TravelHub.Dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.TravelHub.Entities.Offer;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto {

    private String id;

    private String from;

    private String to;

    private LocalDate departDate;

    private LocalDate returnDate;

    private String provider;

    private BigDecimal price;

    private String currency;

    private List<String> legs;

    private String hotel;

    private String activity;

    public OfferDto(Offer offer) {
        this.id = offer.getId();
        this.from = offer.getFrom();
        this.to = offer.getTo();
        this.departDate = offer.getDepartDate();
        this.returnDate = offer.getReturnDate();
        this.provider = offer.getProvider();
        this.price = offer.getPrice();
        this.currency = offer.getCurrency();
        this.legs = offer.getLegs().stream()
                .map(leg -> leg.getId())
                .toList();
        this.hotel = offer.getHotel().getId();
        this.activity = offer.getActivity().getId();
    }

}
