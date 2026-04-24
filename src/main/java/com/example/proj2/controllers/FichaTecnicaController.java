package com.example.proj2.controllers;

import com.example.proj2.models.ArtigoCatalogo;
import com.example.proj2.models.FichaTecnica;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.services.ArtigoCatalogoService;
import com.example.proj2.services.FichaTecnicaService;
import com.example.proj2.services.ProjetoPersonalizadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fichas-tecnicas")
public class FichaTecnicaController {

    private final FichaTecnicaService service;
    private final ProjetoPersonalizadoService projetoService;
    private final ArtigoCatalogoService artigoService;

    public FichaTecnicaController(FichaTecnicaService service,
                                  ProjetoPersonalizadoService projetoService,
                                  ArtigoCatalogoService artigoService) {
        this.service = service;
        this.projetoService = projetoService;
        this.artigoService = artigoService;
    }

    @GetMapping
    public List<FichaTecnica> getAll() {
        return service.findAll();
    }

    // GET /api/fichas-tecnicas/projeto/{projetoId}
    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<?> getByProjetoId(@PathVariable Integer projetoId) {
        ProjetoPersonalizado projeto = projetoService.findById(projetoId).orElse(null);
        if (projeto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        return ResponseEntity.ok(service.findByIdProjeto(projeto));
    }

    // POST /api/fichas-tecnicas/projeto/{projetoId}
    @PostMapping("/projeto/{projetoId}")
    public ResponseEntity<?> createByProjetoId(@PathVariable Integer projetoId, @RequestBody FichaTecnica ficha) {
        ProjetoPersonalizado projeto = projetoService.findById(projetoId).orElse(null);
        if (projeto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        if (ficha == null) {
            return ResponseEntity.badRequest().body("Payload de ficha técnica inválido.");
        }

        ficha.setIdProjeto(projeto);
        FichaTecnica saved = service.save(ficha);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET /api/fichas-tecnicas/artigo/{artigoId}
    @GetMapping("/artigo/{artigoId}")
    public ResponseEntity<?> getByArtigoId(@PathVariable Integer artigoId) {
        ArtigoCatalogo artigo = artigoService.findById(artigoId).orElse(null);
        if (artigo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artigo não encontrado.");
        }
        return ResponseEntity.ok(service.findByArtigoCatalogo(artigo));
    }

    // POST /api/fichas-tecnicas/artigo/{artigoId}
    @PostMapping("/artigo/{artigoId}")
    public ResponseEntity<?> createByArtigoId(@PathVariable Integer artigoId, @RequestBody FichaTecnica ficha) {
        ArtigoCatalogo artigo = artigoService.findById(artigoId).orElse(null);
        if (artigo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artigo não encontrado.");
        }
        if (ficha == null) {
            return ResponseEntity.badRequest().body("Payload de ficha técnica inválido.");
        }

        ficha.setArtigoCatalogo(artigo);
        FichaTecnica saved = service.save(ficha);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/fichas-tecnicas/artigo/{artigoId}/{fichaId}
    @PutMapping("/artigo/{artigoId}/{fichaId}")
    public ResponseEntity<?> updateByArtigoId(@PathVariable Integer artigoId, @PathVariable Integer fichaId) {
        ArtigoCatalogo artigo = artigoService.findById(artigoId).orElse(null);
        if (artigo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artigo não encontrado.");
        }

        FichaTecnica existente = service.findById(fichaId).orElse(null);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ficha técnica não encontrada.");
        }

        existente.setArtigoCatalogo(artigo);
        FichaTecnica saved = service.save(existente);
        return ResponseEntity.ok(saved);
    }

    // DELETE /api/fichas-tecnicas/artigo/{artigoId}/{fichaId}
    @DeleteMapping("/artigo/{artigoId}/{fichaId}")
    public ResponseEntity<?> deleteByArtigoId(@PathVariable Integer artigoId, @PathVariable Integer fichaId) {
        ArtigoCatalogo artigo = artigoService.findById(artigoId).orElse(null);
        if (artigo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artigo não encontrado.");
        }

        FichaTecnica ficha = service.findById(fichaId).orElse(null);
        if (ficha == null || ficha.getArtigoCatalogo() == null || !ficha.getArtigoCatalogo().getId().equals(artigoId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ficha técnica não encontrada.");
        }

        ficha.setArtigoCatalogo(null);
        service.save(ficha);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/fichas-tecnicas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody FichaTecnica ficha) {
        if (ficha == null) {
            return ResponseEntity.badRequest().body("Payload de ficha técnica inválido.");
        }

        FichaTecnica updated = service.updateTechnicalDetails(id, ficha);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ficha técnica não encontrada.");
        }
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/fichas-tecnicas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ficha técnica não encontrada.");
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
