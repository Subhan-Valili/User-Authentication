package az.ingress.userauthenticationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableAsync
@EnableWebSecurity
@EnableJpaRepositories(basePackages = "az.ingress.userauthenticationsystem.repository.jpa")
@EnableRedisRepositories(basePackages = "az.ingress.userauthenticationsystem.repository.redis")
public class UserAuthenticationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAuthenticationSystemApplication.class, args);
    }

}
