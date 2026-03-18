package com.example.proj2.services;

import com.example.proj2.models.ArtigoCatalogo;
import com.example.proj2.repository.ArtigoCatalogoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtigoCatalogoServiceTest {

    @Mock
    private ArtigoCatalogoRepository repository;

    @InjectMocks
    private ArtigoCatalogoService service;

    @Test
    void saveShouldRejectNegativeStock() {
        ArtigoCatalogo artigo = createArtigo(-1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.save(artigo));

        assertEquals("O stock do artigo deve ser maior ou igual a 0.", exception.getMessage());
        verify(repository, never()).save(artigo);
    }

    @Test
    void saveShouldDefaultNullStockToZero() {
        ArtigoCatalogo artigo = createArtigo(null);

        when(repository.save(artigo)).thenReturn(artigo);

        ArtigoCatalogo saved = service.save(artigo);

        assertEquals(0, saved.getStock());
        verify(repository).save(artigo);
    }

    @Test
    void saveShouldPersistPositiveStock() {
        ArtigoCatalogo artigo = createArtigo(5);

        when(repository.save(artigo)).thenReturn(artigo);

        ArtigoCatalogo saved = service.save(artigo);

        assertEquals(5, saved.getStock());
        verify(repository).save(artigo);
    }

    private ArtigoCatalogo createArtigo(Integer stock) {
        ArtigoCatalogo artigo = new ArtigoCatalogo();
        artigo.setNome("Peça de teste");
        artigo.setStock(stock);
        artigo.setVisivel(true);
        return artigo;
    }
}
