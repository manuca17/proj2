package com.example.proj2.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "pagamento")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orcamento")
    private Orcamento idOrcamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encomenda")
    private EncomendaCatalogo idEncomenda;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "tipo_pagamento", length = 50)
    private String tipoPagamento;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_pagamento")
    private Instant dataPagamento;

    @ColumnDefault("false")
    @Column(name = "pago")
    private Boolean pago;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Orcamento getIdOrcamento() {
        return idOrcamento;
    }

    public void setIdOrcamento(Orcamento idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public EncomendaCatalogo getIdEncomenda() {
        return idEncomenda;
    }

    public void setIdEncomenda(EncomendaCatalogo idEncomenda) {
        this.idEncomenda = idEncomenda;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Instant getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Instant dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

}