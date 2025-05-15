package com.tala.talabackend.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tala.talabackend.models.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
