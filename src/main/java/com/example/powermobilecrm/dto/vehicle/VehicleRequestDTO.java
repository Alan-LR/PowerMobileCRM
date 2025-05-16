package com.example.powermobilecrm.dto.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VehicleRequestDTO(
        @NotBlank(message = "A placa é obrigatória")
        String plate,
        @NotNull(message = "O valor do veículo é obrigatório")
        BigDecimal advertisedPrice,
        @NotNull(message = "O ano é obrigatório")
        Integer year,
        Long userId,
        @NotBlank(message = "O ID da marca é obrigatório")
        String brandId,
        @NotBlank(message = "O ID do model é obrigatório")
        String modelId,
        @NotBlank(message = "O código do ano FIPE é obrigatório")
        String yearFipeCode
) {
}
