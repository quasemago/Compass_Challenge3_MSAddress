package com.compassuol.sp.challenge.msaddress.web.controller;

import com.compassuol.sp.challenge.msaddress.domain.service.AddressService;
import com.compassuol.sp.challenge.msaddress.security.jwt.JwtTokenService;
import com.compassuol.sp.challenge.msaddress.web.dto.AddressResponseDTO;
import com.compassuol.sp.challenge.msaddress.web.dto.ErrorMessageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Address", description = "API Address - Tem como objetivo fornecer informações de endereços a partir de um CEP.")
@RestController
@RequestMapping("/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService service;
    private final JwtTokenService jwtService;

    @Operation(summary = "Recuperar informações de um endereço.",
            description = "Recurso para recuperar um endereço através do CEP (Código Postal) ou Id (Identificador).",
            security = @SecurityRequirement(name = "BearerTokenAuthentication"),
            parameters = {
                    @Parameter(name = "value", description = "CEP ou Id do endereço para consulta.",
                            in = ParameterIn.PATH, required = true),
                    @Parameter(name = "Authorization", description = "Bearer Token para autenticação.",
                            in = ParameterIn.HEADER, required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço recuperado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Recurso não processado devido a requisição inválida.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDTO.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Recurso não processado devido a falha na autenticação.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado para o CEP informado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDTO.class))
                    )
            }
    )
    @GetMapping("/{value}")
    public ResponseEntity<?> getOrCreateAddress(@PathVariable("value") String value,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            final String jwtToken = authorizationHeader.substring(7);
            if (jwtService.resolveToken(jwtToken) != null) {
                final AddressResponseDTO response = service.findOrCreateAddress(value).toDTO();
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }
}
