package com.compassuol.sp.challenge.msaddress.infra.openfeign.client;

import com.compassuol.sp.challenge.msaddress.web.dto.AddressRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep-api", url = "https://viacep.com.br/ws")
public interface ViaCepClientConsumer {
    @GetMapping("/{cep}/json")
    AddressRequestDTO getAddressByCep(@PathVariable("cep") String cep);
}
