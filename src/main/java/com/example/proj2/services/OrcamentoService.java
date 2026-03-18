package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.example.proj2.models.Orcamento;
import com.example.proj2.repository.OrcamentoRepository;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.models.Artesa;

@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository repository;

    public List<Orcamento> findAll() {
        return repository.findAll();
    }

    public Optional<Orcamento> findById(Integer id) {
        return repository.findById(id);
    }

    public Orcamento save(Orcamento orcamento) {
        validateOrcamento(orcamento);
        return repository.save(orcamento);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<Orcamento> findByIdProjeto(ProjetoPersonalizado projeto) {
        return repository.findByIdProjeto(projeto);
    }

    public List<Orcamento> findByIdArtesa(Artesa artesao) {
        return repository.findByIdArtesa(artesao);
    }

    public Orcamento updateEstado(Integer id, String estado) {
        Optional<Orcamento> existing = findById(id);
        if (existing.isPresent()) {
            Orcamento o = existing.get();
            o.setEstado(estado);
            return save(o);
        }
        return null;
    }

    public Orcamento approve(Integer id) {
        return updateEstado(id, "aprovado");
    }

    public Orcamento reject(Integer id) {
        return updateEstado(id, "rejeitado");
    }

    private void validateOrcamento(Orcamento orcamento) {
        if (orcamento == null) {
            throw new IllegalArgumentException("Orçamento inválido.");
        }

        BigDecimal valor = orcamento.getValorTotalEstimado();
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do orçamento deve ser positivo.");
        }
    }
}