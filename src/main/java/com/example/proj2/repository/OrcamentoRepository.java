package com.example.proj2.repository;

import com.example.proj2.models.Orcamento;
import com.example.proj2.models.ProjetoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Integer> {
    List<Orcamento> findByIdProjeto(ProjetoPersonalizado projeto);
}
