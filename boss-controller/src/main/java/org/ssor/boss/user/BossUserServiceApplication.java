package org.ssor.boss.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.ssor.boss.core.services.UserService;
import org.ssor.boss.securities.JwtForgotPassToken;

import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@EnableJpaRepositories(basePackages = { "org.ssor.boss.core.repositories" })
@EntityScan(basePackages = { "org.ssor.boss.core.entities" })
public class BossUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BossUserServiceApplication.class, args);
	}

	@Bean
	public JwtForgotPassToken jwtForgotPassToken() {
		return new JwtForgotPassToken();
	}

	@Bean
	public UserService userServices() {
		return new UserService();
	}
}
