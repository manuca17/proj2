package com.example.proj2.models;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "reuniao")
public class Reuniao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reuniao", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_artesa")
    private Artesa idArtesa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto")
    private ProjetoPersonalizado idProjeto;

    @Column(name = "data_hora", nullable = false)
    private Instant dataHora;


    @Column(name = "tipo")
    private String tipo;

    @Column(name = "status")
    private String status;

    @Column(name = "local")
    private String local;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Artesa getIdArtesa() {
        return idArtesa;
    }

    public void setIdArtesa(Artesa idArtesa) {
        this.idArtesa = idArtesa;
    }

    public ProjetoPersonalizado getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(ProjetoPersonalizado idProjeto) {
        this.idProjeto = idProjeto;
    }

    public Instant getDataHora() {
        return dataHora;
    }

    public void setDataHora(Instant dataHora) {
        this.dataHora = dataHora;
    }

    // Getters e Setters atualizados
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

}