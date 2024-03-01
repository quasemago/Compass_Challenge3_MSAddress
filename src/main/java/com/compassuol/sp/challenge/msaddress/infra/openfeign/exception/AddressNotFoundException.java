package com.compassuol.sp.challenge.msaddress.infra.openfeign.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String message) {
        super(message);
    }
}
