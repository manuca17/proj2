package com.example.proj2.controllers;

import com.example.proj2.models.ProjetoPersonalizado;
import com.example.proj2.models.Reuniao;
import com.example.proj2.services.ProjetoPersonalizadoService;
import com.example.proj2.services.ReuniaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reunioes")
public class ReuniaoController {

    private final ReuniaoService service;
    private final ProjetoPersonalizadoService projetoService;

    public ReuniaoController(ReuniaoService service, ProjetoPersonalizadoService projetoService) {
        this.service = service;
        this.projetoService = projetoService;
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

    // POST /api/reunioes/projeto/{projetoId}
    // A artesã fica sempre associada via o projeto — não precisa de ser passada na URL
    @PostMapping("/projeto/{projetoId}")
    public ResponseEntity<?> schedule(@PathVariable Integer projetoId, @RequestBody Reuniao reuniao) {
        ProjetoPersonalizado projeto = projetoService.findById(projetoId).orElse(null);
        if (projeto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
        }
        if (reuniao == null || reuniao.getDataHora() == null) {
            return ResponseEntity.badRequest().body("A data/hora da reunião é obrigatória.");
        }

        reuniao.setIdProjeto(projeto);
        if (reuniao.getStatus() == null || reuniao.getStatus().isBlank()) {
            reuniao.setStatus("agendada");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.scheduleMeeting(reuniao));
    }
}
