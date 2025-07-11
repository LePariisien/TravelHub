package com.example.TravelHub.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "activities")
public class Activity {
    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    // Ajoute d'autres attributs si besoin

    // Getters et setters
}
