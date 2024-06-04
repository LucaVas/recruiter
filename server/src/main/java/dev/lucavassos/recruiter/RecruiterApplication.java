package dev.lucavassos.recruiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "dev.lucavassos.recruiter.modules")
public class RecruiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecruiterApplication.class, args);
    }
}
