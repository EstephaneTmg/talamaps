package com.talamaps.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private Integer phone;
    private String email;
    private String password;
    private String role = "USER";
}
