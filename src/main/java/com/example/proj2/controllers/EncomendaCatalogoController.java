package com.example.proj2.controllers;

import com.example.proj2.models.EncomendaCatalogo;
import com.example.proj2.models.ItemEncomenda;
import com.example.proj2.services.EncomendaCatalogoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/encomendas-catalogo")
public class EncomendaCatalogoController {

    private final EncomendaCatalogoService service;

    public EncomendaCatalogoController(EncomendaCatalogoService service) {
        this.service = service;
    }

    @GetMapping
    public List<EncomendaCatalogo> getAll() {
        return service.findAll();
    }

    // GET /api/encomendas-catalogo/utilizador/{utilizadorId}
    @GetMapping("/utilizador/{utilizadorId}")
    public List<EncomendaCatalogo> getByUtilizadorId(@PathVariable Integer utilizadorId) {
        return service.findByUtilizadorId(utilizadorId);
    }

    // GET /api/encomendas-catalogo/carrinho/{utilizadorId}
    // Devolve a encomenda em estado "carrinho" do utilizador (se existir)
    @GetMapping("/carrinho/{utilizadorId}")
    public ResponseEntity<EncomendaCatalogo> getCarrinho(@PathVariable Integer utilizadorId) {
        return service.findCarrinhoByUtilizadorId(utilizadorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/encomendas-catalogo/carrinho
    // Cria (ou reutiliza) carrinho e adiciona item
    // Body: { "utilizadorId": 1, "artigoId": 2, "quantidade": 3 }
    @PostMapping("/carrinho")
    public ResponseEntity<ItemEncomenda> addItemToCarrinho(@RequestBody Map<String, Integer> body) {
        Integer utilizadorId = body.get("utilizadorId");
        Integer artigoId = body.get("artigoId");
        Integer quantidade = body.getOrDefault("quantidade", 1);
        if (utilizadorId == null || artigoId == null) {
            return ResponseEntity.badRequest().build();
        }
        ItemEncomenda item = service.addItemToCarrinho(utilizadorId, artigoId, quantidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    // DELETE /api/encomendas-catalogo/carrinho/{utilizadorId}
    // Remove todos os itens e apaga o carrinho do utilizador
    @DeleteMapping("/carrinho/{utilizadorId}")
    public ResponseEntity<Void> deleteCarrinho(@PathVariable Integer utilizadorId) {
        service.deleteCarrinhoByUtilizadorId(utilizadorId);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/encomendas-catalogo/{id}/estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> updateEstadoPatch(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return updateEstadoInternal(id, body);
    }

    // PUT /api/encomendas-catalogo/{id}/estado
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstadoPut(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return updateEstadoInternal(id, body);
    }

    // POST /api/encomendas-catalogo/checkout
    // Body: { "utilizadorId": 1 }
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Map<String, Integer> body) {
        Integer utilizadorId = body.get("utilizadorId");
        Integer projetoId = body.get("projetoId");
        if (utilizadorId == null) {
            return ResponseEntity.badRequest().body("Campo 'utilizadorId' é obrigatório.");
        }
        try {
            EncomendaCatalogo encomenda = service.checkout(utilizadorId, projetoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(encomenda);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private ResponseEntity<?> updateEstadoInternal(Integer id, Map<String, String> body) {
        String estado = body == null ? null : body.get("estado");
        if (estado == null || estado.isBlank()) {
            return ResponseEntity.badRequest().body("Campo 'estado' é obrigatório.");
        }

        EncomendaCatalogo updated = service.updateEstado(id, estado);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Encomenda não encontrada.");
        }
        return ResponseEntity.ok(updated);
    }
}