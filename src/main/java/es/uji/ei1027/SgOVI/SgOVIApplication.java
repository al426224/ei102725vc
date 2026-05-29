package es.uji.ei1027.SgOVI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SgOVIApplication {

    public static void main(String[] args) {
        SpringApplication.run(SgOVIApplication.class, args);
    }
}
