package com.senai.novo_conta_bancaria.interface_ui.controller;

import com.senai.novo_conta_bancaria.application.dto.AuthDto;
import com.senai.novo_conta_bancaria.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Autenticação do usuário no sistema.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;

    @Operation(
            summary = "Login",
            description = "Inserção das credenciais necessárias para autenticar-se no sistema. ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AuthDto.LoginRequest.class),
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                        {
                                          "email": "admin@senai.com",
                                          "senha": "admin123"
                                        }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticado com êxito."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Endereço de e-mail inválido",
                                                    value = "\"O texto inserido não corresponde a um endereço de e-mail.\""),
                                            @ExampleObject(
                                                    name = "Senha inválida",
                                                    value = "\"SLAAAA\"")
                                    }
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthDto.TokenResponse> login(@RequestBody AuthDto.LoginRequest req) {
        String token = auth.login(req);
        return ResponseEntity.ok(new AuthDto.TokenResponse(token));
    }
}