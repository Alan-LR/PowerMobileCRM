package com.example.powermobilecrm.service;

import com.example.powermobilecrm.repository.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private VehicleRepository repository;

    public VehicleService(VehicleRepository vehicleRepository){
        this.repository = vehicleRepository;
    }
}
