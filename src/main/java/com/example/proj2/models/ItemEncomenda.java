package com.example.proj2.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "item_encomenda")
public class ItemEncomenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encomenda")
    private EncomendaCatalogo idEncomenda;

    @ManyToOne(fetch = FetchType.LAZY)
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

}