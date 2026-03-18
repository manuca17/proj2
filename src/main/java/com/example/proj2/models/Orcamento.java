package com.example.proj2.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orcamento")
public class Orcamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orcamento", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto")
    private ProjetoPersonalizado idProjeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_artesa")
    private Artesa idArtesa;

    @ColumnDefault("0.00")
    @Column(name = "valor_total_estimado", precision = 10, scale = 2)
    private BigDecimal valorTotalEstimado;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_envio")
    private Instant dataEnvio;

    @ColumnDefault("pendente")
    @Column(name = "estado")
    private String estado;

    @Column(name = "observacoes", length = Integer.MAX_VALUE)
    private String observacoes;

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

    public Artesa getIdArtesa() {
        return idArtesa;
    }

    public void setIdArtesa(Artesa idArtesa) {
        this.idArtesa = idArtesa;
    }

    public BigDecimal getValorTotalEstimado() {
        return valorTotalEstimado;
    }

    public void setValorTotalEstimado(BigDecimal valorTotalEstimado) {
        this.valorTotalEstimado = valorTotalEstimado;
    }

    public Instant getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Instant dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Object getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

}