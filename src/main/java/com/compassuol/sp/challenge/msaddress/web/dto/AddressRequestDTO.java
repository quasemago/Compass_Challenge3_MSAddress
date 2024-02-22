package com.compassuol.sp.challenge.msaddress.web.dto;

import com.compassuol.sp.challenge.msaddress.domain.model.Address;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressRequestDTO {
    @JsonProperty("logradouro")
    private String street;
    @JsonProperty("localidade")
    private String city;
    @JsonProperty("uf")
    private String state;
    @JsonProperty("cep")
    private String cep;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String erro;

    public Address toModel() {
        return new Address(
                this.street,
                this.city,
                this.state,
                this.cep);
    }
}
