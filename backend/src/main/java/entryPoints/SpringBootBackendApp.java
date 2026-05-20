package entryPoints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"backend"})
public class SpringBootBackendApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootBackendApp.class, args);
    }
}
