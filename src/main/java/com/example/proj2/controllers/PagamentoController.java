package com.example.proj2.controllers;

import com.example.proj2.models.EncomendaCatalogo;
import com.example.proj2.models.Pagamento;
import com.example.proj2.services.EncomendaCatalogoService;
import com.example.proj2.services.PagamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService service;
    private final EncomendaCatalogoService encomendaService;

    public PagamentoController(PagamentoService service, EncomendaCatalogoService encomendaService) {
        this.service = service;
        this.encomendaService = encomendaService;
    }

    @GetMapping
    public List<Pagamento> getAll() {
        return service.findAll();
    }

    /** GET /api/pagamentos/projeto/{projetoId} — lista todas as fases de um projeto */
    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<?> getByProjeto(@PathVariable Integer projetoId) {
        try {
            return ResponseEntity.ok(service.findByProjeto(projetoId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * POST /api/pagamentos/projeto/{projetoId}/fase
     * Body: { "fase": "FASE_1", "valor": 120.00, "descricao": "Design e Molde" }
     * A artesã define uma fase de pagamento para o projeto.
     */
    @PostMapping("/projeto/{projetoId}/fase")
    public ResponseEntity<?> criarFase(@PathVariable Integer projetoId, @RequestBody Map<String, Object> body) {
        try {
            String fase = (String) body.get("fase");
            BigDecimal valor = new BigDecimal(body.get("valor").toString());
            String descricao = (String) body.getOrDefault("descricao", "");

            Pagamento criado = service.criarFaseProjeto(projetoId, fase, valor, descricao);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * POST /api/pagamentos/{id}/pagar
     * Body: { "tipoPagamento": "MBWay" }
     * O cliente paga uma fase de projeto ou confirma pagamento de encomenda.
     */
    @PostMapping("/{id}/pagar")
    public ResponseEntity<?> pagar(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> body) {
        try {
            String tipoPagamento = body == null ? null : body.get("tipoPagamento");
            Pagamento pago = service.pagarFase(id, tipoPagamento);
            return ResponseEntity.ok(pago);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * POST /api/pagamentos/encomenda/{encomendaId}/pagar
     * Body: { "tipoPagamento": "Cartão" }
     * O cliente paga uma encomenda de catálogo.
     */
    @PostMapping("/encomenda/{encomendaId}/pagar")
    public ResponseEntity<?> pagarEncomenda(@PathVariable Integer encomendaId, @RequestBody(required = false) Map<String, String> body) {
        EncomendaCatalogo encomenda = encomendaService.findById(encomendaId).orElse(null);
        if (encomenda == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Encomenda não encontrada.");
        }
        try {
            String tipoPagamento = body == null ? null : body.get("tipoPagamento");
            Pagamento pago = service.pagarEncomenda(encomenda, tipoPagamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(pago);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
