package com.example.proj2.repository;

import com.example.proj2.models.Orcamento;
import com.example.proj2.models.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

	@Query("SELECT p FROM Pagamento p LEFT JOIN FETCH p.idOrcamento LEFT JOIN FETCH p.idEncomenda")
	List<Pagamento> findAllWithRelations();

	@Query("SELECT p FROM Pagamento p LEFT JOIN FETCH p.idOrcamento LEFT JOIN FETCH p.idEncomenda WHERE p.id = ?1")
	Optional<Pagamento> findByIdWithRelations(Integer id);

	Optional<Pagamento> findFirstByIdOrcamentoOrderByIdDesc(Orcamento orcamento);

}