package com.example.proj2.controllers;

import com.example.proj2.models.Artesa;
import com.example.proj2.models.Orcamento;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.repository.OrcamentoRepository;
import com.example.proj2.services.ArtesaService;
import com.example.proj2.services.ProjetoPersonalizadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    private final OrcamentoRepository repository;
    private final ProjetoPersonalizadoService projetoService;
    private final ArtesaService artesaService;

    public OrcamentoController(OrcamentoRepository repository,
                               ProjetoPersonalizadoService projetoService,
                               ArtesaService artesaService) {
        this.repository = repository;
        this.projetoService = projetoService;
        this.artesaService = artesaService;
    }

    @GetMapping
    public List<Orcamento> getAll() {
        return repository.findAll();
    }

    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<?> getByProjetoId(@PathVariable Integer projetoId) {
        ProjetoPersonalizado projeto = projetoService.findById(projetoId).orElse(null);
        if (projeto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        return ResponseEntity.ok(repository.findByIdProjeto(projeto));
    }

    @PostMapping("/projeto/{projetoId}")
    public ResponseEntity<?> create(@PathVariable Integer projetoId, @RequestBody Orcamento orcamento) {
        ProjetoPersonalizado projeto = projetoService.findById(projetoId).orElse(null);
        if (projeto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        if (orcamento == null || orcamento.getValorTotalEstimado() == null) {
            return ResponseEntity.badRequest().body("Valor total estimado é obrigatório.");
        }

        orcamento.setIdProjeto(projeto);

        if (orcamento.getIdArtesa() != null && orcamento.getIdArtesa().getId() != null) {
            Artesa artesa = artesaService.findById(orcamento.getIdArtesa().getId()).orElse(null);
            orcamento.setIdArtesa(artesa);
        }

        if (orcamento.getDataSubmissao() == null) {
            orcamento.setDataSubmissao(Instant.now());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(orcamento));
    }
}
