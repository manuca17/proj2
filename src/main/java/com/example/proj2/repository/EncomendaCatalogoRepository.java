package com.example.proj2.repository;

import com.example.proj2.models.EncomendaCatalogo;
import com.example.proj2.models.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface EncomendaCatalogoRepository extends JpaRepository<EncomendaCatalogo, Integer> {

    @Query("SELECT e FROM EncomendaCatalogo e WHERE e.idUtilizador = ?1")
    List<EncomendaCatalogo> findByIdUtilizador(Utilizador idUtilizador);

    @Query("SELECT e FROM EncomendaCatalogo e WHERE e.idUtilizador.id = ?1")
    List<EncomendaCatalogo> findByIdUtilizadorId(Integer utilizadorId);

    @Query("SELECT e FROM EncomendaCatalogo e WHERE e.idUtilizador.id = ?1 AND e.estado = 'carrinho'")
    java.util.Optional<EncomendaCatalogo> findCarrinhoByIdUtilizadorId(Integer utilizadorId);

    Optional<EncomendaCatalogo> findFirstByIdProjetoIdAndEstadoNotOrderByDataPedidoDesc(Integer projetoId, String estado);
}