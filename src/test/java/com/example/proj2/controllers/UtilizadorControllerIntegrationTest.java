package com.example.proj2.controllers;

import com.example.proj2.repository.UtilizadorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UtilizadorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @BeforeEach
    void setUp() {
        utilizadorRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        RegisterPayload payload = new RegisterPayload(
            "Empresa Teste",
            "123456789",
            "User@Email.com",
            "segredo",
            "912345678",
            "Rua A, 123"
        );

        mockMvc.perform(post("/api/utilizadores/registar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("Registo efetuado com sucesso."))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.nomeEmpresa").value("Empresa Teste"))
            .andExpect(jsonPath("$.email").value("user@email.com"))
            .andExpect(jsonPath("$.perfil").value("CLIENTE"));
    }

    @Test
    void shouldRejectInvalidPayload() throws Exception {
        RegisterPayload payload = new RegisterPayload(
            " ",
            "",
            "",
            "",
            null,
            null
        );

        mockMvc.perform(post("/api/utilizadores/registar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Dados de registo inválidos."));
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {
        RegisterPayload payload = new RegisterPayload(
            "Empresa Teste",
            "123456789",
            "user@email.com",
            "segredo",
            null,
            null
        );

        mockMvc.perform(post("/api/utilizadores/registar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isCreated());

        RegisterPayload duplicate = new RegisterPayload(
            "Outra Empresa",
            "987654321",
            "User@Email.com",
            "segredo",
            null,
            null
        );

        mockMvc.perform(post("/api/utilizadores/registar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicate)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Já existe um utilizador com esse email."));
    }

    private record RegisterPayload(String nomeEmpresa, String nif, String email, String password, String telefone,
                                   String moradaFaturacao) {
    }
}
