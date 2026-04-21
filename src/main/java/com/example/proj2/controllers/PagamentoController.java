package com.example.proj2.controllers;

import com.example.proj2.models.Orcamento;
import com.example.proj2.models.Pagamento;
import com.example.proj2.services.OrcamentoService;
import com.example.proj2.services.PagamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService service;
    private final OrcamentoService orcamentoService;

    public PagamentoController(PagamentoService service, OrcamentoService orcamentoService) {
        this.service = service;
        this.orcamentoService = orcamentoService;
    }

    @GetMapping
    public List<Pagamento> getAll() {
        return service.findAll();
    }

    // POST /api/pagamentos/orcamento/{orcamentoId}/pagar
    @PostMapping("/orcamento/{orcamentoId}/pagar")
    public ResponseEntity<?> payOrcamento(@PathVariable Integer orcamentoId, @RequestBody(required = false) Map<String, String> body) {
        Orcamento orcamento = orcamentoService.findById(orcamentoId).orElse(null);
        if (orcamento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orçamento não encontrado.");
        }

        try {
            String tipoPagamento = body == null ? null : body.get("tipoPagamento");
            Pagamento pagamento = service.processOrcamentoPayment(orcamento, tipoPagamento);
            orcamentoService.updateEstado(orcamentoId, "pago");
            return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
