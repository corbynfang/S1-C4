package com.example.Backend.repository;

import com.example.Backend.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
} // Spring Data JPA gives us save(), findAll(), findById(), etc. for free. We just declare the interface - no implementation needed.
