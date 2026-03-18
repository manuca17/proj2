package com.example.proj2.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "projeto_personalizado")
public class ProjetoPersonalizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projeto", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilizador")
    private Utilizador idUtilizador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_artesa")
    private Artesa idArtesa;

    @Column(name = "titulo_projeto", nullable = false)
    private String tituloProjeto;

    @Column(name = "briefing", length = Integer.MAX_VALUE)
    private String briefing;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_criacao")
    private Instant dataCriacao;

    @ColumnDefault("briefing")
    @Column(name = "estado_atual")
    private String estadoAtual;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Utilizador getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(Utilizador idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public Artesa getIdArtesa() {
        return idArtesa;
    }

    public void setIdArtesa(Artesa idArtesa) {
        this.idArtesa = idArtesa;
    }

    public String getTituloProjeto() {
        return tituloProjeto;
    }

    public void setTituloProjeto(String tituloProjeto) {
        this.tituloProjeto = tituloProjeto;
    }

    public String getBriefing() {
        return briefing;
    }

    public void setBriefing(String briefing) {
        this.briefing = briefing;
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Instant dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getEstadoAtual() {
        return estadoAtual;
    }

    public void setEstadoAtual(String estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

}