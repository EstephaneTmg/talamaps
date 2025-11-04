package com.talamaps.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.talamaps.model.Property;

public interface PropertyRepositoryCustom {
    Page<Property> findByFilters(String propertyType, String listingType, Double minPrice, Double maxPrice, String address, Pageable pageable);
}
