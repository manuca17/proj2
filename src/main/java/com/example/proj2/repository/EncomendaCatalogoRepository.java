package com.example.proj2.repository;

import com.example.proj2.models.EncomendaCatalogo;
import com.example.proj2.models.Utilizador;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface EncomendaCatalogoRepository extends JpaRepository<EncomendaCatalogo, Integer> {

    @Query("SELECT e FROM EncomendaCatalogo e WHERE e.idUtilizador = ?1 AND e.estado != 'carrinho'")
    List<EncomendaCatalogo> findByIdUtilizador(Utilizador idUtilizador);

    @Query("SELECT e FROM EncomendaCatalogo e WHERE e.idUtilizador.id = ?1 AND e.estado != 'carrinho'")
    List<EncomendaCatalogo> findByIdUtilizadorId(Integer utilizadorId);

    Optional<EncomendaCatalogo> findFirstByIdUtilizadorIdAndEstadoOrderByDataPedidoDesc(Integer utilizadorId, String estado);

    Optional<EncomendaCatalogo> findFirstByIdProjetoIdAndEstadoNotOrderByDataPedidoDesc(Integer projetoId, String estado);

    List<EncomendaCatalogo> findByEstado(String estado);

    @EntityGraph(attributePaths = {"idUtilizador", "idProjeto", "idProjeto.idUtilizador"})
    @Query("SELECT e FROM EncomendaCatalogo e WHERE e.estado = :estado")
    List<EncomendaCatalogo> findByEstadoWithRelations(@Param("estado") String estado);
}