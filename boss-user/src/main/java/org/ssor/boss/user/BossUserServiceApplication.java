package org.ssor.boss.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.ssor.boss.core.configuration.PasswordConfiguration;
import org.ssor.boss.core.repository.UserRepository;
import org.ssor.boss.core.service.UserService;

import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@EnableJpaRepositories(basePackages = { "org.ssor.boss.core.repository" })
@EntityScan(basePackages = { "org.ssor.boss.core.entity" })
@Import({PasswordConfiguration.class})
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
public class BossUserServiceApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(BossUserServiceApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public UserService userService()
	{
		return new UserService(userRepository, passwordEncoder);
	}
}
