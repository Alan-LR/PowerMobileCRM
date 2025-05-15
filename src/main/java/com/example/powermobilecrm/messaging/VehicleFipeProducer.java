package com.example.powermobilecrm.messaging;

import com.example.powermobilecrm.dto.vehicle.VehicleFipeDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VehicleFipeProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.fipe.queue}")
    private String queue;

    public VehicleFipeProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToFipeQueue(VehicleFipeDTO dto) {
        rabbitTemplate.convertAndSend(queue, dto);
    }
}
