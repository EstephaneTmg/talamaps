package com.talamaps.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.talamaps.model.Property;
import com.talamaps.repository.PropertyRepository;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository repository;

    public Property createProperty(Property property) {
        property.setCreatedAt(LocalDateTime.now());
        property.setUpdatedAt(LocalDateTime.now());
        return repository.save(property);
    }

    public Page<Property> getProperties(String propertyType, String listingType, Double minPrice, Double maxPrice, String address, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repository.findByFilters(propertyType, listingType, minPrice, maxPrice, address, pageable);
    }

    public Optional<Property> getProperty(String id) {
        return repository.findById(id);
    }

    public Property updateProperty(String id, Property property) {
        return repository.findById(id).map(existing -> {
            property.setId(id);
            property.setCreatedAt(existing.getCreatedAt());
            property.setUpdatedAt(LocalDateTime.now());
            return repository.save(property);
        }).orElseThrow(() -> new RuntimeException("Property not found"));
    }
    public Property uploadPropertyImages(String id, List<MultipartFile> files) throws IOException {
        Property property = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        List<String> imageUrls = property.getImageUrls() != null ? property.getImageUrls() : new ArrayList<>();

        String uploadDir = "uploads/properties/" + id;
        Files.createDirectories(Paths.get(uploadDir));

        for (MultipartFile file : files) {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            Files.write(filePath, file.getBytes());
            imageUrls.add("/" + uploadDir + "/" + filename); // URL accessible path
        }

        property.setImageUrls(imageUrls);
        return repository.save(property);
    }
    public void deleteProperty(String id) {
        repository.deleteById(id);
    }

    public List<Property> searchByLocation(double lat, double lng, double radiusKm) {
        Point location = new Point(lng, lat);
        Distance distance = new Distance(radiusKm, Metrics.KILOMETERS);
        return repository.findByLocationNear(location, distance);
        
    }
}