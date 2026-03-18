package com.example.proj2.repository;

import com.example.proj2.models.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UtilizadorRepository extends JpaRepository<Utilizador, Integer> {

    @Query("SELECT u FROM Utilizador u WHERE u.email = ?1")
    Optional<Utilizador> findByEmail(String email);
}