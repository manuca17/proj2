package com.example.proj2.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "artigo_foto")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ArtigoFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_artigo", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private ArtigoCatalogo idArtigo;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "ordem")
    private Integer ordem = 0;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ArtigoCatalogo getIdArtigo() { return idArtigo; }
    public void setIdArtigo(ArtigoCatalogo idArtigo) { this.idArtigo = idArtigo; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }
}
