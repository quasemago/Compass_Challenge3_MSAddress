package com.compassuol.sp.challenge.msaddress.domain;

import com.compassuol.sp.challenge.msaddress.domain.model.Address;
import com.compassuol.sp.challenge.msaddress.domain.repository.AddressRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.compassuol.sp.challenge.msaddress.common.AddressUtils.VALID_ADDRESS;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AddressRepositoryTest {
    @Autowired
    private AddressRepository repository;
    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    public void afterEach() {
        VALID_ADDRESS.setId(null);
    }

    @Test
    public void findAddressByCep_WithExistingAddress_ReturnsAddress() {
        final Address savedAddress = testEntityManager.persistFlushFind(VALID_ADDRESS);

        Optional<Address> sutAddress = repository.findByCep(VALID_ADDRESS.getCep());

        assertThat(sutAddress).isNotEmpty();
        assertThat(sutAddress.get()).isEqualTo(savedAddress);
    }

    @Test
    public void findAddressByCep_WithNonExistingAddress_ReturnsEmpty() {
        Optional<Address> sutAddress = repository.findByCep("01001-200");
        assertThat(sutAddress).isEmpty();
    }
}
