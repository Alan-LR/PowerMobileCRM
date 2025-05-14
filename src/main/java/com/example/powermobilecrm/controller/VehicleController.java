package com.example.powermobilecrm.controller;

import com.example.powermobilecrm.dto.users.UserResponseDTO;
import com.example.powermobilecrm.dto.vehicle.VehicleRequestDTO;
import com.example.powermobilecrm.dto.vehicle.VehicleResponseDTO;
import com.example.powermobilecrm.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private VehicleService service;

    public VehicleController(VehicleService vehicleService){
        this.service = vehicleService;
    }

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> createVehicle(@RequestBody @Valid VehicleRequestDTO data){
        VehicleResponseDTO vehicle = service.createVehicle(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getAllVehicles(){
        List<VehicleResponseDTO> vehicles = service.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getVehicle(@PathVariable Long id){
        VehicleResponseDTO vehicle = service.getVehicle(id);
        return ResponseEntity.ok(vehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(@PathVariable Long id, @RequestBody @Valid VehicleRequestDTO data){
        VehicleResponseDTO updateVehicle = service.updateVehicle(id, data);
        return ResponseEntity.ok(updateVehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id){
        boolean deleted = service.deleteVehicle(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
