package com.example.powermobilecrm.dto.vehicle;

import java.io.Serializable;

public record VehicleFipeDTO(Long vehicleId, String brandId, String modelId, String yearFipeCode) implements Serializable {}


