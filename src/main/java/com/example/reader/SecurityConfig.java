package com.example.reader;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private ReaderRepository repository;

    public SecurityConfig(ReaderRepository repository) {
        this.repository = repository;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/").hasRole("READER")).
                formLogin(log -> log.loginPage("/login").failureUrl("/login?error=true"));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Reader reader = repository.findByUsername(username);
            if (reader != null) {
                return org.springframework.security.core.userdetails.User
                        .withUsername(username)
                        .password(reader.getPassword()) // Use appropriate password field from Reader entity
                        .roles("READER") // Provide necessary roles/authorities
                        .build();
            }
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        };
    }
}


