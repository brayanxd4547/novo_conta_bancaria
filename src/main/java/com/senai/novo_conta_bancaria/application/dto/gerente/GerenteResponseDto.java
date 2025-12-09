package com.senai.novo_conta_bancaria.application.dto.gerente;

import com.senai.novo_conta_bancaria.domain.entity.Gerente;
import jakarta.validation.constraints.*;

public record GerenteResponseDto(
        String id,
        String nome,
        Long cpf,
        String email,
        String senha
) {
    public static GerenteResponseDto fromEntity(Gerente gerente) {
        return new GerenteResponseDto(
                gerente.getId(),
                gerente.getNome(),
                gerente.getCpf(),
                gerente.getEmail(),
                gerente.getSenha()
        );
    }
}