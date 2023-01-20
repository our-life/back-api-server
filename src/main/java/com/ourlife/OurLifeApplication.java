package com.ourlife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ConfigurationPropertiesScan("com.ourlife")
@SpringBootApplication
@EnableJpaAuditing
public class OurLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OurLifeApplication.class, args);
    }

}
