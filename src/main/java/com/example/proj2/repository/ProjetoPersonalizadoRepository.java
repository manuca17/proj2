package com.example.proj2.repository;

import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.models.Utilizador;
import com.example.proj2.models.Artesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProjetoPersonalizadoRepository extends JpaRepository<ProjetoPersonalizado, Integer> {

    @Query("SELECT p FROM ProjetoPersonalizado p WHERE p.idUtilizador = ?1")
    List<ProjetoPersonalizado> findByIdUtilizador(Utilizador utilizador);

    @Query("SELECT p FROM ProjetoPersonalizado p WHERE p.idUtilizador.id = ?1")
    List<ProjetoPersonalizado> findByIdUtilizadorId(Integer utilizadorId);

    @Query("SELECT p FROM ProjetoPersonalizado p WHERE p.idArtesa = ?1")
    List<ProjetoPersonalizado> findByIdArtesa(Artesa artesao);

    @Query("SELECT p FROM ProjetoPersonalizado p LEFT JOIN FETCH p.idUtilizador LEFT JOIN FETCH p.idArtesa")
    List<ProjetoPersonalizado> findAllWithRelations();

    @Query("SELECT p FROM ProjetoPersonalizado p LEFT JOIN FETCH p.idUtilizador LEFT JOIN FETCH p.idArtesa WHERE p.id = ?1")
    java.util.Optional<ProjetoPersonalizado> findByIdWithRelations(Integer id);
}