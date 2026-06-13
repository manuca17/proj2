package com.example.proj2.services;

import com.example.proj2.models.EncomendaCatalogo;
import com.example.proj2.models.Fatura;
import com.example.proj2.models.Pagamento;
import com.example.proj2.repository.EncomendaCatalogoRepository;
import com.example.proj2.repository.FaturaRepository;
import com.example.proj2.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.Year;
import java.util.Optional;

@Service
public class FaturaService {

    @Autowired
    private FaturaRepository faturaRepository;

    @Autowired
    private EncomendaCatalogoRepository encomendaRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    public Optional<Fatura> findByEncomendaId(Integer encomendaId) {
        return faturaRepository.findByIdEncomendaId(encomendaId);
    }

    @Transactional
    public Fatura createOrUpdate(Integer encomendaId, String nome, String nif, String morada,
                                  String cidade, String codigoPostal, String telefone, String metodoPagamento) {
        EncomendaCatalogo encomenda = encomendaRepository.findById(encomendaId)
            .orElseThrow(() -> new IllegalArgumentException("Encomenda não encontrada."));

        Fatura fatura = faturaRepository.findByIdEncomendaId(encomendaId).orElse(new Fatura());
        boolean isNew = fatura.getId() == null;

        fatura.setIdEncomenda(encomenda);
        fatura.setNome(nome);
        fatura.setNif(nif);
        fatura.setMorada(morada);
        fatura.setCidade(cidade);
        fatura.setCodigoPostal(codigoPostal);
        fatura.setTelefone(telefone);
        fatura.setMetodoPagamento(metodoPagamento);
        fatura.setDataEmissao(Instant.now());

        Fatura saved = faturaRepository.save(fatura);

        if (isNew) {
            String numero = String.format("FAT-%d-%04d", Year.now().getValue(), saved.getId());
            saved.setNumeroFatura(numero);
            saved = faturaRepository.save(saved);
        }

        return saved;
    }

    public Optional<Fatura> findByPagamentoId(Integer pagamentoId) {
        return faturaRepository.findByIdPagamentoId(pagamentoId);
    }

    @Transactional
    public Fatura createOrUpdateForPagamento(Integer pagamentoId, String nome, String nif, String morada,
            String cidade, String codigoPostal, String telefone, String metodoPagamento) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
            .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));

        Fatura fatura = faturaRepository.findByIdPagamentoId(pagamentoId).orElse(new Fatura());
        boolean isNew = fatura.getId() == null;

        fatura.setIdPagamento(pagamento);
        fatura.setNome(nome);
        fatura.setNif(nif != null ? nif : "");
        fatura.setMorada(morada);
        fatura.setCidade(cidade);
        fatura.setCodigoPostal(codigoPostal);
        fatura.setTelefone(telefone != null ? telefone : "");
        fatura.setMetodoPagamento(metodoPagamento);
        fatura.setDataEmissao(Instant.now());

        Fatura saved = faturaRepository.save(fatura);

        if (isNew) {
            String numero = String.format("FAT-PROJ-%d-%04d", Year.now().getValue(), saved.getId());
            saved.setNumeroFatura(numero);
            saved = faturaRepository.save(saved);
        }

        return saved;
    }
}
