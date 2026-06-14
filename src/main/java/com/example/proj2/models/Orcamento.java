package com.example.proj2.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orcamento")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orcamento", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_projeto")
    private ProjetoPersonalizado idProjeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_artesa")
    private Artesa idArtesa;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "valor_total_estimado", precision = 10, scale = 2)
    private BigDecimal valorTotalEstimado;

    @Column(name = "observacoes", length = Integer.MAX_VALUE)
    private String observacoes;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_submissao")
    private Instant dataSubmissao;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public ProjetoPersonalizado getIdProjeto() { return idProjeto; }
    public void setIdProjeto(ProjetoPersonalizado idProjeto) { this.idProjeto = idProjeto; }
    public Artesa getIdArtesa() { return idArtesa; }
    public void setIdArtesa(Artesa idArtesa) { this.idArtesa = idArtesa; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public BigDecimal getValorTotalEstimado() { return valorTotalEstimado; }
    public void setValorTotalEstimado(BigDecimal v) { this.valorTotalEstimado = v; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public Instant getDataSubmissao() { return dataSubmissao; }
    public void setDataSubmissao(Instant dataSubmissao) { this.dataSubmissao = dataSubmissao; }
}
