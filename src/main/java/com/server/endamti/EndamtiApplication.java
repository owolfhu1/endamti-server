package com.server.endamti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class EndamtiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EndamtiApplication.class, args);
    }

}
