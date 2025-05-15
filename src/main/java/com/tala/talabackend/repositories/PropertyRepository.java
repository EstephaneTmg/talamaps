package com.tala.talabackend.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tala.talabackend.models.Property;

public interface PropertyRepository extends MongoRepository<Property, String> {
    @Query("{ $text: { $search: ?0 } }")
    List<Property> searchByText(String keyword);
}
