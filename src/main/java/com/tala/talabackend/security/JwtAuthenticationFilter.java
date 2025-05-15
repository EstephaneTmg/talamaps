package com.tala.talabackend.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            String username = jwtUtil.extractUsername(token.substring(7));
            if (username != null) {
                Authentication authentication = getAuthentication(username);
                setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(String username) {
        return null; // This method would get the user details from the database and set the authentication object
    }

    private void setAuthentication(Authentication authentication) {
        // Logic to set the authentication context
    }
}
