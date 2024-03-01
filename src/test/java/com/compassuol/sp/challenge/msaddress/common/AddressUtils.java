package com.compassuol.sp.challenge.msaddress.common;

import com.compassuol.sp.challenge.msaddress.domain.model.Address;
import com.compassuol.sp.challenge.msaddress.web.dto.AddressRequestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressUtils {
    public static Address VALID_ADDRESS = new Address("Praça da Sé", "São Paulo", "SP", "01001-000");
    public static String VALID_JWT_SECRETKEY = "217969690605818764702766367087001";

    public static Address mockValidAddress() {
        return Address.builder()
                .street("Praça da Sé")
                .city("São Paulo")
                .state("SP")
                .cep("01001-000")
                .build();
    }

    public static AddressRequestDTO mockAddressRequestDTO() {
        return new AddressRequestDTO("Praça da Sé", "São Paulo", "SP", "01001-000", null);
    }

    public static AddressRequestDTO mockInvalidAddressRequestDTO() {
        return new AddressRequestDTO(null, null, null, null, "erro");
    }

    public static String mockJwtToken() {
        return "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMzIwNzEwMH0.3J";
    }
}
