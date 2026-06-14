package com.example.proj2.repository;

import com.example.proj2.models.Reuniao;
import com.example.proj2.models.ProjetoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ReuniaoRepository extends JpaRepository<Reuniao, Integer> {

    @Query("SELECT r FROM Reuniao r WHERE r.idProjeto = ?1")
    List<Reuniao> findByIdProjeto(ProjetoPersonalizado projeto);

    @Query("SELECT r FROM Reuniao r LEFT JOIN FETCH r.idProjeto")
    List<Reuniao> findAllWithRelations();

    @Query("SELECT r FROM Reuniao r WHERE r.idProjeto.id = ?1")
    List<Reuniao> findByIdProjetoId(Integer projetoId);

    @Query("SELECT r FROM Reuniao r LEFT JOIN FETCH r.idProjeto LEFT JOIN FETCH r.idProjeto.idArtesa WHERE r.idProjeto.id = ?1")
    List<Reuniao> findByIdProjetoIdWithRelations(Integer projetoId);

    @Query("SELECT r FROM Reuniao r LEFT JOIN FETCH r.idProjeto LEFT JOIN FETCH r.idProjeto.idArtesa WHERE r.id = ?1")
    Optional<Reuniao> findByIdWithRelations(Integer id);
}
