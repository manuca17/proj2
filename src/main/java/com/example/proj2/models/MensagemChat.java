package com.example.proj2.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "mensagem_chat")
public class MensagemChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensagem", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto")
    private ProjetoPersonalizado idProjeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_remetente_utilizador")
    private Utilizador idRemetenteUtilizador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_remetente_artesa")
    private Artesa idRemetenteArtesa;

    @Column(name = "conteudo", nullable = false, length = Integer.MAX_VALUE)
    private String conteudo;

    @Column(name = "url_foto", length = Integer.MAX_VALUE)
    private String urlFoto;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_envio")
    private Instant dataEnvio;

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

    public Utilizador getIdRemetenteUtilizador() {
        return idRemetenteUtilizador;
    }

    public void setIdRemetenteUtilizador(Utilizador idRemetenteUtilizador) {
        this.idRemetenteUtilizador = idRemetenteUtilizador;
    }

    public Artesa getIdRemetenteArtesa() {
        return idRemetenteArtesa;
    }

    public void setIdRemetenteArtesa(Artesa idRemetenteArtesa) {
        this.idRemetenteArtesa = idRemetenteArtesa;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Instant getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Instant dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

}