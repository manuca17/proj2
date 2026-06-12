package com.example.proj2.repository;

import com.example.proj2.models.EncomendaCatalogo;
import com.example.proj2.models.Pagamento;
import com.example.proj2.models.ProjetoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    @Query("SELECT p FROM Pagamento p LEFT JOIN FETCH p.idProjeto LEFT JOIN FETCH p.idEncomenda")
    List<Pagamento> findAllWithRelations();

    @Query("SELECT p FROM Pagamento p LEFT JOIN FETCH p.idProjeto LEFT JOIN FETCH p.idEncomenda WHERE p.id = ?1")
    Optional<Pagamento> findByIdWithRelations(Integer id);

    @Query("SELECT p FROM Pagamento p WHERE p.idProjeto = ?1 ORDER BY p.fase ASC")
    List<Pagamento> findByIdProjeto(ProjetoPersonalizado projeto);

    @Query("SELECT p FROM Pagamento p WHERE p.idProjeto = ?1 AND p.fase = ?2")
    Optional<Pagamento> findByIdProjetoAndFase(ProjetoPersonalizado projeto, String fase);

    @Query("SELECT p FROM Pagamento p WHERE p.idEncomenda = ?1")
    Optional<Pagamento> findByIdEncomenda(EncomendaCatalogo encomenda);
}
