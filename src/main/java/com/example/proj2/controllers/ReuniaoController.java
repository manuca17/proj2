package com.example.proj2.controllers;

import com.example.proj2.models.Artesa;
import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.models.Reuniao;
import com.example.proj2.services.ArtesaService;
import com.example.proj2.services.ProjetoPersonalizadoService;
import com.example.proj2.services.ReuniaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reunioes")
public class ReuniaoController {

    private final ReuniaoService service;
    private final ProjetoPersonalizadoService projetoService;
    private final ArtesaService artesaService;

    public ReuniaoController(ReuniaoService service,
                             ProjetoPersonalizadoService projetoService,
                             ArtesaService artesaService) {
        this.service = service;
        this.projetoService = projetoService;
        this.artesaService = artesaService;
    }

    @GetMapping
    public List<Reuniao> getAll() {
        return service.findAll();
    }

    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<?> getByProjetoId(@PathVariable Integer projetoId) {
        if (projetoService.findById(projetoId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        return ResponseEntity.ok(service.findByProjetoId(projetoId));
    }

    // PUT /api/reunioes/{reuniaoId}/confirmar-presenca
    @PutMapping("/{reuniaoId}/confirmar-presenca")
    public ResponseEntity<?> confirmPresence(@PathVariable Integer reuniaoId) {
        Reuniao updated = service.updateStatus(reuniaoId, "confirmada");
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reunião não encontrada.");
        }
        return ResponseEntity.ok(updated);
    }

    // PUT /api/reunioes/{reuniaoId}/remarcar
    @PutMapping("/{reuniaoId}/remarcar")
    public ResponseEntity<?> reschedule(@PathVariable Integer reuniaoId, @RequestBody Reuniao reuniao) {
        if (reuniao == null || reuniao.getDataHora() == null) {
            return ResponseEntity.badRequest().body("A nova data/hora da reunião é obrigatória.");
        }

        Reuniao updated = service.reschedule(reuniaoId, reuniao);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reunião não encontrada.");
        }
        return ResponseEntity.ok(updated);
    }

    // PUT /api/reunioes/{reuniaoId}/cancelar
    @PutMapping("/{reuniaoId}/cancelar")
    public ResponseEntity<?> cancel(@PathVariable Integer reuniaoId) {
        Reuniao updated = service.updateStatus(reuniaoId, "cancelada");
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reunião não encontrada.");
        }
        return ResponseEntity.ok(updated);
    }

    // POST /api/reunioes/projeto/{projetoId}/artesa/{artesaId}
    @PostMapping("/projeto/{projetoId}/artesa/{artesaId}")
    public ResponseEntity<?> scheduleByProjetoAndArtesa(@PathVariable Integer projetoId,
                                                         @PathVariable Integer artesaId,
                                                         @RequestBody Reuniao reuniao) {
        ProjetoPersonalizado projeto = projetoService.findById(projetoId).orElse(null);
        if (projeto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }

        Artesa artesa = artesaService.findById(artesaId).orElse(null);
        if (artesa == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artesã não encontrada.");
        }

        if (reuniao == null || reuniao.getDataHora() == null) {
            return ResponseEntity.badRequest().body("A data/hora da reunião é obrigatória.");
        }

        reuniao.setIdProjeto(projeto);
        reuniao.setIdArtesa(artesa);
        if (reuniao.getStatus() == null || reuniao.getStatus().isBlank()) {
            reuniao.setStatus("agendada");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.scheduleMeeting(reuniao));
    }
}