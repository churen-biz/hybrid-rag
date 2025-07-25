package biz.churen.ee.rag.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "biz.churen.ee")
public class HybridRagApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HybridRagApiApplication.class, args);
    }
}
