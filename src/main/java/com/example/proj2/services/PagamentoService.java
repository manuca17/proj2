package com.example.proj2.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.example.proj2.models.EncomendaCatalogo;
import com.example.proj2.models.Pagamento;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.repository.PagamentoRepository;
import com.example.proj2.repository.ProjetoPersonalizadoRepository;

@Service
public class PagamentoService {

    private static final List<String> FASES_VALIDAS = List.of("FASE_1", "FASE_2", "FASE_3");

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private ProjetoPersonalizadoRepository projetoRepository;

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

    /** Devolve todas as fases de pagamento de um projeto. */
    public List<Pagamento> findByProjeto(Integer projetoId) {
        ProjetoPersonalizado projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado."));
        return repository.findByIdProjeto(projeto);
    }

    /**
     * A artesã cria uma fase de pagamento para um projeto.
     * Cada projeto pode ter no máximo 3 fases (FASE_1, FASE_2, FASE_3).
     * Uma fase não pode ser criada se já existir para o mesmo projeto.
     */
    public Pagamento criarFaseProjeto(Integer projetoId, String fase, BigDecimal valor, String descricao) {
        if (!FASES_VALIDAS.contains(fase)) {
            throw new IllegalArgumentException("Fase inválida. Use: FASE_1, FASE_2 ou FASE_3.");
        }

        ProjetoPersonalizado projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado."));

        repository.findByIdProjetoAndFase(projeto, fase).ifPresent(p -> {
            throw new IllegalStateException("A " + fase + " já existe para este projeto.");
        });

        Pagamento pagamento = new Pagamento();
        pagamento.setIdProjeto(projeto);
        pagamento.setFase(fase);
        pagamento.setValor(valor);
        pagamento.setDescricao(descricao);
        pagamento.setPago(false);

        return save(pagamento);
    }

    /**
     * O cliente paga uma fase específica de um projeto.
     * Atualiza o tipo de pagamento e marca como pago.
     */
    public Pagamento pagarFase(Integer pagamentoId, String tipoPagamento) {
        Pagamento pagamento = findById(pagamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));

        if (pagamento.getIdProjeto() == null) {
            throw new IllegalStateException("Este pagamento não pertence a um projeto.");
        }
        if (Boolean.TRUE.equals(pagamento.getPago())) {
            throw new IllegalStateException("Esta fase já foi paga.");
        }

        pagamento.setTipoPagamento(tipoPagamento == null || tipoPagamento.isBlank() ? "A definir" : tipoPagamento);
        pagamento.setDataPagamento(Instant.now());
        pagamento.setPago(true);

        return save(pagamento);
    }

    /**
     * O cliente paga uma encomenda de catálogo.
     * Cria o registo de pagamento ligado à encomenda.
     */
    public Pagamento pagarEncomenda(EncomendaCatalogo encomenda, String tipoPagamento) {
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda inválida.");
        }

        repository.findByIdEncomenda(encomenda).ifPresent(p -> {
            if (Boolean.TRUE.equals(p.getPago())) {
                throw new IllegalStateException("Esta encomenda já foi paga.");
            }
        });

        Pagamento pagamento = repository.findByIdEncomenda(encomenda).orElseGet(Pagamento::new);
        pagamento.setIdEncomenda(encomenda);
        pagamento.setValor(encomenda.getValorFinal());
        pagamento.setTipoPagamento(tipoPagamento == null || tipoPagamento.isBlank() ? "A definir" : tipoPagamento);
        pagamento.setDataPagamento(Instant.now());
        pagamento.setPago(true);

        return save(pagamento);
    }

    public Pagamento markAsPaid(Integer id) {
        Pagamento p = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));
        p.setPago(true);
        return save(p);
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
