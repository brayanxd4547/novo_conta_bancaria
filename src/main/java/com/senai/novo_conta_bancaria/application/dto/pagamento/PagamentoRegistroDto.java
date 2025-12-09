package com.senai.novo_conta_bancaria.application.dto.pagamento;

import com.senai.novo_conta_bancaria.domain.entity.Conta;
import com.senai.novo_conta_bancaria.domain.entity.Pagamento;
import com.senai.novo_conta_bancaria.domain.entity.Taxa;
import com.senai.novo_conta_bancaria.domain.enums.FormaPagamento;
import com.senai.novo_conta_bancaria.domain.enums.StatusPagamento;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record PagamentoRegistroDto(
        @NotNull(message = "O nome do serviço pago não pode ser nulo.")
        @NotBlank(message = "O nome do serviço pago não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome do serviço pago deve ter entre 3 e 100 caracteres.")
        String servico,

        @NotNull(message = "O valor do serviço pago não pode ser nulo.")
        @Positive(message = "O valor do serviço pago deve ser maior que zero.")
        BigDecimal valorPago,

        @NotNull(message = "A forma de pagamento não pode ser nula.")
        @NotBlank(message = "A forma de pagamento não pode ser vazia.")
        @Size(min = 3, max = 100, message = "A forma de pagamento deve ter entre 3 e 100 caracteres.")
        String formaPagamento,

        @NotNull(message = "A biometria não pode ser nulo.")
        @Positive(message = "A biometria não pode ser negativo.")
        Long biometria
) {
        public Pagamento toEntity(Conta conta, Set<Taxa> taxas) {
                return Pagamento.builder()
                        .conta(conta)
                        .servico(servico)
                        .valorPago(valorPago)
                        .dataPagamento(LocalDateTime.now())
                        .status(StatusPagamento.EM_ANDAMENTO)
                        .taxas(taxas)
                        .formaPagamento(FormaPagamento.valueOf(String.valueOf(formaPagamento)))
                        .build();
        }
}