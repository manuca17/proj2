package com.example.proj2.repository;

import com.example.proj2.models.ArtigoCatalogo;
import com.example.proj2.models.FichaTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ArtigoCatalogoRepository extends JpaRepository<ArtigoCatalogo, Integer> {

    @Query("SELECT a FROM ArtigoCatalogo a WHERE a.visivel = true")
    List<ArtigoCatalogo> findByVisivelTrue();

    @Query("SELECT a FROM ArtigoCatalogo a LEFT JOIN FETCH a.idFichaTecnica WHERE a.id = ?1")
    Optional<ArtigoCatalogo> findByIdWithFichaTecnica(Integer id);
}
