package com.example.proj2.controllers;

import com.example.proj2.models.EncomendaCatalogo;
import com.example.proj2.services.EncomendaCatalogoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
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

    // GET /api/encomendas/aguarda-aprovacao  (admin)
    @GetMapping("/aguarda-aprovacao")
    public ResponseEntity<List<EncomendaCatalogo>> getPendentesAprovacao() {
        return ResponseEntity.ok(encomendaService.findPendentesAprovacao());
    }

    // PUT /api/encomendas/{id}/aprovar  (admin)
    @PutMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovar(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        try {
            BigDecimal preco = new BigDecimal(body.get("preco").toString());
            EncomendaCatalogo atualizada = encomendaService.aprovarEncomenda(id, preco);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // PUT /api/encomendas/{id}/rejeitar  (admin)
    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<?> rejeitar(@PathVariable Integer id) {
        try {
            EncomendaCatalogo atualizada = encomendaService.rejeitarEncomenda(id);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // PUT /api/encomendas/{id}/pagar  (client)
    @PutMapping("/{id}/pagar")
    public ResponseEntity<?> pagar(@PathVariable Integer id) {
        try {
            EncomendaCatalogo atualizada = encomendaService.pagarEncomenda(id);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
