package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import com.example.proj2.models.Utilizador;
import com.example.proj2.repository.UtilizadorRepository;

@Service
public class UtilizadorService {

    @Autowired
    private UtilizadorRepository repository;

    public List<Utilizador> findAll() {
        return repository.findAll();
    }

    public Optional<Utilizador> findById(Integer id) {
        return repository.findById(id);
    }

    public Utilizador save(Utilizador utilizador) {
        validateUtilizador(utilizador);
        utilizador.setEmail(normalizeEmail(utilizador.getEmail()));
        return repository.save(utilizador);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Optional<Utilizador> findByEmail(String email) {
        return repository.findByEmail(normalizeEmail(email));
    }

    public boolean authenticate(String email, String password) {
        Optional<Utilizador> user = findByEmail(email);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    public Utilizador updateProfile(Integer id, Utilizador updated) {
        Optional<Utilizador> existing = findById(id);
        if (existing.isPresent()) {
            Utilizador u = existing.get();
            u.setNomeEmpresa(updated.getNomeEmpresa());
            u.setNif(updated.getNif());
            u.setEmail(updated.getEmail());
            u.setPassword(updated.getPassword());
            u.setTelefone(updated.getTelefone());
            u.setMoradaFaturacao(updated.getMoradaFaturacao());
            return save(u);
        }
        return null;
    }

    private void validateUtilizador(Utilizador utilizador) {
        if (utilizador == null) {
            throw new IllegalArgumentException("Utilizador inválido.");
        }

        String email = normalizeEmail(utilizador.getEmail());
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O email do utilizador é obrigatório.");
        }

        repository.findByEmail(email)
            .filter(existing -> !Objects.equals(existing.getId(), utilizador.getId()))
            .ifPresent(existing -> {
                throw new IllegalArgumentException("Já existe um utilizador com esse email.");
            });

        String nif = utilizador.getNif();
        if (nif == null || nif.isBlank()) {
            throw new IllegalArgumentException("O NIF do utilizador é obrigatório.");
        }

        if (nif.trim().length() > 9) {
            throw new IllegalArgumentException("O NIF do utilizador deve ter no máximo 9 caracteres.");
        }

        utilizador.setNif(nif.trim());
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}