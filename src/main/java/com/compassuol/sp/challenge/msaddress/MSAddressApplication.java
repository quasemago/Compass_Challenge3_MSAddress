package com.compassuol.sp.challenge.msaddress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MSAddressApplication {

    public static void main(String[] args) {
        SpringApplication.run(MSAddressApplication.class, args);
    }

}
