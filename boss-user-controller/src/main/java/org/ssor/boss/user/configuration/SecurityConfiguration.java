package org.ssor.boss.user.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.ssor.boss.core.service.UserService;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider()
  {
    final var authProvider = new DaoAuthenticationProvider();
    authProvider.setPasswordEncoder(passwordEncoder);
    authProvider.setUserDetailsService(userService);
    return authProvider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception
  {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    http.authorizeRequests()
        .antMatchers("/user").hasAuthority("USER_DEFAULT")
        .antMatchers("/api/v*/users").hasAuthority("USER_DEFAULT")
        .antMatchers("/api/v*/users/registration").permitAll()
        .antMatchers("/api/v*/users/{\\d+}").permitAll()
        .anyRequest().authenticated().and().formLogin();
  }
}
