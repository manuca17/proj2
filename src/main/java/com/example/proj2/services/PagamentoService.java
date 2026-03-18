package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.example.proj2.models.Pagamento;
import com.example.proj2.repository.PagamentoRepository;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    public List<Pagamento> findAll() {
        return repository.findAll();
    }

    public Optional<Pagamento> findById(Integer id) {
        return repository.findById(id);
    }

    public Pagamento save(Pagamento pagamento) {
        validatePagamento(pagamento);
        return repository.save(pagamento);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }


    public Pagamento markAsPaid(Integer id) {
        Optional<Pagamento> existing = findById(id);
        if (existing.isPresent()) {
            Pagamento p = existing.get();
            p.setPago(true);
            return save(p);
        }
        return null;
    }

    public Pagamento processPayment(Pagamento pagamento) {
        return save(pagamento);
    }

    private void validatePagamento(Pagamento pagamento) {
        if (pagamento == null) {
            throw new IllegalArgumentException("Pagamento inválido.");
        }

        BigDecimal valor = pagamento.getValor();
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser positivo.");
        }
    }
}