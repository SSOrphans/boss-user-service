package org.ssor.boss.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
public class BossUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BossUserServiceApplication.class, args);
	}
}
