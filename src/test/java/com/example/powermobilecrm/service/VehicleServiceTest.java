package com.example.powermobilecrm.service;

import com.example.powermobilecrm.dto.vehicle.VehicleRequestDTO;
import com.example.powermobilecrm.dto.vehicle.VehicleResponseDTO;
import com.example.powermobilecrm.entity.brand.Brand;
import com.example.powermobilecrm.entity.model.Model;
import com.example.powermobilecrm.entity.users.User;
import com.example.powermobilecrm.entity.vehicle.Vehicle;
import com.example.powermobilecrm.messaging.VehicleFipeProducer;
import com.example.powermobilecrm.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    private VehicleService vehicleService;
    private VehicleRepository vehicleRepository;
    private UserRepository userRepository;
    private VehicleFipeProducer vehicleFipeProducer;
    private FipeService fipeService;
    private BrandRepository brandRepository;
    private ModelRepository modelRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        vehicleRepository = mock(VehicleRepository.class);
        userRepository = mock(UserRepository.class);
        vehicleFipeProducer = mock(VehicleFipeProducer.class);
        fipeService = mock(FipeService.class);
        brandRepository = mock(BrandRepository.class);
        modelRepository = mock(ModelRepository.class);
        userService = mock(UserService.class);

        vehicleService = new VehicleService(vehicleRepository, vehicleFipeProducer,
                fipeService, brandRepository, modelRepository, userService);
    }

    @Test
    void shouldCreateVehicleSuccessfully() {
        VehicleRequestDTO dto = new VehicleRequestDTO(
                "ABC1234",
                BigDecimal.TEN,
                2020,
                1L,
                "1",
                "1",
                "2020-1"
        );

        User mockUser = new User();
        Brand mockBrand = new Brand("1", "Fiat");
        Model mockModel = new Model("1", "Uno", mockBrand);
        Vehicle savedVehicle = new Vehicle(dto);
        savedVehicle.setId(1L);
        savedVehicle.setUser(mockUser);
        savedVehicle.setBrand(mockBrand);
        savedVehicle.setModel(mockModel);

        when(vehicleRepository.existsByPlate("ABC1234")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());
        when(brandRepository.save(any())).thenReturn(mockBrand);
        when(modelRepository.findById(1L)).thenReturn(Optional.empty());
        when(modelRepository.save(any())).thenReturn(mockModel);
        doNothing().when(fipeService).validateYear("1", "1", "2020-1");
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(savedVehicle);

        VehicleResponseDTO result = vehicleService.createVehicle(dto);

        assertEquals("ABC1234", result.plate());
        verify(vehicleRepository).save(any());
        verify(vehicleFipeProducer).sendToFipeQueue(any());
    }

    @Test
    void shouldThrowExceptionIfPlateAlreadyExists() {
        String plate = "ABC1234";
        VehicleRequestDTO dto = new VehicleRequestDTO(
                plate,
                BigDecimal.TEN,
                2020,
                null,
                "1",
                "1",
                "2020-1"
        );

        when(vehicleRepository.existsByPlate(plate)).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> vehicleService.createVehicle(dto));
    }

    @Test
    void shouldReturnVehicleById() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        VehicleResponseDTO result = vehicleService.getVehicle(1L);
        assertEquals(1L, result.id());
    }

    @Test
    void shouldThrowWhenVehicleNotFoundById() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> vehicleService.getVehicle(1L));
    }

    @Test
    void shouldReturnTrueWhenVehicleDeleted() {
        when(vehicleRepository.existsById(1L)).thenReturn(true);
        Boolean result = vehicleService.deleteVehicle(1L);
        assertTrue(result);
        verify(vehicleRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldReturnFalseWhenVehicleNotFoundToDelete() {
        when(vehicleRepository.existsById(1L)).thenReturn(false);
        Boolean result = vehicleService.deleteVehicle(1L);
        assertFalse(result);
    }

    @Test
    void shouldUpdateVehicleSuccessfully() {
        Long vehicleId = 1L;
        VehicleRequestDTO dto = new VehicleRequestDTO("XYZ1234", BigDecimal.valueOf(50000), 2021,
                2L, "10", "20", "2021-1");

        User user = new User();
        user.setId(2L);

        Brand brand = new Brand("10", "Toyota");
        Model model = new Model("20", "Corolla", brand);

        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setId(vehicleId);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.findByPlate(dto.plate())).thenReturn(Optional.empty()); // corrigido aqui
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(brandRepository.findById(10L)).thenReturn(Optional.empty());
        when(brandRepository.save(any())).thenReturn(brand);
        when(modelRepository.findById(20L)).thenReturn(Optional.empty());
        when(modelRepository.save(any())).thenReturn(model);
        doNothing().when(fipeService).validateYear("10", "20", "2021-1");
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VehicleResponseDTO result = vehicleService.updateVehicle(vehicleId, dto);

        assertEquals("XYZ1234", result.plate());
        assertEquals(BigDecimal.valueOf(50000), result.advertisedPrice());
        assertEquals(2021, result.year());
        verify(vehicleRepository).save(any());
    }
}
