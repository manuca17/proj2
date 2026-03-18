package com.example.proj2.services;

import com.example.proj2.models.Pagamento;
import com.example.proj2.repository.PagamentoRepository;
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
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository repository;

    @InjectMocks
    private PagamentoService service;

    @Test
    void saveShouldRejectZeroValue() {
        Pagamento pagamento = createPagamento(BigDecimal.ZERO);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.save(pagamento));

        assertEquals("O valor do pagamento deve ser positivo.", exception.getMessage());
        verify(repository, never()).save(pagamento);
    }

    @Test
    void processPaymentShouldRejectNegativeValue() {
        Pagamento pagamento = createPagamento(new BigDecimal("-12.30"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.processPayment(pagamento));

        assertEquals("O valor do pagamento deve ser positivo.", exception.getMessage());
        verify(repository, never()).save(pagamento);
    }

    @Test
    void processPaymentShouldPersistPositiveValue() {
        Pagamento pagamento = createPagamento(new BigDecimal("99.99"));

        when(repository.save(pagamento)).thenReturn(pagamento);

        Pagamento saved = service.processPayment(pagamento);

        assertEquals(new BigDecimal("99.99"), saved.getValor());
        verify(repository).save(pagamento);
    }

    private Pagamento createPagamento(BigDecimal valor) {
        Pagamento pagamento = new Pagamento();
        pagamento.setValor(valor);
        pagamento.setPago(false);
        return pagamento;
    }
}
