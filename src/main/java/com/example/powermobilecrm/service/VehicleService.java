package com.example.powermobilecrm.service;

import com.example.powermobilecrm.dto.users.UserResponseDTO;
import com.example.powermobilecrm.dto.vehicle.VehicleRequestDTO;
import com.example.powermobilecrm.dto.vehicle.VehicleResponseDTO;
import com.example.powermobilecrm.entity.users.User;
import com.example.powermobilecrm.entity.vehicle.Vehicle;
import com.example.powermobilecrm.repository.UserRepository;
import com.example.powermobilecrm.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private VehicleRepository repository;
    private UserRepository userRepository;

    public VehicleService(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.repository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VehicleResponseDTO createVehicle(VehicleRequestDTO data) {
        User user = findUser(data.userId());
        existsPlate(data.plate());

        Vehicle vehicle = new Vehicle(data);
        vehicle.setUser(user);
        return new VehicleResponseDTO(repository.save(vehicle));
    }

    public User findUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public List<VehicleResponseDTO> getAllVehicles() {
        return repository.findAll().stream().map(VehicleResponseDTO::new).toList();
    }

    public VehicleResponseDTO getVehicle(Long id){
        Optional<Vehicle> vehicleOptional = repository.findById(id);
        Vehicle vehicle = vehicleOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado"));
        return new VehicleResponseDTO(vehicle);
    }

    @Transactional
    public VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO data) {
        return repository.findById(id)
                .map(existingVehicle -> {
                    User user = findUser(data.userId());
                    findPlate(data.plate(), id);

                    existingVehicle.setPlate(data.plate());
                    existingVehicle.setAdvertisedPrice(data.advertisedPrice());
                    existingVehicle.setYear(data.year());
                    existingVehicle.setUser(user);

                    return new VehicleResponseDTO(repository.save(existingVehicle));
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado"));
    }


    public Boolean deleteVehicle(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public void existsPlate(String plate) {
        if (repository.existsByPlate(plate)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um veículo com essa placa.");
        }
    }

    public void findPlate(String plate, Long id) {
        repository.findByPlate(plate)
                .filter(v -> !v.getId().equals(id))
                .ifPresent(v -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um veículo com essa placa.");
                });
    }
}
