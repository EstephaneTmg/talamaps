package com.tala.talabackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tala.talabackend.models.Property;
import com.tala.talabackend.repositories.PropertyRepository;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @GetMapping("/{id}")
    public Property getPropertyById(@PathVariable String id) {
        return propertyRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Property createProperty(@RequestBody Property property) {
        return propertyRepository.save(property);
    }

    @PutMapping("/{id}")
    public Property updateProperty(@PathVariable String id, @RequestBody Property updatedProperty) {
        return propertyRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(updatedProperty.getTitle());
                    existing.setDescription(updatedProperty.getDescription());
                    existing.setLocation(updatedProperty.getLocation());
                    existing.setPrice(updatedProperty.getPrice());
                    existing.setPropertyType(updatedProperty.getPropertyType());
                    existing.setBedrooms(updatedProperty.getBedrooms());
                    return propertyRepository.save(existing);
                }).orElse(null);
    }
    @DeleteMapping("/{id}")
    public String deleteProperty(@PathVariable String id) {
        propertyRepository.deleteById(id);
        return "Property deleted";
    }
}
