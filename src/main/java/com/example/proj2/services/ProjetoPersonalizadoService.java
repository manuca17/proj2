package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.repository.ProjetoPersonalizadoRepository;
import com.example.proj2.models.Utilizador;
import com.example.proj2.models.Artesa;

@Service
public class ProjetoPersonalizadoService {

    @Autowired
    private ProjetoPersonalizadoRepository repository;

    public List<ProjetoPersonalizado> findAll() {
        return repository.findAll();
    }

    public Optional<ProjetoPersonalizado> findById(Integer id) {
        return repository.findById(id);
    }

    public ProjetoPersonalizado save(ProjetoPersonalizado projeto) {
        return repository.save(projeto);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<ProjetoPersonalizado> findByIdUtilizador(Utilizador utilizador) {
        return repository.findByIdUtilizador(utilizador);
    }

    public List<ProjetoPersonalizado> findByIdArtesa(Artesa artesao) {
        return repository.findByIdArtesa(artesao);
    }

    public ProjetoPersonalizado updateEstado(Integer id, String estado) {
        Optional<ProjetoPersonalizado> existing = findById(id);
        if (existing.isPresent()) {
            ProjetoPersonalizado p = existing.get();
            p.setEstadoAtual(estado);
            return save(p);
        }
        return null;
    }

    public ProjetoPersonalizado assignArtesa(Integer id,Artesa artesao) {
        Optional<ProjetoPersonalizado> existing = findById(id);
        if (existing.isPresent()) {
            ProjetoPersonalizado p = existing.get();
            p.setIdArtesa(artesao);
            return save(p);
        }
        return null;
    }
}