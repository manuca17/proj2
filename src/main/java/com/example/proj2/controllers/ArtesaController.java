package com.example.proj2.controllers;

import com.example.proj2.models.Artesa;
import com.example.proj2.services.ArtesaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/artesas")
public class ArtesaController {

    private final ArtesaService service;

    public ArtesaController(ArtesaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Artesa> getAll() {
        return service.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request == null || request.email() == null || request.email().isBlank()
            || request.password() == null || request.password().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new LoginResponse("Dados de login inválidos.", null, null, null, null));
        }

        return service.authenticateUser(request.email(), request.password())
            .map(user -> ResponseEntity.ok(new LoginResponse("Login efetuado com sucesso.", user.getId(), user.getNome(), user.getEmail(), "ARTESA")))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse("Credenciais inválidas.", null, null, null, null)));
    }

    public record LoginRequest(String email, String password) {
    }

    public record LoginResponse(String message, Integer id, String nome, String email, String perfil) {
    }
}
