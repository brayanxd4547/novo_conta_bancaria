package com.senai.novo_conta_bancaria.interface_ui.controller;

import com.senai.novo_conta_bancaria.application.dto.dispositivo_iot.DispositivoIoTAtualizacaoDTO;
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

@Tag(name = "dispositivosIoT", description = "Gerenciamento de dispositivos IoT do banco.")
@RestController
@RequestMapping("/api/dispositivoIoT")
@RequiredArgsConstructor
public class DispositivoIoTController {
    private final DispositivoIoTService service;

    // Create: embutido em Cliente

    // Read
    @Operation(
            summary = "Listar todos os dispositivos IoT",
            description = "Retorna todos os dispositivos IoT cadastrados na base de dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<DispositivoIoTResponseDTO>> listarTodosOsDispositivosIoT() {
        return ResponseEntity
                .ok(service.listarTodosOsDispositivosIoT()); // status code: 200 (encontrado com êxito)
    }

    @Operation(
            summary = "Buscar dispositivo IoT por código serial",
            description = "Retorna um dispositivo IoT cadastrado na base de dados a partir do seu código serial.",
            parameters = {
                    @Parameter(
                            name = "codigoSerial",
                            description = "Código serial do dispositivo IoT a ser buscado",
                            example = "AAAA-BBBB-CCCC-DDDD"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dispositivo IoT encontrado."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Dispositivo IoT não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "\"Dispositivo IoT com código serial AAAA-BBBB-CCCC-DDDD não " +
                                                    "encontrado.\""
                                    )
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
            summary = "Atualizar um dispositivo IoT",
            description = "Atualiza os dados de um dispositivo IoT existente com novas informações.",
            parameters = {
                    @Parameter(
                            name = "codigoSerial",
                            description = "Código serial do dispositivo IoT a ser atualizado",
                            example = "AAAA-BBBB-CCCC-DDDD"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = DispositivoIoTRegistroDTO.class),
                            examples = @ExampleObject(name = "Exemplo de atualização", value = """
                                    {
                                        "codigoSerial": "AAAABBBBCCCCDDDD",
                                        "chavePublica": "abcdefghijklmnopqrstuvwxyz",
                                        "clienteCPF": 12345678910
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dispositivo IoT atualizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Código serial inválido",
                                                    value = "\"O código serial deve ter exatamente 19 caracteres " +
                                                            "(contando com hífens).\""),
                                            @ExampleObject(
                                                    name = "Chave pública inválida",
                                                    value = "\"A chave pública deve ter entre 3 e 100 caracteres.\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Dispositivo IoT não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "\"Dispositivo IoT com código serial AAAA-BBBB-CCCC-DDDD não " +
                                                    "encontrado.\""
                                    )
                            )
                    )
            }
    )
    @PutMapping("/{codigoSerial}")
    public ResponseEntity<DispositivoIoTResponseDTO> atualizarDispositivoIoT(@PathVariable String codigoSerial,
                                                               @Valid @RequestBody DispositivoIoTAtualizacaoDTO dto) {
        return ResponseEntity
                .ok(service.atualizarDispositivoIoT(codigoSerial, dto));
    }

    // Delete: permitido apenas pela remoção do Cliente
}