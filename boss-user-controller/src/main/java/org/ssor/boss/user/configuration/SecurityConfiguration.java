package org.ssor.boss.user.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

  // I have some concerns about the security constraints of the POST methods for both **/registration and
  // **/confirmation. I'm not sure if these should be permit all. Seems like something only the system should be able to
  // do. And the same kind of goes for PUT or DELETE **/users/{user_id}. Lemme know if you think I made the right
  // decision or not. -- John
  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    http.authorizeRequests()
        .antMatchers(HttpMethod.GET, "/api/v*/users").hasAuthority("USER_VENDOR")
        .antMatchers(HttpMethod.POST, "/api/v*/users/registration").permitAll()
        .antMatchers(HttpMethod.POST, "/api/v*/users/confirmation").permitAll()
        .antMatchers(HttpMethod.GET, "/api/v*/users/{\\d+}").hasAnyAuthority("USER_DEFAULT", "USER_VENDOR")
        .antMatchers(HttpMethod.PUT, "/api/v*/users/{\\d+}").hasAuthority("USER_DEFAULT")
        .antMatchers(HttpMethod.DELETE, "/api/v*/users/{\\d+}").hasAuthority("USER_DEFAULT")
        .anyRequest().authenticated().and().formLogin();
  }
}
