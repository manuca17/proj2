package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.proj2.models.ArtigoCatalogo;
import com.example.proj2.repository.ArtigoCatalogoRepository;

@Service
public class ArtigoCatalogoService {

    @Autowired
    private ArtigoCatalogoRepository repository;

    public List<ArtigoCatalogo> findAll() {
        return repository.findAll();
    }

    public Optional<ArtigoCatalogo> findById(Integer id) {
        return repository.findById(id);
    }

    public ArtigoCatalogo save(ArtigoCatalogo artigo) {
        validateArtigo(artigo);
        return repository.save(artigo);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<ArtigoCatalogo> findByVisivelTrue() {
        return repository.findByVisivelTrue();
    }


    public ArtigoCatalogo toggleVisibility(Integer id) {
        Optional<ArtigoCatalogo> existing = findById(id);
        if (existing.isPresent()) {
            ArtigoCatalogo a = existing.get();
            a.setVisivel(!a.getVisivel());
            return save(a);
        }
        return null;
    }

    private void validateArtigo(ArtigoCatalogo artigo) {
        if (artigo == null) {
            throw new IllegalArgumentException("Artigo inválido.");
        }

        if (artigo.getStock() == null) {
            artigo.setStock(0);
        }

        if (artigo.getStock() < 0) {
            throw new IllegalArgumentException("O stock do artigo deve ser maior ou igual a 0.");
        }
    }
}