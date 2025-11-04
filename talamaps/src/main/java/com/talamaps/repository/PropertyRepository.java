package com.talamaps.repository;

import java.util.List;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.talamaps.model.Property;

@Repository
public interface PropertyRepository extends MongoRepository<Property, String>, PropertyRepositoryCustom {
    List<Property> findByPriceBetween(double min, double max);
    List<Property> findByPropertyType(String propertyType);
    List<Property> findByListingType(String listingType);
    List<Property> findByLocationNear(Point location, Distance distance);
}