package alatoo.edu.kg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BackendProject {
    public static void main(String[] args) {
        SpringApplication.run(BackendProject.class, args);
    }
}