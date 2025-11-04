package com.talamaps.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "properties")
@CompoundIndexes({
    @CompoundIndex(name = "price_type_idx", def = "{'price': 1, 'propertyType': 1}"),
    @CompoundIndex(name = "location_idx", def = "{'location': '2dsphere'}")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    @Id
    private String id;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String address;

    @Positive
    private double price;

    @NotBlank
    private String propertyType; // ENUM: HOUSE, APARTMENT, etc.

    @NotBlank
    private String listingType; // ENUM: SALE, RENT

    @Min(0)
    private int bedrooms;

    @Min(0)
    private int bathrooms;

    @Min(0)
    private double squareFootage;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    
    private GeoJsonPoint location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> imageUrls;

    // ✅ Temporary fields (not persisted)
    @Transient
    private Double latitude;

    @Transient
    private Double longitude;
}