package com.example.proj2.repository;

import com.example.proj2.models.Orcamento;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.models.Artesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Integer> {

    @Query("SELECT o FROM Orcamento o WHERE o.idProjeto = ?1")
    List<Orcamento> findByIdProjeto(ProjetoPersonalizado projeto);

    @Query("SELECT o FROM Orcamento o WHERE o.idArtesa = ?1")
    List<Orcamento> findByIdArtesa(Artesa artesao);
}