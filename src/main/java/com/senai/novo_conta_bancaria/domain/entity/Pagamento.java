package com.senai.novo_conta_bancaria.domain.entity;

import com.senai.novo_conta_bancaria.domain.enums.StatusPagamento;
import com.senai.novo_conta_bancaria.domain.enums.FormaPagamento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "pagamentos",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        }
)
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pagamento_conta"))
    private Cliente conta;

    @Column(nullable = false, length = 120)
    private String servico;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPago;

    @Column(nullable = false)
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected StatusPagamento status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pagamento_taxas",
            joinColumns = @JoinColumn(name = "pagamento_id"),
            inverseJoinColumns = @JoinColumn(name = "taxa_id"))
    private HashSet<Taxa> taxas;

    @Column(nullable = false)
    public FormaPagamento formaPagamento;
}
