package com.compassuol.sp.challenge.msaddress.domain.repository;

import com.compassuol.sp.challenge.msaddress.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByCep(String cep);
}