package com.example.reader;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderRepository extends JpaRepository<Reader, String> {
    Reader findByUsername(String username);
}
