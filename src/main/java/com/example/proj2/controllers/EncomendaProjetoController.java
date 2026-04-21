package com.example.proj2.controllers;

import com.example.proj2.models.EncomendaCatalogo;
import com.example.proj2.services.EncomendaCatalogoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/encomendas")
public class EncomendaProjetoController {

    private final EncomendaCatalogoService encomendaService;

    public EncomendaProjetoController(EncomendaCatalogoService encomendaService) {
        this.encomendaService = encomendaService;
    }

    // POST /api/encomendas/projeto/{projetoId}/reencomendar
    @PostMapping("/projeto/{projetoId}/reencomendar")
    public ResponseEntity<?> reencomendar(@PathVariable Integer projetoId, @RequestBody(required = false) Map<String, Integer> body) {
        try {
            Integer quantidade = body == null ? null : body.get("quantidade");
            EncomendaCatalogo nova = encomendaService.reencomendarProjeto(projetoId, quantidade);
            return ResponseEntity.status(HttpStatus.CREATED).body(nova);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
