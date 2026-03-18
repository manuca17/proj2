package com.example.proj2.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ficha_tecnica")
public class FichaTecnica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ficha", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto")
    private ProjetoPersonalizado idProjeto;

    @Column(name = "tipo_barro", length = 100)
    private String tipoBarro;

    @Column(name = "cor_vidrado", length = 100)
    private String corVidrado;

    @Column(name = "temperatura_cozedura")
    private Integer temperaturaCozedura;

    @Column(name = "tempo_secagem", length = 50)
    private String tempoSecagem;

    @Column(name = "observacoes", length = Integer.MAX_VALUE)
    private String observacoes;

    @Column(name = "foto_design")
    private String fotoDesign;

    @Column(name = "foto_prototipo")
    private String fotoPrototipo;

    @Column(name = "ref_molde", length = 100)
    private String refMolde;

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

    public String getTipoBarro() {
        return tipoBarro;
    }

    public void setTipoBarro(String tipoBarro) {
        this.tipoBarro = tipoBarro;
    }

    public String getCorVidrado() {
        return corVidrado;
    }

    public void setCorVidrado(String corVidrado) {
        this.corVidrado = corVidrado;
    }

    public Integer getTemperaturaCozedura() {
        return temperaturaCozedura;
    }

    public void setTemperaturaCozedura(Integer temperaturaCozedura) {
        this.temperaturaCozedura = temperaturaCozedura;
    }

    public String getTempoSecagem() {
        return tempoSecagem;
    }

    public void setTempoSecagem(String tempoSecagem) {
        this.tempoSecagem = tempoSecagem;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getFotoDesign() {
        return fotoDesign;
    }

    public void setFotoDesign(String fotoDesign) {
        this.fotoDesign = fotoDesign;
    }

    public String getFotoPrototipo() {
        return fotoPrototipo;
    }

    public void setFotoPrototipo(String fotoPrototipo) {
        this.fotoPrototipo = fotoPrototipo;
    }

    public String getRefMolde() {
        return refMolde;
    }

    public void setRefMolde(String refMolde) {
        this.refMolde = refMolde;
    }

}