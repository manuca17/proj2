package com.example.proj2.models;

import jakarta.persistence.*;

@Entity
@Table(name = "utilizador")
public class Utilizador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilizador", nullable = false)
    private Integer id;

    @Column(name = "nome_empresa", nullable = false)
    private String nomeEmpresa;

    @Column(name = "nif", nullable = false, length = 9)
    private String nif;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "telefone", length = 15)
    private String telefone;

    @Column(name = "morada_faturacao", length = Integer.MAX_VALUE)
    private String moradaFaturacao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMoradaFaturacao() {
        return moradaFaturacao;
    }

    public void setMoradaFaturacao(String moradaFaturacao) {
        this.moradaFaturacao = moradaFaturacao;
    }

}