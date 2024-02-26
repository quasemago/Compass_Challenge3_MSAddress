package com.compassuol.sp.challenge.msaddress.web.exception.handler;

import com.compassuol.sp.challenge.msaddress.infra.openfeign.exception.AddressNotFoundException;
import com.compassuol.sp.challenge.msaddress.web.dto.ErrorMessageDTO;
import com.compassuol.sp.challenge.msaddress.web.exception.AddressFormatNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleAddressNotFoundException(AddressNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessageDTO(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(AddressFormatNotValidException.class)
    public ResponseEntity<ErrorMessageDTO> handleAddressFormatNotValidException(AddressFormatNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessageDTO(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDTO> handleGenericException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado."));
    }
}
