package com.example.TravelHub.Dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatedOfferDto {
    private String id;
    private String toCityCode;
    private String provider;
    private double price;
}
