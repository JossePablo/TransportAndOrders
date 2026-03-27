package com.empresa.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.api.model.entity.Driver;

public interface DriverRepository extends JpaRepository<Driver, UUID> {
    // Listar conductores
    List<Driver> findByActiveTrue();
}