package com.example.ebankingportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        return token -> Jwt.withTokenValue(token)
                .header("alg", "none")
<<<<<<< HEAD
<<<<<<< HEAD
                .claim("sub", "P-0123456789")
=======
                .claim("sub", "P-0123456789") // We manually set the customer ID here
>>>>>>> 3b820ba (initial commit for ebanking portal transaction)
=======
                .claim("sub", "P-0123456789")
>>>>>>> 9acf0d8 (initial commit for ebanking portal transaction)
                .build();
    }
}