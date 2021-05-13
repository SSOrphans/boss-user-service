package org.ssor.boss.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.ssor.boss.core.service.UserService;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@EnableEurekaClient
@SpringBootApplication
@ComponentScan("org.ssor.boss")
@EntityScan("org.ssor.boss.core.entity")
@EnableJpaRepositories("org.ssor.boss.core.repository")
public class BossUserControllerApplication
{

  public static void main(String[] args)
  {
    SpringApplication.run(BossUserControllerApplication.class, args);
  }

  /**
   * Provides an object mapper for the controller to use.
   *
   * @return A new object mapper instance.
   */
  @Bean
  public ObjectMapper getObjectMapper()
  {
    return new ObjectMapper();
  }
}
