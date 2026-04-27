package com.example.proj2.controllers;

import com.example.proj2.models.Utilizador;
import com.example.proj2.services.UtilizadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/utilizadores")
public class UtilizadorController {

    private final UtilizadorService service;

    public UtilizadorController(UtilizadorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Utilizador> getAll() {
        return service.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request == null || request.email() == null || request.email().isBlank()
            || request.password() == null || request.password().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse("Dados de login inválidos.", null, null, null, null));
        }

        return service.authenticateUser(request.email(), request.password())
            .map(user -> ResponseEntity.ok(new LoginResponse("Login efetuado com sucesso.", user.getId(), user.getNomeEmpresa(), user.getEmail(), "CLIENTE")))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse("Credenciais inválidas.", null, null, null, null)));
    }

    @PostMapping("/registar")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        if (request == null || request.nomeEmpresa() == null || request.nomeEmpresa().isBlank()
            || request.nif() == null || request.nif().isBlank()
            || request.email() == null || request.email().isBlank()
            || request.password() == null || request.password().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RegisterResponse("Dados de registo inválidos.", null, null, null, null));
        }

        Utilizador utilizador = new Utilizador();
        utilizador.setNomeEmpresa(request.nomeEmpresa());
        utilizador.setNif(request.nif());
        utilizador.setEmail(request.email());
        utilizador.setPassword(request.password());
        utilizador.setTelefone(request.telefone());
        utilizador.setMoradaFaturacao(request.moradaFaturacao());

        try {
            Utilizador saved = service.registerUser(utilizador);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterResponse("Registo efetuado com sucesso.", saved.getId(), saved.getNomeEmpresa(), saved.getEmail(), "CLIENTE"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RegisterResponse(ex.getMessage(), null, null, null, null));
        }
    }

    public record LoginRequest(String email, String password) {
    }

    public record LoginResponse(String message, Integer id, String nomeEmpresa, String email, String perfil) {
    }

    public record RegisterRequest(String nomeEmpresa, String nif, String email, String password, String telefone,
                                 String moradaFaturacao) {
    }

    public record RegisterResponse(String message, Integer id, String nomeEmpresa, String email, String perfil) {
    }
}