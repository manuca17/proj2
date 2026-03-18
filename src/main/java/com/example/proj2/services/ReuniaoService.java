package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.proj2.models.Reuniao;
import com.example.proj2.repository.ReuniaoRepository;
import com.example.proj2.models.Artesa;
import com.example.proj2.models.ProjetoPersonalizado;

@Service
public class ReuniaoService {

    @Autowired
    private ReuniaoRepository repository;

    public List<Reuniao> findAll() {
        return repository.findAll();
    }

    public Optional<Reuniao> findById(Integer id) {
        return repository.findById(id);
    }

    public Reuniao save(Reuniao reuniao) {
        return repository.save(reuniao);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<Reuniao> findByIdProjeto(ProjetoPersonalizado projeto) {
        return repository.findByIdProjeto(projeto);
    }

    public List<Reuniao> findByIdArtesa(Artesa artesao) {
        return repository.findByIdArtesa(artesao);
    }

    public Reuniao updateStatus(Integer id, String status) {
        Optional<Reuniao> existing = findById(id);
        if (existing.isPresent()) {
            Reuniao r = existing.get();
            r.setStatus(status);
            return save(r);
        }
        return null;
    }

    public Reuniao scheduleMeeting(Reuniao reuniao) {
        // Additional logic for scheduling
        return save(reuniao);
    }
}