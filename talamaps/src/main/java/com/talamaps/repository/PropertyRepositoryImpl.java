package com.talamaps.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.talamaps.model.Property;

@Repository
public class PropertyRepositoryImpl implements PropertyRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Property> findByFilters(String propertyType, String listingType, Double minPrice, Double maxPrice, String address, Pageable pageable) {
        Query query = new Query();

        if (propertyType != null && !propertyType.isEmpty()) {
            query.addCriteria(Criteria.where("propertyType").is(propertyType));
        }
        if (listingType != null && !listingType.isEmpty()) {
            query.addCriteria(Criteria.where("listingType").is(listingType));
        }
        if (minPrice != null || maxPrice != null) {
            Criteria priceCriteria = Criteria.where("price");
            if (minPrice != null && maxPrice != null) {
                priceCriteria.gte(minPrice).lte(maxPrice);
            } else if (minPrice != null) {
                priceCriteria.gte(minPrice);
            } else {
                priceCriteria.lte(maxPrice);
            }
            query.addCriteria(priceCriteria);
        }
        if (address != null && !address.isEmpty()) {
            // Case-insensitive contains search on address
            query.addCriteria(Criteria.where("address").regex(address, "i"));
        }

        long count = mongoTemplate.count(query, Property.class);
        query.with(pageable);

        List<Property> properties = mongoTemplate.find(query, Property.class);

        return new PageImpl<>(properties, pageable, count);
    }
}
