package org.ssor.boss.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
//@EnableJpaRepositories(basePackages = { "org.ssor.boss.core.repositories" })
//@EntityScan(basePackages = { "org.ssor.boss.core.entities" })
public class BossUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BossUserServiceApplication.class, args);
	}

//	@Bean
//	public JwtForgotPassToken jwtForgotPassToken() {
//		return new JwtForgotPassToken();
//	}
//
//	@Bean
//	public UserService userServices() {
//		return new UserService();
//	}
}
