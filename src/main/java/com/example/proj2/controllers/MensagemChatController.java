package com.example.proj2.controllers;

import com.example.proj2.models.Artesa;
import com.example.proj2.models.MensagemChat;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.models.Utilizador;
import com.example.proj2.services.ArtesaService;
import com.example.proj2.services.MensagemChatService;
import com.example.proj2.services.ProjetoPersonalizadoService;
import com.example.proj2.services.UtilizadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mensagens-chat")
public class MensagemChatController {

    private final MensagemChatService service;
    private final ProjetoPersonalizadoService projetoService;
    private final UtilizadorService utilizadorService;
    private final ArtesaService artesaService;

    public MensagemChatController(MensagemChatService service,
                                   ProjetoPersonalizadoService projetoService,
                                   UtilizadorService utilizadorService,
                                   ArtesaService artesaService) {
        this.service = service;
        this.projetoService = projetoService;
        this.utilizadorService = utilizadorService;
        this.artesaService = artesaService;
    }

    @GetMapping
    public List<MensagemChat> getAll() {
        return service.findAll();
    }

    // GET /api/mensagens-chat/projeto/{projetoId}
    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<?> getByProjetoId(@PathVariable Integer projetoId) {
        if (projetoService.findById(projetoId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        return ResponseEntity.ok(service.findByProjetoId(projetoId));
    }

    // GET /api/mensagens-chat/projeto/{projetoId}/total
    @GetMapping("/projeto/{projetoId}/total")
    public ResponseEntity<?> getTotalByProjetoId(@PathVariable Integer projetoId) {
        if (projetoService.findById(projetoId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        long totalMensagens = service.countByProjetoId(projetoId);
        return ResponseEntity.ok(Map.of(
            "projetoId", projetoId,
            "totalMensagens", totalMensagens
        ));
    }

    // POST /api/mensagens-chat/projeto/{projetoId}/utilizador/{utilizadorId}
    @PostMapping("/projeto/{projetoId}/utilizador/{utilizadorId}")
    public ResponseEntity<?> sendMessageAsUtilizador(@PathVariable Integer projetoId,
                                                      @PathVariable Integer utilizadorId,
                                                      @RequestBody MensagemChat mensagem) {
        ProjetoPersonalizado projeto = projetoService.findById(projetoId).orElse(null);
        if (projeto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        Utilizador utilizador = utilizadorService.findById(utilizadorId).orElse(null);
        if (utilizador == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilizador não encontrado.");
        }
        if (mensagem.getConteudo() == null || mensagem.getConteudo().isBlank()) {
            return ResponseEntity.badRequest().body("O conteúdo da mensagem é obrigatório.");
        }

        mensagem.setIdProjeto(projeto);
        mensagem.setIdRemetenteUtilizador(utilizador);
        mensagem.setIdRemetenteArtesa(null);
        if (mensagem.getDataEnvio() == null) {
            mensagem.setDataEnvio(Instant.now());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(mensagem));
    }

    // POST /api/mensagens-chat/projeto/{projetoId}/artesa/{artesaId}
    @PostMapping("/projeto/{projetoId}/artesa/{artesaId}")
    public ResponseEntity<?> sendMessageAsArtesa(@PathVariable Integer projetoId,
                                                  @PathVariable Integer artesaId,
                                                  @RequestBody MensagemChat mensagem) {
        ProjetoPersonalizado projeto = projetoService.findById(projetoId).orElse(null);
        if (projeto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        Artesa artesa = artesaService.findById(artesaId).orElse(null);
        if (artesa == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artesã não encontrada.");
        }
        if (mensagem.getConteudo() == null || mensagem.getConteudo().isBlank()) {
            return ResponseEntity.badRequest().body("O conteúdo da mensagem é obrigatório.");
        }

        mensagem.setIdProjeto(projeto);
        mensagem.setIdRemetenteUtilizador(null);
        mensagem.setIdRemetenteArtesa(artesa);
        if (mensagem.getDataEnvio() == null) {
            mensagem.setDataEnvio(Instant.now());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(mensagem));
    }
}