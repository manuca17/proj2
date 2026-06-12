package com.example.proj2.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artigo_catalogo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ArtigoCatalogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_artigo", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "artigoCatalogo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<FichaTecnica> fichasTecnicas = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto_origem")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProjetoPersonalizado idProjetoOrigem;

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

    public List<FichaTecnica> getFichasTecnicas() {
        return fichasTecnicas;
    }

    public void setFichasTecnicas(List<FichaTecnica> fichasTecnicas) {
        this.fichasTecnicas.clear();
        if (fichasTecnicas != null) {
            for (FichaTecnica ficha : fichasTecnicas) {
                ficha.setArtigoCatalogo(this);
                this.fichasTecnicas.add(ficha);
            }
        }
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

    public ProjetoPersonalizado getIdProjetoOrigem() {
        return idProjetoOrigem;
    }

    public void setIdProjetoOrigem(ProjetoPersonalizado idProjetoOrigem) {
        this.idProjetoOrigem = idProjetoOrigem;
    }

}