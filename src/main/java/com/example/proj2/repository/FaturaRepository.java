package com.example.proj2.repository;

import com.example.proj2.models.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FaturaRepository extends JpaRepository<Fatura, Integer> {

    @Query("SELECT f FROM Fatura f LEFT JOIN FETCH f.idEncomenda WHERE f.idEncomenda.id = ?1")
    Optional<Fatura> findByIdEncomendaId(Integer encomendaId);

    Optional<Fatura> findByIdPagamentoId(Integer pagamentoId);
}
