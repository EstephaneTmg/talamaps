package com.talamaps.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talamaps.model.Property;
import com.talamaps.service.PropertyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
@CrossOrigin
public class PropertyController {

    private final PropertyService service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ✅ New: Create property with image upload
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public Property createPropertyWithImages(
            @RequestPart("property") String propertyJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        Property property = objectMapper.readValue(propertyJson, Property.class);
        
        // ✅ Convert lat/lng to GeoJSON location
    if (property.getLocation() == null && property.getLatitude() != null && property.getLongitude() != null) {
        property.setLocation(new GeoJsonPoint(property.getLongitude(), property.getLatitude()));
    }
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            for (MultipartFile image : images) {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, image.getBytes());
                imageUrls.add("/uploads/" + fileName);
            }
        }

        property.setImageUrls(imageUrls);
        return service.createProperty(property);
    }

    @PostMapping
    public Property createProperty(@Valid @RequestBody Property property) {
        return service.createProperty(property);
    }
    // ✅ Upload images for a property
    @PostMapping("/{id}/images")
    public ResponseEntity<Property> uploadImages(
            @PathVariable String id,
            @RequestParam("files") List<MultipartFile> files
    ) throws IOException {
        Property updated = service.uploadPropertyImages(id, files);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public Page<Property> getProperties(
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) String listingType,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String address,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getProperties(propertyType, listingType, minPrice, maxPrice, address, page, size);
    }

    @GetMapping("/{id}")
    public Property getProperty(@PathVariable String id) {
        return service.getProperty(id).orElseThrow(() -> new RuntimeException("Property not found"));
    }

    @PutMapping("/{id}")
    public Property updateProperty(@PathVariable String id, @Valid @RequestBody Property property) {
        return service.updateProperty(id, property);
    }

    @DeleteMapping("/{id}")
    public void deleteProperty(@PathVariable String id) {
        service.deleteProperty(id);
    }

    @GetMapping("/search")
    public List<Property> searchByLocation(@RequestParam double lat,
                                        @RequestParam double lng,
                                        @RequestParam double radiusKm) {
        return service.searchByLocation(lat, lng, radiusKm);
    }
}
