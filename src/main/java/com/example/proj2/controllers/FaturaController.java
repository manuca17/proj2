package com.example.proj2.controllers;

import com.example.proj2.models.Fatura;
import com.example.proj2.services.FaturaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/faturas")
public class FaturaController {

    private final FaturaService faturaService;

    public FaturaController(FaturaService faturaService) {
        this.faturaService = faturaService;
    }

    // GET /api/faturas/encomenda/{id}
    @GetMapping("/encomenda/{encomendaId}")
    public ResponseEntity<?> getByEncomenda(@PathVariable Integer encomendaId) {
        return faturaService.findByEncomendaId(encomendaId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/faturas/pagamento/{pagamentoId}
    @GetMapping("/pagamento/{pagamentoId}")
    public ResponseEntity<?> getByPagamento(@PathVariable Integer pagamentoId) {
        return faturaService.findByPagamentoId(pagamentoId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/faturas
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
        try {
            Integer encomendaId = Integer.parseInt(body.get("id_encomenda").toString());
            String nome = (String) body.get("nome");
            String nif = (String) body.getOrDefault("nif", "");
            String morada = (String) body.get("morada");
            String cidade = (String) body.get("cidade");
            String codigoPostal = (String) body.get("codigo_postal");
            String telefone = (String) body.getOrDefault("telefone", "");
            String metodoPagamento = (String) body.getOrDefault("metodo_pagamento", "A definir");

            Fatura fatura = faturaService.createOrUpdate(
                encomendaId, nome, nif, morada, cidade, codigoPostal, telefone, metodoPagamento
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(fatura);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST /api/faturas/pagamento
    @PostMapping("/pagamento")
    public ResponseEntity<?> createForPagamento(@RequestBody Map<String, Object> body) {
        try {
            Integer pagamentoId = Integer.parseInt(body.get("id_pagamento").toString());
            String nome = (String) body.get("nome");
            String nif = (String) body.getOrDefault("nif", "");
            String morada = (String) body.get("morada");
            String cidade = (String) body.get("cidade");
            String codigoPostal = (String) body.get("codigo_postal");
            String telefone = (String) body.getOrDefault("telefone", "");
            String metodoPagamento = (String) body.getOrDefault("metodo_pagamento", "A definir");

            Fatura fatura = faturaService.createOrUpdateForPagamento(
                pagamentoId, nome, nif, morada, cidade, codigoPostal, telefone, metodoPagamento
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(fatura);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
