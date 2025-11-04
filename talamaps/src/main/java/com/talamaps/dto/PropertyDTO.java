package com.talamaps.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * DTO used for property creation and updates.
 */
@Builder
public record PropertyDTO(
    @NotBlank(message = "Address is required")
    String address,

    @NotNull(message = "Price is required")
    Double price,

    @NotBlank(message = "Property type is required")
    String propertyType, // changed from 'type' to match controller param names

    @NotNull(message = "Number of bedrooms is required")
    Integer bedrooms,

    @NotNull(message = "Number of bathrooms is required")
    Integer bathrooms,

    @NotNull(message = "Square footage is required")
    Integer squareFootage,

    @NotBlank(message = "Description is required")
    String description,

    @NotBlank(message = "Listing type is required")
    String listingType, // e.g., "For Sale" / "For Rent"

    @NotNull(message = "Latitude is required")
    Double latitude,

    @NotNull(message = "Longitude is required")
    Double longitude,

    List<String> imageUrls // optional for images uploaded later
) {}
