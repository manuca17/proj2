package com.example.proj2.controllers;

import com.example.proj2.models.ArtigoCatalogo;
import com.example.proj2.models.FichaTecnica;
import com.example.proj2.services.ArtigoCatalogoService;
import com.example.proj2.services.FichaTecnicaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/artigos-catalogo")
public class ArtigoCatalogoController {

    private final ArtigoCatalogoService service;
    private final FichaTecnicaService fichaTecnicaService;

    public ArtigoCatalogoController(ArtigoCatalogoService service, FichaTecnicaService fichaTecnicaService) {
        this.service = service;
        this.fichaTecnicaService = fichaTecnicaService;
    }

    @GetMapping
    public List<ArtigoCatalogo> getAll() {
        return service.findAll();
    }

    // POST /api/artigos-catalogo
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ArtigoCatalogo artigo) {
        if (artigo == null) {
            return ResponseEntity.badRequest().body("Payload de artigo inválido.");
        }

        if (artigo.getFichasTecnicas() != null) {
            artigo.getFichasTecnicas().forEach(ficha -> ficha.setArtigoCatalogo(artigo));
        }

        try {
            ArtigoCatalogo saved = service.save(artigo);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/artigos-catalogo/{artigoId}
    @PutMapping("/{artigoId}")
    public ResponseEntity<?> update(@PathVariable Integer artigoId, @RequestBody ArtigoCatalogo artigo) {
        if (artigo == null) {
            return ResponseEntity.badRequest().body("Payload de artigo inválido.");
        }

        ArtigoCatalogo existing = service.findById(artigoId).orElse(null);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artigo não encontrado.");
        }

        if (artigo.getNome() != null) {
            existing.setNome(artigo.getNome());
        }
        if (artigo.getPrecoUnitario() != null) {
            existing.setPrecoUnitario(artigo.getPrecoUnitario());
        }
        if (artigo.getStock() != null) {
            existing.setStock(artigo.getStock());
        }
        if (artigo.getVisivel() != null) {
            existing.setVisivel(artigo.getVisivel());
        }

        if (artigo.getFichasTecnicas() != null) {
            existing.setFichasTecnicas(artigo.getFichasTecnicas());
        }

        try {
            ArtigoCatalogo saved = service.save(existing);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/artigos-catalogo/{artigoId}
    @DeleteMapping("/{artigoId}")
    public ResponseEntity<?> delete(@PathVariable Integer artigoId) {
        if (service.findById(artigoId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artigo não encontrado.");
        }
        service.deleteById(artigoId);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/artigos-catalogo/{artigoId}/ficha-tecnica/{fichaId}
    @PutMapping("/{artigoId}/ficha-tecnica/{fichaId}")
    public ResponseEntity<?> attachFichaTecnica(@PathVariable Integer artigoId, @PathVariable Integer fichaId) {
        ArtigoCatalogo artigo = service.findById(artigoId).orElse(null);
        if (artigo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artigo não encontrado.");
        }
        FichaTecnica ficha = fichaTecnicaService.findById(fichaId).orElse(null);
        if (ficha == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ficha técnica não encontrada.");
        }

        ficha.setArtigoCatalogo(artigo);
        fichaTecnicaService.save(ficha);
        return ResponseEntity.ok(artigo);
    }
}