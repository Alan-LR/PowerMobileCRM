package com.example.powermobilecrm.dto.vehicle;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record VehicleRequestDTO(
        Long id,
        @NotBlank(message = "A placa é obrigatória")
        String plate,
        BigDecimal advertisedPrice,
        @NotBlank(message = "O ano é obrigatório")
        Integer year
) {
}
