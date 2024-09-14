package org.example.uberprojectsocketserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EntityScan("org.example.uberprojectentityservice.models")
public class UberProjectSocketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberProjectSocketServerApplication.class, args);
    }

}
