package com.compassuol.sp.challenge.msaddress.web;

import com.compassuol.sp.challenge.msaddress.domain.model.Address;
import com.compassuol.sp.challenge.msaddress.domain.service.AddressService;
import com.compassuol.sp.challenge.msaddress.infra.openfeign.exception.AddressNotFoundException;
import com.compassuol.sp.challenge.msaddress.web.controller.AddressController;
import com.compassuol.sp.challenge.msaddress.web.dto.AddressResponseDTO;
import com.compassuol.sp.challenge.msaddress.web.exception.AddressFormatNotValidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.compassuol.sp.challenge.msaddress.common.AddressUtils.mockValidAddress;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(AddressController.class)
public class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AddressService service;

    @Test
    public void getAddressByCep_WithValidData_ReturnsAddressResponse() throws Exception {
        final Address validAddress = mockValidAddress();

        when(service.findOrCreateAddressByCep(anyString())).thenReturn(validAddress);
        final AddressResponseDTO responseBody = validAddress.toDTO();

        mockMvc.perform(
                        get("/v1/address/{cep}", "01001-000")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseBody)));

        verify(service, times(1)).findOrCreateAddressByCep(anyString());
    }

    @Test
    public void getAddressByCep_WithInvalidData_ReturnsBadRequest() throws Exception {
        when(service.findOrCreateAddressByCep(anyString())).thenThrow(AddressFormatNotValidException.class);

        mockMvc.perform(
                        get("/v1/address/{cep}", "0100-000")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());

        verify(service, times(1)).findOrCreateAddressByCep(anyString());
    }

    @Test
    public void getAddressByCep_WithNonExistingCep_ReturnsNotFound() throws Exception {
        when(service.findOrCreateAddressByCep(anyString())).thenThrow(AddressNotFoundException.class);

        mockMvc.perform(
                        get("/v1/address/{cep}", "01000-000")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        verify(service, times(1)).findOrCreateAddressByCep(anyString());
    }

    @Test
    public void getAddressByCep_WithEmptyCep_ReturnsInternalServerError() throws Exception {
        mockMvc.perform(
                        get("/v1/address/{cep}", "")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());
    }
}
