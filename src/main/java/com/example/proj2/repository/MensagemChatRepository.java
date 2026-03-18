package com.example.proj2.repository;

import com.example.proj2.models.MensagemChat;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.models.Utilizador;
import com.example.proj2.models.Artesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MensagemChatRepository extends JpaRepository<MensagemChat, Integer> {

    @Query("SELECT m FROM MensagemChat m WHERE m.idProjeto = ?1")
    List<MensagemChat> findByIdProjeto(ProjetoPersonalizado projeto);

    @Query("SELECT m FROM MensagemChat m WHERE m.idRemetenteUtilizador = ?1")
    List<MensagemChat> findByIdRemetenteUtilizador(Utilizador utilizador);

    @Query("SELECT m FROM MensagemChat m WHERE m.idRemetenteArtesa = ?1")
    List<MensagemChat> findByIdRemetenteArtesa(Artesa artesao);
}