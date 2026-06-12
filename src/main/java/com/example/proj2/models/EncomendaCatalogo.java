package com.example.proj2.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.proj2.models.ProjetoPersonalizado;

@Entity
@Table(name = "encomenda_catalogo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EncomendaCatalogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_encomenda", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilizador")
    private Utilizador idUtilizador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProjetoPersonalizado idProjeto;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_pedido")
    private Instant dataPedido;

    @ColumnDefault("0.00")
    @Column(name = "valor_final", precision = 10, scale = 2)
    private BigDecimal valorFinal;

    @ColumnDefault("pendente")
    @Column(name = "estado")
    private String estado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Utilizador getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(Utilizador idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public ProjetoPersonalizado getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(ProjetoPersonalizado idProjeto) {
        this.idProjeto = idProjeto;
    }

    public Instant getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Instant dataPedido) {
        this.dataPedido = dataPedido;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Alias para contrato do frontend/admin
    @JsonProperty("dataEncomenda")
    public Instant getDataEncomenda() {
        return dataPedido;
    }

    @JsonProperty("estadoPagamento")
    public String getEstadoPagamento() {
        return "pago".equalsIgnoreCase(estado) ? "PAGO" : "PENDENTE";
    }

    @JsonProperty("paymentStatus")
    public String getPaymentStatus() {
        return getEstadoPagamento();
    }

}