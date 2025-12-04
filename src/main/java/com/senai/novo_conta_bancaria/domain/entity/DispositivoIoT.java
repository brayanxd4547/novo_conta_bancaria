package com.senai.novo_conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "dispositivosIoTs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        }
)
public class DispositivoIoT {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 120)
    private String codigoSerial;

    @Column(nullable = false, length = 120)
    private String chavePublica;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToOne(mappedBy = "dispositivoIoT")
    private Cliente cliente;
}
