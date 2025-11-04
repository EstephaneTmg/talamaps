package com.talamaps.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private Integer phone;
    private String email;
    private String password;
}
