package com.example.proj2.controllers;

import com.example.proj2.models.Orcamento;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.services.OrcamentoService;
import com.example.proj2.services.ProjetoPersonalizadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    private final OrcamentoService service;
    private final ProjetoPersonalizadoService projetoService;

    public OrcamentoController(OrcamentoService service, ProjetoPersonalizadoService projetoService) {
        this.service = service;
        this.projetoService = projetoService;
    }

    @GetMapping
    public List<Orcamento> getAll() {
        return service.findAll();
    }

    // GET /api/orcamentos/projeto/{projetoId}
    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<?> getByProjetoId(@PathVariable Integer projetoId) {
        ProjetoPersonalizado projeto = projetoService.findById(projetoId).orElse(null);
        if (projeto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        return ResponseEntity.ok(service.findByIdProjeto(projeto));
    }

    // POST /api/orcamentos/projeto/{projetoId}
    @PostMapping("/projeto/{projetoId}")
    public ResponseEntity<?> createByProjetoId(@PathVariable Integer projetoId, @RequestBody Orcamento orcamento) {
        ProjetoPersonalizado projeto = projetoService.findById(projetoId).orElse(null);
        if (projeto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        if (orcamento == null) {
            return ResponseEntity.badRequest().body("Payload de orçamento inválido.");
        }

        // Garante vínculo consistente ao projeto vindo no path.
        orcamento.setIdProjeto(projeto);
        if (orcamento.getIdArtesa() == null && projeto.getIdArtesa() != null) {
            orcamento.setIdArtesa(projeto.getIdArtesa());
        }
        if (orcamento.getDataEnvio() == null) {
            orcamento.setDataEnvio(Instant.now());
        }
        if (orcamento.getEstado() == null) {
            orcamento.setEstado("pendente");
        }

        try {
            Orcamento saved = service.save(orcamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
