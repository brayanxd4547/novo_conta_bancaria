package com.senai.novo_conta_bancaria.interface_ui.controller;

import com.senai.novo_conta_bancaria.application.dto.pagamento.PagamentoRegistroDto;
import com.senai.novo_conta_bancaria.application.dto.pagamento.PagamentoResponseDto;
import com.senai.novo_conta_bancaria.application.service.PagamentoAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pagamentos", description = "Gerenciamento de pagamentos do banco.")
@RestController
@RequestMapping("/api/pagamento")
@RequiredArgsConstructor
public class PagamentoController {
    private final PagamentoAppService pagamentoAppService;

    @Operation(
            summary = "Realizar um pagamento",
            description = "Retira um valor da conta para pagar um serviço por meio de uma forma de pagamento.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PagamentoRegistroDto.class),
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                        {
                                            "servico": "SENAI",
                                            "valorPago": 100,
                                            "formaPagamento": "BOLETO"
                                        }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pagamento realizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Nome do serviço inválido",
                                                    value = "\"O nome do serviço precisa ter entre 3 e 100 caracteres.\""),
                                            @ExampleObject(
                                                    name = "Forma de pagamento inválida",
                                                    value = "\"A forma de pagamento não foi encontrada.\"")
                                    }
                            )
                    )
            }
    )
    @PostMapping("/{numeroConta}")
    public ResponseEntity<PagamentoResponseDto> pagar(@PathVariable Long numeroConta, @Valid @RequestBody PagamentoRegistroDto dto) {
        return ResponseEntity
                .ok(pagamentoAppService.pagar(numeroConta, dto));
    }
}
