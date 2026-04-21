package com.example.proj2.controllers;

import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.models.MensagemChat;
import com.example.proj2.services.MensagemChatService;
import com.example.proj2.services.ProjetoPersonalizadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projetos-personalizados")
public class ProjetoPersonalizadoController {

    private final ProjetoPersonalizadoService service;
    private final MensagemChatService mensagemChatService;

    public ProjetoPersonalizadoController(ProjetoPersonalizadoService service, MensagemChatService mensagemChatService) {
        this.service = service;
        this.mensagemChatService = mensagemChatService;
    }

    @GetMapping
    public List<ProjetoPersonalizado> getAll() {
        return service.findAll();
    }

    @GetMapping("/utilizador/{utilizadorId}")
    public List<ProjetoPersonalizado> getByUtilizadorId(@PathVariable Integer utilizadorId) {
        return service.findByUtilizadorId(utilizadorId);
    }

    @PostMapping
    public ResponseEntity<ProjetoPersonalizado> create(@RequestBody ProjetoPersonalizado projeto) {
        ProjetoPersonalizado saved = service.save(projeto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET /api/projetos-personalizados/{id}/mensagens
    @GetMapping("/{id}/mensagens")
    public ResponseEntity<?> getMensagensByProjetoId(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        return ResponseEntity.ok(mensagemChatService.findByProjetoId(id));
    }

    // POST /api/projetos-personalizados/{id}/mensagens
    @PostMapping("/{id}/mensagens")
    public ResponseEntity<?> createMensagemByProjetoId(@PathVariable Integer id, @RequestBody MensagemChat mensagem) {
        ProjetoPersonalizado projeto = service.findById(id)
            .orElse(null);
        if (projeto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        if (mensagem == null || mensagem.getConteudo() == null || mensagem.getConteudo().isBlank()) {
            return ResponseEntity.badRequest().body("Campo 'conteudo' é obrigatório.");
        }

        // Força a associação ao projeto do path para evitar inconsistências no payload.
        mensagem.setIdProjeto(projeto);
        if (mensagem.getDataEnvio() == null) {
            mensagem.setDataEnvio(Instant.now());
        }

        MensagemChat saved = mensagemChatService.save(mensagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> updateEstadoPatch(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return updateEstadoInternal(id, body);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstadoPut(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return updateEstadoInternal(id, body);
    }

    private ResponseEntity<?> updateEstadoInternal(Integer id, Map<String, String> body) {
        String estado = body == null ? null : body.get("estado");
        if (estado == null || estado.isBlank()) {
            return ResponseEntity.badRequest().body("Campo 'estado' é obrigatório.");
        }

        ProjetoPersonalizado updated = service.updateEstado(id, estado);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        return ResponseEntity.ok(updated);
    }
}