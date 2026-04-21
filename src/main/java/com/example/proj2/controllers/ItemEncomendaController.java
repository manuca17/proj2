package com.example.proj2.controllers;

import com.example.proj2.models.ItemEncomenda;
import com.example.proj2.services.ItemEncomendaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/itens-encomenda")
public class ItemEncomendaController {

    private final ItemEncomendaService service;

    public ItemEncomendaController(ItemEncomendaService service) {
        this.service = service;
    }

    @GetMapping
    public List<ItemEncomenda> getAll() {
        return service.findAll();
    }

    @GetMapping("/encomenda/{encomendaId}")
    public List<ItemEncomenda> getByEncomendaId(@PathVariable Integer encomendaId) {
        return service.findByEncomendaId(encomendaId);
    }
}