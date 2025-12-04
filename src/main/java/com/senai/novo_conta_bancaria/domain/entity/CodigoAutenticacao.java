package com.senai.novo_conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "codigosAutenticacao",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        }
)
public class CodigoAutenticacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 120)
    private String codigo;

    @Column(nullable = false)
    private Integer expiraEm;

    @Column(nullable = false)
    private Boolean validado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_codigoAutenticacao_cliente"))
    private Cliente cliente;
}
