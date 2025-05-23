package com.example.powermobilecrm.service;

import com.example.powermobilecrm.dto.vehicle.VehicleFipeDTO;
import com.example.powermobilecrm.dto.vehicle.VehicleRequestDTO;
import com.example.powermobilecrm.dto.vehicle.VehicleResponseDTO;
import com.example.powermobilecrm.entity.brand.Brand;
import com.example.powermobilecrm.entity.model.Model;
import com.example.powermobilecrm.entity.users.User;
import com.example.powermobilecrm.entity.vehicle.Vehicle;
import com.example.powermobilecrm.messaging.VehicleFipeProducer;
import com.example.powermobilecrm.repository.BrandRepository;
import com.example.powermobilecrm.repository.ModelRepository;
import com.example.powermobilecrm.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class VehicleService {

    public static final String VEHICLE_NOT_FOUND = "Veículo não encontrado";
    public static final String VEHICLE_PLATE_ALREADY_EXISTS = "Já existe um veículo com essa placa.";

    private VehicleRepository repository;
    private VehicleFipeProducer vehicleFipeProducer;
    private FipeService fipeService;
    private BrandRepository brandRepository;
    private ModelRepository modelRepository;
    private UserService userService;

    public VehicleService(VehicleRepository vehicleRepository, VehicleFipeProducer vehicleFipeProducer, FipeService fipeService, BrandRepository brandRepository, ModelRepository modelRepository, UserService userService) {
        this.repository = vehicleRepository;
        this.vehicleFipeProducer = vehicleFipeProducer;
        this.fipeService = fipeService;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.userService = userService;
    }

    @Transactional
    public VehicleResponseDTO createVehicle(VehicleRequestDTO data) {
        existsPlate(data.plate());
        User user = null;
        if (Objects.nonNull(data.userId())) {
            user = userService.findUser(data.userId());
        }

        Brand brand = getOrCreateBrand(data.brandId());
        Model model = getOrCreateModel(data.modelId(), data.brandId(), brand);

        validateFipeData(data.brandId(), data.modelId(), data.yearFipeCode());

        Vehicle vehicle = buildVehicle(data, user, brand, model);
        vehicle = repository.save(vehicle);

        sendFipeRequest(vehicle, data);

        return new VehicleResponseDTO(vehicle);
    }

    private Vehicle buildVehicle(VehicleRequestDTO data, User user, Brand brand, Model model) {
        Vehicle vehicle = new Vehicle(data);
        vehicle.setUser(user);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        return vehicle;
    }

    private void sendFipeRequest(Vehicle vehicle, VehicleRequestDTO data) {
        vehicleFipeProducer.sendToFipeQueue(new VehicleFipeDTO(
                vehicle.getId(),
                data.brandId(),
                data.modelId(),
                data.yearFipeCode()
        ));
    }

    private void validateFipeData(String brandId, String modelId, String yearFipeCode) {
        fipeService.validateYear(brandId, modelId, yearFipeCode);
    }

    private Brand getOrCreateBrand(String brandId) {
        return brandRepository.findById(Long.valueOf(brandId))
                .orElseGet(() -> {
                    String name = fipeService.getBrandName(brandId);
                    return brandRepository.save(new Brand(brandId, name));
                });
    }

    private Model getOrCreateModel(String modelId, String brandId, Brand brand) {
        return modelRepository.findById(Long.valueOf(modelId))
                .orElseGet(() -> {
                    String name = fipeService.getModelName(brandId, modelId);
                    return modelRepository.save(new Model(modelId, name, brand));
                });
    }

    public List<VehicleResponseDTO> getAllVehicles() {
        return repository.findAll().stream().map(VehicleResponseDTO::new).toList();
    }

    public VehicleResponseDTO getVehicle(Long id) {
        Optional<Vehicle> vehicleOptional = repository.findById(id);
        Vehicle vehicle = vehicleOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, VEHICLE_NOT_FOUND));
        return new VehicleResponseDTO(vehicle);
    }

    @Transactional
    public VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO data) {
        Vehicle existingVehicle = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, VEHICLE_NOT_FOUND));

        findPlate(data.plate(), id);

        User user = null;
        if (Objects.nonNull(data.userId())) {
            user = userService.findUser(data.userId());
        }

        Brand brand = getOrCreateBrand(data.brandId());
        Model model = getOrCreateModel(data.modelId(), data.brandId(), brand);

        validateFipeData(data.brandId(), data.modelId(), data.yearFipeCode());

        existingVehicle.setPlate(data.plate());
        existingVehicle.setAdvertisedPrice(data.advertisedPrice());
        existingVehicle.setYear(data.year());
        existingVehicle.setUser(user);
        existingVehicle.setBrand(brand);
        existingVehicle.setModel(model);

        return new VehicleResponseDTO(repository.save(existingVehicle));
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
            throw new ResponseStatusException(HttpStatus.CONFLICT, VEHICLE_PLATE_ALREADY_EXISTS);
        }
    }

    public void findPlate(String plate, Long id) {
        repository.findByPlate(plate)
                .filter(v -> !v.getId().equals(id))
                .ifPresent(v -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, VEHICLE_PLATE_ALREADY_EXISTS);
                });
    }
}
