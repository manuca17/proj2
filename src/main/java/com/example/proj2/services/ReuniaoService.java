package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.proj2.models.Reuniao;
import com.example.proj2.repository.ReuniaoRepository;
import com.example.proj2.models.ProjetoPersonalizado;

@Service
public class ReuniaoService {

    @Autowired
    private ReuniaoRepository repository;

    public List<Reuniao> findAll() {
        return repository.findAllWithRelations();
    }

    public Optional<Reuniao> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Reuniao> findByIdWithRelations(Integer id) {
        return repository.findByIdWithRelations(id);
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

    public List<Reuniao> findByProjetoId(Integer projetoId) {
        return repository.findByIdProjetoIdWithRelations(projetoId);
    }

    public Reuniao updateStatus(Integer id, String status) {
        Optional<Reuniao> existing = findById(id);
        if (existing.isPresent()) {
            Reuniao r = existing.get();
            r.setStatus(status);
            Reuniao saved = save(r);
            return findByIdWithRelations(saved.getId()).orElse(saved);
        }
        return null;
    }

    public Reuniao reschedule(Integer id, Reuniao changes) {
        Optional<Reuniao> existing = findById(id);
        if (existing.isPresent()) {
            Reuniao r = existing.get();
            r.setDataHora(changes.getDataHora());
            if (changes.getLocal() != null) {
                r.setLocal(changes.getLocal());
            }
            if (changes.getTipo() != null) {
                r.setTipo(changes.getTipo());
            }
            r.setStatus("remarcada");
            Reuniao saved = save(r);
            return findByIdWithRelations(saved.getId()).orElse(saved);
        }
        return null;
    }

    public Reuniao scheduleMeeting(Reuniao reuniao) {
        Reuniao saved = save(reuniao);
        return findByIdWithRelations(saved.getId()).orElse(saved);
    }
}