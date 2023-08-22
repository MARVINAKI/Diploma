package com.example.diploma.repository;

import com.example.diploma.dto.ExtendedAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtendedAdRepository extends JpaRepository<ExtendedAd, Integer> {
}
