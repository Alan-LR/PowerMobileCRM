package com.example.powermobilecrm.repository;

import com.example.powermobilecrm.entity.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByPlate(String plate);

    Optional<Vehicle> findByPlate(String plate);
}
