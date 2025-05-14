package com.example.powermobilecrm.dto.users;

import com.example.powermobilecrm.entity.users.User;
import com.example.powermobilecrm.entity.users.UserStatus;

import java.time.LocalDateTime;

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
        LocalDateTime createdAt
){
    public UserResponseDTO(User user){
        this(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getCpf(), user.getZipCode(), user.getAddress(),
        user.getNumber(), user.getComplement(), user.getStatus(), user.getCreatedAt());
    }
}
