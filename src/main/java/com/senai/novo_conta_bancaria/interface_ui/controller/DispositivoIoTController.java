package com.senai.novo_conta_bancaria.interface_ui.controller;

import com.senai.novo_conta_bancaria.application.dto.dispositivo_iot.DispositivoIoTRegistroDTO;
import com.senai.novo_conta_bancaria.application.dto.dispositivo_iot.DispositivoIoTResponseDTO;
import com.senai.novo_conta_bancaria.application.service.DispositivoIoTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "DispositivoIoTs", description = "Gerenciamento de DispositivoIoTs do banco.")
@RestController
@RequestMapping("/api/DispositivoIoT")
@RequiredArgsConstructor
public class DispositivoIoTController {
    private final DispositivoIoTService service;

    // Create: embutido em Cliente

    // Read
    @Operation(
            summary = "Listar todos os DispositivoIoTs",
            description = "Retorna todos os DispositivoIoTs cadastrados na base de dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<DispositivoIoTResponseDTO>> listarTodosOsDispositivoIoTs() {
        return ResponseEntity
                .ok(service.listarTodosOsDispositivoIoTs()); // status code: 200 (encontrado com êxito)
    }

    @Operation(
            summary = "Buscar DispositivoIoT por CPF",
            description = "Retorna um DispositivoIoT cadastrado na base de dados a partir do seu CPF.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do DispositivoIoT a ser buscado", example = "12345678910")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "DispositivoIoT encontrado."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "DispositivoIoT não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"DispositivoIoT com CPF 19876543210 não encontrado.\"")
                            )
                    )
            }
    )
    @GetMapping("/{codigoSerial}")
    public ResponseEntity<DispositivoIoTResponseDTO> buscarDispositivoIoT(@PathVariable String codigoSerial) {
        return ResponseEntity
                .ok(service.buscarDispositivoIoT(codigoSerial));
    }

    // Update
    @Operation(
            summary = "Atualizar um DispositivoIoT",
            description = "Atualiza os dados de um DispositivoIoT existente com novas informações.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do DispositivoIoT a ser atualizado", example = "12345678910")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = DispositivoIoTRegistroDTO.class),
                            examples = @ExampleObject(name = "Exemplo de atualização", value = """
                                    {
                                        "nome": "José Silva dos Santos",
                                        "cpf": 12345678910,
                                        "email": "jose@email.com",
                                        "senha": "JoseDosSantos1234"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "DispositivoIoT atualizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Nome inválido",
                                                    value = "\"O nome deve ter entre 3 e 100 caracteres.\""),
                                            @ExampleObject(
                                                    name = "CPF inválido",
                                                    value = "\"O CPF deve ter até 11 digitos.\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "DispositivoIoT não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"DispositivoIoT com ID 19876543210 não encontrado.\"")
                            )
                    )
            }
    )
    @PutMapping("/{codigoSerial}")
    public ResponseEntity<DispositivoIoTResponseDTO> atualizarDispositivoIoT(@PathVariable String codigoSerial,
                                                               @Valid @RequestBody DispositivoIoTRegistroDTO dto) {
        return ResponseEntity
                .ok(service.atualizarDispositivoIoT(codigoSerial, dto));
    }

    // Delete: permitido apenas pela deleção do Cliente
}