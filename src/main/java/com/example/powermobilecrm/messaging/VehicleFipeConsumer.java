package com.example.powermobilecrm.messaging;

import com.example.powermobilecrm.dto.vehicle.VehicleFipeDTO;
import com.example.powermobilecrm.entity.vehicle.Vehicle;
import com.example.powermobilecrm.repository.VehicleRepository;
import com.example.powermobilecrm.service.FipeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class VehicleFipeConsumer {

    private final VehicleRepository vehicleRepository;
    private final FipeService fipeService;

    public VehicleFipeConsumer(VehicleRepository vehicleRepository, FipeService fipeService) {
        this.vehicleRepository = vehicleRepository;
        this.fipeService = fipeService;
    }

    @RabbitListener(queues = "${rabbitmq.fipe.queue}")
    public void receiveFromQueue(VehicleFipeDTO dto) {
        Optional<Vehicle> optional = vehicleRepository.findById(dto.vehicleId());
        if (optional.isEmpty()) return;

        Vehicle vehicle = optional.get();

        BigDecimal fipePrice = fipeService.getFipePrice(dto.brandId(), dto.modelId(), dto.yearFipeCode());
        vehicle.setFipePrice(fipePrice);

        vehicleRepository.save(vehicle);
    }
}

