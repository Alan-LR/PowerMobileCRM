package com.example.powermobilecrm.dto.users;

import com.example.powermobilecrm.dto.vehicle.VehicleResponseDTO;
import com.example.powermobilecrm.entity.users.User;
import com.example.powermobilecrm.entity.users.UserStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record UserResponseDTO (
        Long id,
        String name,
        String email,
        String phone,
        String cpf,
        String zipCode,
        String address,
        String number,
        String complement,
        UserStatus status,
        LocalDateTime createdAt,
        List<VehicleResponseDTO> vehicles
){
    public UserResponseDTO(User user){
        this(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getCpf(), user.getZipCode(), user.getAddress(),
        user.getNumber(), user.getComplement(), user.getStatus(), user.getCreatedAt(),user.getVehicles() != null
                        ? user.getVehicles().stream()
                        .map(VehicleResponseDTO::new)
                        .collect(Collectors.toList())
                        : List.of() );
    }
}
