package com.ourlife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("com.ourlife")
@SpringBootApplication
public class OurLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OurLifeApplication.class, args);
    }

}
