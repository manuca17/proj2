package com.example.proj2.services;

import com.example.proj2.models.Utilizador;
import com.example.proj2.repository.UtilizadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilizadorServiceTest {

    @Mock
    private UtilizadorRepository repository;

    @InjectMocks
    private UtilizadorService service;

    @Test
    void saveShouldRejectDuplicateEmail() {
        Utilizador novo = createUtilizador(null, "Duplicado@Email.com", "123456789");
        Utilizador existente = createUtilizador(10, "duplicado@email.com", "123456789");

        when(repository.findByEmail("duplicado@email.com")).thenReturn(Optional.of(existente));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.save(novo));

        assertEquals("Já existe um utilizador com esse email.", exception.getMessage());
        verify(repository, never()).save(novo);
    }

    @Test
    void saveShouldRejectNifLongerThanNineCharacters() {
        Utilizador utilizador = createUtilizador(null, "user@email.com", "1234567890");

        when(repository.findByEmail("user@email.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.save(utilizador));

        assertEquals("O NIF do utilizador deve ter no máximo 9 caracteres.", exception.getMessage());
        verify(repository, never()).save(utilizador);
    }

    @Test
    void saveShouldNormalizeEmailAndTrimNif() {
        Utilizador utilizador = createUtilizador(null, "  User@Email.COM ", " 123456789 ");

        when(repository.findByEmail("user@email.com")).thenReturn(Optional.empty());
        when(repository.save(utilizador)).thenReturn(utilizador);

        Utilizador saved = service.save(utilizador);

        assertEquals("user@email.com", saved.getEmail());
        assertEquals("123456789", saved.getNif());
        verify(repository).save(utilizador);
    }

    private Utilizador createUtilizador(Integer id, String email, String nif) {
        Utilizador utilizador = new Utilizador();
        utilizador.setId(id);
        utilizador.setEmail(email);
        utilizador.setNif(nif);
        utilizador.setNomeEmpresa("Empresa Teste");
        utilizador.setPassword("segredo");
        return utilizador;
    }
}
