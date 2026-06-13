package com.example.proj2.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "fatura")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fatura", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encomenda", unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EncomendaCatalogo idEncomenda;

    @Column(name = "numero_fatura", unique = true)
    private String numeroFatura;

    @Column(name = "nome")
    private String nome;

    @Column(name = "nif")
    private String nif;

    @Column(name = "morada")
    private String morada;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "metodo_pagamento")
    private String metodoPagamento;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_emissao")
    private Instant dataEmissao;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public EncomendaCatalogo getIdEncomenda() { return idEncomenda; }
    public void setIdEncomenda(EncomendaCatalogo idEncomenda) { this.idEncomenda = idEncomenda; }

    public String getNumeroFatura() { return numeroFatura; }
    public void setNumeroFatura(String numeroFatura) { this.numeroFatura = numeroFatura; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public String getMorada() { return morada; }
    public void setMorada(String morada) { this.morada = morada; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }

    public Instant getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(Instant dataEmissao) { this.dataEmissao = dataEmissao; }
}
