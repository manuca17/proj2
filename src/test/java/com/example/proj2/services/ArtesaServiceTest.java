package com.example.proj2.services;

import com.example.proj2.models.Artesa;
import com.example.proj2.repository.ArtesaRepository;
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
class ArtesaServiceTest {

    @Mock
    private ArtesaRepository repository;

    @InjectMocks
    private ArtesaService service;

    @Test
    void saveShouldRejectDuplicateEmail() {
        Artesa nova = createArtesa(null, "Artesa@Email.com");
        Artesa existente = createArtesa(4, "artesa@email.com");

        when(repository.findByEmail("artesa@email.com")).thenReturn(Optional.of(existente));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.save(nova));

        assertEquals("Já existe uma artesã com esse email.", exception.getMessage());
        verify(repository, never()).save(nova);
    }

    @Test
    void saveShouldNormalizeEmailBeforePersisting() {
        Artesa artesa = createArtesa(null, "  ARTESA@EMAIL.COM ");

        when(repository.findByEmail("artesa@email.com")).thenReturn(Optional.empty());
        when(repository.save(artesa)).thenReturn(artesa);

        Artesa saved = service.save(artesa);

        assertEquals("artesa@email.com", saved.getEmail());
        verify(repository).save(artesa);
    }

    private Artesa createArtesa(Integer id, String email) {
        Artesa artesa = new Artesa();
        artesa.setId(id);
        artesa.setNome("Artesã Teste");
        artesa.setEmail(email);
        return artesa;
    }
}
