package com.example.proj2.services;

import com.example.proj2.models.Orcamento;
import com.example.proj2.repository.OrcamentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrcamentoServiceTest {

    @Mock
    private OrcamentoRepository repository;

    @InjectMocks
    private OrcamentoService service;

    @Test
    void saveShouldRejectZeroValue() {
        Orcamento orcamento = createOrcamento(BigDecimal.ZERO);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.save(orcamento));

        assertEquals("O valor do orçamento deve ser positivo.", exception.getMessage());
        verify(repository, never()).save(orcamento);
    }

    @Test
    void saveShouldRejectNegativeValue() {
        Orcamento orcamento = createOrcamento(new BigDecimal("-1.00"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.save(orcamento));

        assertEquals("O valor do orçamento deve ser positivo.", exception.getMessage());
        verify(repository, never()).save(orcamento);
    }

    @Test
    void saveShouldPersistPositiveValue() {
        Orcamento orcamento = createOrcamento(new BigDecimal("45.50"));

        when(repository.save(orcamento)).thenReturn(orcamento);

        Orcamento saved = service.save(orcamento);

        assertEquals(new BigDecimal("45.50"), saved.getValorTotalEstimado());
        verify(repository).save(orcamento);
    }

    private Orcamento createOrcamento(BigDecimal valor) {
        Orcamento orcamento = new Orcamento();
        orcamento.setValorTotalEstimado(valor);
        return orcamento;
    }
}
