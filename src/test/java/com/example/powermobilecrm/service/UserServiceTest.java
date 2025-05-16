package com.example.powermobilecrm.service;

import com.example.powermobilecrm.dto.users.UserRequestDTO;
import com.example.powermobilecrm.dto.users.UserResponseDTO;
import com.example.powermobilecrm.entity.users.User;
import com.example.powermobilecrm.entity.users.UserStatus;
import com.example.powermobilecrm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void shouldCreateUserWithActiveStatus() {
        UserRequestDTO dto = new UserRequestDTO("João", "joao@email.com", "11999999999",
                "12345678901", "01000-000", "Rua A", "123", "Apto 1", "ACTIVE");

        User savedUser = new User(dto);
        savedUser.setId(1L);
        savedUser.setStatus(UserStatus.ACTIVE);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.createUser(dto);

        assertEquals("João", result.name());
        assertEquals(UserStatus.ACTIVE, result.status());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldReturnUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("Maria");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUser(1L);

        assertEquals("Maria", result.name());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUser(99L));
    }

    @Test
    void shouldUpdateUser() {
        User existingUser = new User();
        existingUser.setId(1L);

        UserRequestDTO dto = new UserRequestDTO("Novo Nome", "novo@email.com", "111",
                "00000000000", "12345-678", "Rua Nova", "42", "Casa", "INACTIVE");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponseDTO result = userService.updateUser(1L, dto);

        assertEquals("Novo Nome", result.name());
        assertEquals(UserStatus.INACTIVE, result.status());
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistentUser() {
        when(userRepository.existsById(999L)).thenReturn(false);
        assertFalse(userService.deleteUser(999L));
    }

    @Test
    void shouldReturnTrueWhenDeletingExistingUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertTrue(userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldFindUsersBetweenDates() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();

        User user = new User();
        user.setName("Fulano");

        when(userRepository.findByCreatedAtBetween(start, end))
                .thenReturn(List.of(user));

        List<UserResponseDTO> result = userService.findByCreatedAtBetween(start, end);

        assertEquals(1, result.size());
        assertEquals("Fulano", result.get(0).name());
    }
}
