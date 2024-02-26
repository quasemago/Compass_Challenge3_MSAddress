package com.compassuol.sp.challenge.msaddress.domain.service;

import com.compassuol.sp.challenge.msaddress.domain.model.Address;
import com.compassuol.sp.challenge.msaddress.domain.repository.AddressRepository;
import com.compassuol.sp.challenge.msaddress.infra.openfeign.client.ViaCepClientConsumer;
import com.compassuol.sp.challenge.msaddress.infra.openfeign.exception.AddressNotFoundException;
import com.compassuol.sp.challenge.msaddress.web.dto.AddressRequestDTO;
import com.compassuol.sp.challenge.msaddress.web.exception.AddressFormatNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository repository;
    private final ViaCepClientConsumer viaCepConsumer;

    @Transactional
    public Address findOrCreateAddressByCep(String cep) {
        if (!cep.matches("\\d{5}-\\d{3}")) {
            throw new AddressFormatNotValidException("O formato do CEP deve ser 99999-999.");
        }

        Optional<Address> existingAddress = repository.findByCep(cep);
        if (existingAddress.isPresent()) {
            return existingAddress.get();
        }

        final AddressRequestDTO request = viaCepConsumer.getAddressByCep(cep);
        if (request.getErro() != null) {
            throw new AddressNotFoundException("O CEP informado {" + cep + "} não foi encontrado!");
        }

        final Address savedAddress = new Address(request.getStreet(), request.getCity(), request.getState(), request.getCep());
        return repository.save(savedAddress);
    }
}