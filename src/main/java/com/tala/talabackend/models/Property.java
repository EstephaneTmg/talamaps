package com.tala.talabackend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "properties")
@Data
public class Property {
    @Id
    private String id;

    @TextIndexed
    private String title;

    @TextIndexed
    private String description;

    @Indexed
    private String city;
    
    @Indexed
    private String location;

    @Indexed
    private double price;

    @Indexed
        private String propertyType;
    
    @Indexed
    private int bedrooms;

    //@Indexed
    //private boolean available;
}

