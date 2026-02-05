package com.example.demo.config; // adapte le package selon ton projet

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Autorise ton frontend Angular
        config.addAllowedOrigin("http://localhost:4200");
        
        // Autorise toutes les méthodes HTTP
        config.addAllowedMethod("*");
        
        // Autorise tous les headers
        config.addAllowedHeader("*");
        
        // Autorise les cookies/credentials
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}