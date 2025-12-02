package com.senai.novo_conta_bancaria.interface_ui.controller;

import com.senai.novo_conta_bancaria.application.dto.taxa.TaxaAtualizacaoDto;
import com.senai.novo_conta_bancaria.application.dto.taxa.TaxaRegistroDto;
import com.senai.novo_conta_bancaria.application.dto.taxa.TaxaResponseDto;
import com.senai.novo_conta_bancaria.application.service.TaxaService;
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

import java.net.URI;
import java.util.List;

@Tag(name = "Taxas", description = "Gerenciamento de taxas do banco.")
@RestController
@RequestMapping("/api/taxa")
@RequiredArgsConstructor
public class TaxaController {
    private final TaxaService service;

    // Create
    @Operation(
            summary = "Cadastrar uma nova taxa",
            description = "Adiciona uma nova taxa à base de dados após validações.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TaxaRegistroDto.class),
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                        {
                                          "descricao": "Tarifa Bancária",
                                          "percentual": 0,
                                          "valorFixo": 10,
                                          "formaPagamento": "BOLETO"
                                        }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Taxa cadastrada com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Descrição inválida",
                                                    value = "\"A descrição deve ter entre 3 e 100 caracteres.\""),
                                            @ExampleObject(
                                                    name = "Percentual inválido",
                                                    value = "\"O percentual não deve ser negativo.\"")
                                    }
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<TaxaResponseDto> registrarTaxa(@Valid @RequestBody TaxaRegistroDto dto) {
        return ResponseEntity // retorna o código de status
                .created(URI.create("api/taxa")) // status code: 201 (criado com êxito)
                .body(service.registrarTaxa(dto));
    }

    // Read
    @Operation(
            summary = "Listar todas as taxas",
            description = "Retorna todas as taxas cadastradas na base de dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<TaxaResponseDto>> listarTodasAsTaxas() {
        return ResponseEntity
                .ok(service.listarTodasAsTaxas()); // status code: 200 (encontrado com êxito)
    }

    @Operation(
            summary = "Buscar taxa por ID",
            description = "Retorna uma taxa cadastrada na base de dados a partir do seu ID.",
            parameters = {
                    @Parameter(name = "id", description = "ID da taxa a ser buscada",
                            example = "b73157ab-4b6e-4748-8abc-0609f2f1c522"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Taxa encontrada."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Taxa não encontrada.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Taxa com ID " +
                                            "b73157ab-4b6e-4748-8abc-0609f2f1c522 não encontrada.\""
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaxaResponseDto> buscarTaxa(@PathVariable String id) {
        return ResponseEntity
                .ok(service.buscarTaxa(id));
    }

    // Update
    @Operation(
            summary = "Atualizar uma taxa",
            description = "Atualiza os dados de uma taxa existente com novas informações.",
            parameters = {
                    @Parameter(name = "id", description = "ID da taxa a ser atualizada",
                            example = "b73157ab-4b6e-4748-8abc-0609f2f1c522"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TaxaAtualizacaoDto.class),
                            examples = @ExampleObject(name = "Exemplo de atualização", value = """
                                        {
                                          "descricao": "Tarifa Bancária",
                                          "percentual": 0,
                                          "valorFixo": 10,
                                          "formaPagamento": "BOLETO"
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Taxa atualizada com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Descrição inválida",
                                                    value = "\"A descrição deve ter entre 3 e 100 caracteres.\""),
                                            @ExampleObject(
                                                    name = "Percentual inválido",
                                                    value = "\"O percentual não deve ser negativo.\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Taxa não encontrada.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Taxa com ID " +
                                            "b73157ab-4b6e-4748-8abc-0609f2f1c522 não encontrada.\""
                                    )
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TaxaResponseDto> atualizarTaxa(@PathVariable String id,
                                                         @Valid @RequestBody TaxaAtualizacaoDto dto) {
        return ResponseEntity
                .ok(service.atualizarTaxa(id, dto));
    }

    // Delete
    @Operation(
            summary = "Apagar uma taxa",
            description = "Remove uma taxa da base de dados a partir do seu ID.",
            parameters = {
                    @Parameter(name = "id", description = "ID da taxa a ser apagado.",
                            example = "b73157ab-4b6e-4748-8abc-0609f2f1c522"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Taxa removida com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Taxa não encontrada.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Taxa com ID " +
                                            "b73157ab-4b6e-4748-8abc-0609f2f1c522 não encontrado.\""
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarTaxa(@PathVariable String id) {
        service.apagarTaxa(id);
        return ResponseEntity
                .noContent() // status code: 204 (encontrado, sem conteúdo)
                .build();
    }
}