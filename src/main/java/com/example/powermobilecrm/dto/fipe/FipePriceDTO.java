package com.example.powermobilecrm.dto.fipe;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FipePriceDTO(@JsonProperty("Valor") String valor) {
}
