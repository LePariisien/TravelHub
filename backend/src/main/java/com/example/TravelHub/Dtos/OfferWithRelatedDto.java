package com.example.TravelHub.Dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.TravelHub.Entities.Offer;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferWithRelatedDto {

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

    private List<RelatedOfferDto> relatedOffer;

    public OfferWithRelatedDto(Offer offer, List<RelatedOfferDto> relatedOffer) {
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
        this.relatedOffer = relatedOffer;
    }

    public OfferWithRelatedDto(OfferDto offerDto) {
        this.id = offerDto.getId();
        this.from = offerDto.getFrom();
        this.to = offerDto.getTo();
        this.departDate = offerDto.getDepartDate();
        this.returnDate = offerDto.getReturnDate();
        this.provider = offerDto.getProvider();
        this.price = offerDto.getPrice();
        this.currency = offerDto.getCurrency();
        this.legs = offerDto.getLegs();
        this.hotel = offerDto.getHotel();
        this.activity = offerDto.getActivity();
    }

}
