package com.example.proj2.repository;

import com.example.proj2.models.FichaTecnica;
import com.example.proj2.models.ProjetoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FichaTecnicaRepository extends JpaRepository<FichaTecnica, Integer> {

    @Query("SELECT f FROM FichaTecnica f WHERE f.idProjeto = ?1")
    List<FichaTecnica> findByIdProjeto(ProjetoPersonalizado idProjeto);
}