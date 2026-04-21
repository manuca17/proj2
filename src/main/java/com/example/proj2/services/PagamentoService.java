package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import com.example.proj2.models.Orcamento;
import com.example.proj2.models.Pagamento;
import com.example.proj2.repository.PagamentoRepository;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    public List<Pagamento> findAll() {
        return repository.findAllWithRelations();
    }

    public Optional<Pagamento> findById(Integer id) {
        return repository.findByIdWithRelations(id);
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

    public Pagamento processOrcamentoPayment(Orcamento orcamento, String tipoPagamento) {
        if (orcamento == null) {
            throw new IllegalArgumentException("Orçamento inválido.");
        }

        BigDecimal valor = orcamento.getValorTotalEstimado();
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O orçamento não tem valor válido para pagamento.");
        }

        Optional<Pagamento> existing = repository.findFirstByIdOrcamentoOrderByIdDesc(orcamento);
        Pagamento pagamento = existing.orElseGet(Pagamento::new);

        if (Boolean.TRUE.equals(pagamento.getPago())) {
            throw new IllegalStateException("Este orçamento já foi pago.");
        }

        pagamento.setIdOrcamento(orcamento);
        pagamento.setValor(valor);
        pagamento.setTipoPagamento((tipoPagamento == null || tipoPagamento.isBlank()) ? "A definir" : tipoPagamento);
        pagamento.setDataPagamento(Instant.now());
        pagamento.setPago(true);

        Pagamento saved = save(pagamento);
        return findById(saved.getId()).orElse(saved);
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