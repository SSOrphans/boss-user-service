package org.ssor.boss.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.ssor.*")
@EntityScan(basePackages = "org.ssor.boss.*")
@EnableJpaRepositories(basePackages = "org.ssor.boss.*")
public class BossUserServiceApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(BossUserServiceApplication.class, args);
	}
}
