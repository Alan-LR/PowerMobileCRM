package com.example.powermobilecrm.repository;

import com.example.powermobilecrm.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
