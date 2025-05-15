package com.example.powermobilecrm.dto.vehicle;

import com.example.powermobilecrm.dto.users.UserResponseDTO;
import com.example.powermobilecrm.entity.vehicle.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VehicleResponseDTO(
        Long id,
        String plate,
        BigDecimal advertisedPrice,
        Integer year,
        LocalDateTime createdAt,
        UserResponseDTO user,
        String brandId,
        String brandName,
        String modelId,
        String modelName,
        BigDecimal fipePrice
) {
    public VehicleResponseDTO(Vehicle vehicle) {
        this(
                vehicle.getId(),
                vehicle.getPlate(),
                vehicle.getAdvertisedPrice(),
                vehicle.getYear(),
                vehicle.getCreatedAt(),
                vehicle.getUser() != null ? new UserResponseDTO(vehicle.getUser()) : null,
                vehicle.getBrand() != null ? vehicle.getBrand().getId() : null,
                vehicle.getBrand() != null ? vehicle.getBrand().getName() : null,
                vehicle.getModel() != null ? vehicle.getModel().getId() : null,
                vehicle.getModel() != null ? vehicle.getModel().getName() : null,
                vehicle.getFipePrice()
        );
    }
}

