package com.example.proj2.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "item_encomenda")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemEncomenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "idUtilizador"})
    @JoinColumn(name = "id_encomenda")
    private EncomendaCatalogo idEncomenda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "idFichaTecnica"})
    @JoinColumn(name = "id_artigo")
    private ArtigoCatalogo idArtigo;

    @ColumnDefault("1")
    @Column(name = "quantidade")
    private Integer quantidade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EncomendaCatalogo getIdEncomenda() {
        return idEncomenda;
    }

    public void setIdEncomenda(EncomendaCatalogo idEncomenda) {
        this.idEncomenda = idEncomenda;
    }

    public ArtigoCatalogo getIdArtigo() {
        return idArtigo;
    }

    public void setIdArtigo(ArtigoCatalogo idArtigo) {
        this.idArtigo = idArtigo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @JsonProperty("nomeProduto")
    public String getNomeProduto() {
        return idArtigo == null ? null : idArtigo.getNome();
    }

    @JsonProperty("precoUnitario")
    public BigDecimal getPrecoUnitario() {
        return idArtigo == null ? null : idArtigo.getPrecoUnitario();
    }

}