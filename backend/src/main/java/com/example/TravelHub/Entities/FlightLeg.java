package com.example.TravelHub.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flightlegs")
public class FlightLeg {
    @Id
    private String id;
    private String flightNumber;
    private String departure;
    private String arrival;
    private String departureTime;
    private String arrivalTime;
}
