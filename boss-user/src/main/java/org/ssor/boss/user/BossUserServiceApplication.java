package org.ssor.boss.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.ssor.boss.core.configuration.PasswordConfiguration;
import org.ssor.boss.core.service.ConfirmationService;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@SpringBootApplication
@Import({ PasswordConfiguration.class, ConfirmationService.class })
@EntityScan({ "org.ssor.boss.core.entity" })
@EnableJpaRepositories({ "org.ssor.boss.core.repository" })
public class BossUserServiceApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(BossUserServiceApplication.class, args);
	}
}
