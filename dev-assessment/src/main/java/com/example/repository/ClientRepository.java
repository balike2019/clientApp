package com.example.repository;

import com.example.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByIdNumber(String idNumber);
    Optional<Client> findByMobileNumber(String mobileNumber);
    List<Client> findByFirstNameContainingOrIdNumberContainingOrMobileNumberContaining(
            String firstName, String idNumber, String mobileNumber);
    Boolean existsByMobileNumber(String mobileNumber);
    Boolean existsByIdNumber(String idNumber);
}
