package com.senai.novo_conta_bancaria.application.dto;

public class AuthDto {
    public record LoginRequest(
            String email,
            String senha
    ) {
    }

    public record TokenResponse(
            String token
    ) {
    }
}