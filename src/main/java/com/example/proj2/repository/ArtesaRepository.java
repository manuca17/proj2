package com.example.proj2.repository;

import com.example.proj2.models.Artesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ArtesaRepository extends JpaRepository<Artesa, Integer> {

    @Query("SELECT a FROM Artesa a WHERE a.email = ?1")
    Optional<Artesa> findByEmail(String email);
}