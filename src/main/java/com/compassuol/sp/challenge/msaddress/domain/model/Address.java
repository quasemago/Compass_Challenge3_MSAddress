package com.compassuol.sp.challenge.msaddress.domain.model;

import com.compassuol.sp.challenge.msaddress.web.dto.AddressResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "cep", nullable = false)
    private String cep;

    public Address(String street, String city, String state, String cep) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.cep = cep;
    }

    public AddressResponseDTO toDTO() {
        return new AddressResponseDTO(
                this.street,
                this.city,
                this.state,
                this.cep);
    }
}
