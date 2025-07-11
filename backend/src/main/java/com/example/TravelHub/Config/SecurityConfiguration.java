package com.example.TravelHub.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

            .authorizeHttpRequests(auth -> auth
                // Swagger: autorisé sans authentification (tous les sous-chemins)
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                // tes autres routes publiques (si besoin)
                .requestMatchers("/public/**").permitAll()
                // Tout le reste est accessible sans authentification
                .anyRequest().permitAll()
            )
            .csrf().disable()
            .httpBasic().disable();

        return http.build();
    }
}

