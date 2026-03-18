package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.proj2.models.MensagemChat;
import com.example.proj2.repository.MensagemChatRepository;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.models.Utilizador;
import com.example.proj2.models.Artesa;

@Service
public class MensagemChatService {

    @Autowired
    private MensagemChatRepository repository;

    public List<MensagemChat> findAll() {
        return repository.findAll();
    }

    public Optional<MensagemChat> findById(Integer id) {
        return repository.findById(id);
    }

    public MensagemChat save(MensagemChat mensagem) {
        return repository.save(mensagem);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<MensagemChat> findByIdProjeto(ProjetoPersonalizado projeto) {
        return repository.findByIdProjeto(projeto);
    }

    public List<MensagemChat> findByIdRemetenteUtilizador(Utilizador utilizador) {
        return repository.findByIdRemetenteUtilizador(utilizador);
    }

    public List<MensagemChat> findByIdRemetenteArtesa(Artesa artesao) {
        return repository.findByIdRemetenteArtesa(artesao);
    }

    public MensagemChat sendMessage(MensagemChat mensagem) {
        return save(mensagem);
    }
}