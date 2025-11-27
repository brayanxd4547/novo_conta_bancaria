package com.senai.novo_conta_bancaria.interface_ui.controller;

import com.senai.novo_conta_bancaria.application.dto.ContaResumoDto;
import com.senai.novo_conta_bancaria.application.dto.PagamentoRegistroDto;
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
                                        }
                                    """ // TODO
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
                                            @ExampleObject( // TODO
                                                    name = "Nome inválido",
                                                    value = "\"Preço mínimo do serviço deve ser R$ 50,00\""),
                                            @ExampleObject(
                                                    name = "CPF inválido",
                                                    value = "\"Duração do serviço não pode exceder 30 dias\"")
                                    }
                            )
                    )
            }
    )
    @PostMapping("/{numeroConta}")
    public ResponseEntity<ContaResumoDto> pagar(@PathVariable Long numero, @Valid @RequestBody PagamentoRegistroDto dto) {
        return ResponseEntity
                .ok(pagamentoAppService.pagar(numero, dto));
    }
}
