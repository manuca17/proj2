package com.example.proj2.repository;

import com.example.proj2.models.ItemEncomenda;
import com.example.proj2.models.EncomendaCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ItemEncomendaRepository extends JpaRepository<ItemEncomenda, Integer> {

    @Query("SELECT i FROM ItemEncomenda i WHERE i.idEncomenda = ?1")
    List<ItemEncomenda> findByIdEncomenda(EncomendaCatalogo encomenda);

    @Query("SELECT i FROM ItemEncomenda i WHERE i.idEncomenda.id = ?1")
    List<ItemEncomenda> findByIdEncomendaId(Integer encomendaId);

    @Query("SELECT i FROM ItemEncomenda i JOIN FETCH i.idArtigo WHERE i.idEncomenda.id = ?1")
    List<ItemEncomenda> findByIdEncomendaIdWithArtigo(Integer encomendaId);

    @Query("SELECT i FROM ItemEncomenda i JOIN FETCH i.idArtigo WHERE i.idEncomenda = ?1")
    List<ItemEncomenda> findByIdEncomendaWithArtigo(EncomendaCatalogo encomenda);
}