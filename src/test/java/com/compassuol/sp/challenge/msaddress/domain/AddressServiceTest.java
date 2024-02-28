package com.compassuol.sp.challenge.msaddress.domain;

import com.compassuol.sp.challenge.msaddress.domain.model.Address;
import com.compassuol.sp.challenge.msaddress.domain.repository.AddressRepository;
import com.compassuol.sp.challenge.msaddress.domain.service.AddressService;
import com.compassuol.sp.challenge.msaddress.infra.openfeign.client.ViaCepClientConsumer;
import com.compassuol.sp.challenge.msaddress.infra.openfeign.exception.AddressNotFoundException;
import com.compassuol.sp.challenge.msaddress.web.exception.AddressFormatNotValidException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.compassuol.sp.challenge.msaddress.common.AddressUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
    @InjectMocks
    private AddressService service;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ViaCepClientConsumer viaCepClientConsumer;

    @Test
    public void findOrCreateAddress_WithValidDataAndExistingAddress_ReturnsAddress() {
        final Address validAddress = mockValidAddress();

        when(addressRepository.findByCep(anyString())).thenReturn(Optional.of(validAddress));

        Address sut = service.findOrCreateAddress("01001-000");

        assertThat(sut).isNotNull();
        assertThat(sut).isEqualTo(validAddress);
    }

    @Test
    public void findOrCreateAddress_WithExistingAddressId_ReturnsAddress() {
        final Address validAddress = mockValidAddress();

        when(addressRepository.findById(1L)).thenReturn(Optional.of(validAddress));

        Address sut = service.findOrCreateAddress("1");

        assertThat(sut).isNotNull();
        assertThat(sut).isEqualTo(validAddress);
    }

    @Test
    public void findOrCreateAddress_WithNonExistingAddressId_ThrowsException() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findOrCreateAddress("1"))
                .isInstanceOf(AddressNotFoundException.class);
    }

    @Test
    public void findOrCreateAddress_WithValidDataAndNonExistingAddress_ReturnsAddress() {
        final Address validAddress = mockValidAddress();

        when(addressRepository.findByCep(anyString())).thenReturn(Optional.empty());
        when(viaCepClientConsumer.getAddressByCep(anyString())).thenReturn(mockAddressRequestDTO());
        when(addressRepository.save(validAddress)).thenReturn(validAddress);

        Address sut = service.findOrCreateAddress("01001-000");

        assertThat(sut).isNotNull();
        assertThat(sut).isEqualTo(validAddress);
    }

    @Test
    public void findOrCreateAddress_WithValidDataAndNonExistingCep_ThrowsException() {
        when(addressRepository.findByCep(anyString())).thenReturn(Optional.empty());
        when(viaCepClientConsumer.getAddressByCep(anyString())).thenReturn(mockInvalidAddressRequestDTO());

        assertThatThrownBy(() -> service.findOrCreateAddress("00000-000"))
                .isInstanceOf(AddressNotFoundException.class);
    }

    @Test
    public void findOrCreateAddress_WithInvalidFormat_ThrowsException() {
        final String invalidCep = "01001-00";

        assertThatThrownBy(() -> service.findOrCreateAddress(invalidCep))
                .isInstanceOf(AddressFormatNotValidException.class);
    }
}
