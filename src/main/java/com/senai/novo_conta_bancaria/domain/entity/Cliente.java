package com.senai.novo_conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Cliente extends Usuario {
    @Column(nullable = false)
    private Long biometria;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Conta> contas;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dispositivoIoT_id", referencedColumnName = "id")
    private DispositivoIoT dispositivoIoT;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<CodigoAutenticacao> codigosAutenticacao;
}