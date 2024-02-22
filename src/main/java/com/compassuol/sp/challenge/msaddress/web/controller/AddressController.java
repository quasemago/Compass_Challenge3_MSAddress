package com.compassuol.sp.challenge.msaddress.web.controller;

import com.compassuol.sp.challenge.msaddress.domain.service.AddressService;
import com.compassuol.sp.challenge.msaddress.web.dto.AddressResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService service;

    @GetMapping("/{cep}")
    public ResponseEntity<AddressResponseDTO> getAddressByCep(@PathVariable("cep") String cep) {
        final AddressResponseDTO response = service.findOrCreateAddressByCep(cep).toDTO();
        return ResponseEntity.ok(response);
    }
}
