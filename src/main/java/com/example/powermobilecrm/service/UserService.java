package com.example.powermobilecrm.service;

import com.example.powermobilecrm.dto.users.UserRequestDTO;
import com.example.powermobilecrm.dto.users.UserResponseDTO;
import com.example.powermobilecrm.entity.users.User;
import com.example.powermobilecrm.entity.users.UserStatus;
import com.example.powermobilecrm.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    public static final String USER_NOT_FOUND = "Usuário não encontrado";
    public static final String STATUS_NOT_FOUND = "Status inválido. Use ACTIVE ou INACTIVE.";


    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO data) {
        User user = new User(data);
        user.setStatus(UserStatus.ACTIVE);
        return new UserResponseDTO(repository.save(user));
    }

    public List<UserResponseDTO> getAllUsers() {
        return repository.findAll().stream().map(UserResponseDTO::new).toList();
    }

    public UserResponseDTO getUser(Long id){
        Optional<User> userOptional = repository.findById(id);
        User user = userOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
        return new UserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO data) {
        return repository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(data.name());
                    existingUser.setEmail(data.email());
                    existingUser.setPhone(data.phone());
                    existingUser.setCpf(data.cpf());
                    existingUser.setZipCode(data.zipCode());
                    existingUser.setAddress(data.address());
                    existingUser.setNumber(data.number());
                    existingUser.setComplement(data.complement());

                    if (Objects.nonNull(data.status())) {
                        existingUser.setStatus(parseStatus(data.status()));
                    }

                    return new UserResponseDTO(repository.save(existingUser));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
    }

    private UserStatus parseStatus(String status) {
        try {
            return UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, STATUS_NOT_FOUND);
        }
    }

    public boolean deleteUser(Long id){
        if(repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<UserResponseDTO> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findByCreatedAtBetween(start, end).stream()
                .map(UserResponseDTO::new)
                .toList();
    }

    public User findUser(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
    }
}
