package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import com.example.proj2.models.Artesa;
import com.example.proj2.repository.ArtesaRepository;

@Service
public class ArtesaService {

    @Autowired
    private ArtesaRepository repository;

    public List<Artesa> findAll() {
        return repository.findAll();
    }

    public Optional<Artesa> findById(Integer id) {
        return repository.findById(id);
    }

    public Artesa save(Artesa artesao) {
        validateArtesa(artesao);
        artesao.setEmail(normalizeEmail(artesao.getEmail()));
        return repository.save(artesao);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Optional<Artesa> findByEmail(String email) {
        return repository.findByEmail(normalizeEmail(email));
    }

    private void validateArtesa(Artesa artesao) {
        if (artesao == null) {
            throw new IllegalArgumentException("Artesã inválida.");
        }

        String email = normalizeEmail(artesao.getEmail());
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O email da artesã é obrigatório.");
        }

        repository.findByEmail(email)
            .filter(existing -> !Objects.equals(existing.getId(), artesao.getId()))
            .ifPresent(existing -> {
                throw new IllegalArgumentException("Já existe uma artesã com esse email.");
            });
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

}