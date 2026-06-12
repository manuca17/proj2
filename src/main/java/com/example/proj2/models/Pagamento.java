package com.example.proj2.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "pagamento")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProjetoPersonalizado idProjeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encomenda")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EncomendaCatalogo idEncomenda;

    // FASE_1, FASE_2, FASE_3 para projetos; null para encomendas de catálogo
    @Column(name = "fase", length = 20)
    private String fase;

    @Column(name = "descricao", length = Integer.MAX_VALUE)
    private String descricao;

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

    public ProjetoPersonalizado getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(ProjetoPersonalizado idProjeto) {
        this.idProjeto = idProjeto;
    }

    public EncomendaCatalogo getIdEncomenda() {
        return idEncomenda;
    }

    public void setIdEncomenda(EncomendaCatalogo idEncomenda) {
        this.idEncomenda = idEncomenda;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
