package com.senai.novo_conta_bancaria.domain.entity;

import com.senai.novo_conta_bancaria.domain.enums.FormaPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "taxas",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        }
)
public class Taxa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 120)
    private String descricao;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal percentual;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorFixo;

    @ManyToMany(mappedBy = "taxas", fetch = FetchType.LAZY)
    private HashSet<Pagamento> pagamentos;

    @Column(nullable = false)
    public FormaPagamento formaPagamento;

    @Column(nullable = false)
    private boolean ativo;
}
