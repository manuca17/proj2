package com.example.proj2.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "artigo_catalogo")
public class ArtigoCatalogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_artigo", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ficha_tecnica")
    private FichaTecnica idFichaTecnica;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @ColumnDefault("0")
    @Column(name = "stock")
    private Integer stock;

    @ColumnDefault("true")
    @Column(name = "visivel")
    private Boolean visivel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FichaTecnica getIdFichaTecnica() {
        return idFichaTecnica;
    }

    public void setIdFichaTecnica(FichaTecnica idFichaTecnica) {
        this.idFichaTecnica = idFichaTecnica;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getVisivel() {
        return visivel;
    }

    public void setVisivel(Boolean visivel) {
        this.visivel = visivel;
    }

}