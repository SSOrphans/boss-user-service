/**
 * 
 */
package org.ssor.boss.user.security;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Christian Angeles
 *
 */
@ExtendWith(SpringExtension.class)
public class JwtForgotPassTokenTest {

	@TestConfiguration
	public static class JwtTestContextConfig {
		@Bean
		public JwtForgotPassToken jwtForgotPassToken() {
			return new JwtForgotPassToken();
		}
	}

	@Autowired
	JwtForgotPassToken jwtForgotPassToken;

	@Test
	public void generateAccessTokenTest() {
		assertNotNull(jwtForgotPassToken.generateAccessToken("sample@ss.com"));
	}

	@Test
	public void getUserEmailTest() {
		String token = jwtForgotPassToken.generateAccessToken("sample@ss.com");
		String email = jwtForgotPassToken.getUserEmail(token);
		assertEquals(email, "sample@ss.com");
	}

	@Test
	public void getIssueDateTest() {
		String token = jwtForgotPassToken.generateAccessToken("sample@ss.com");
		assertNotNull(jwtForgotPassToken.getIssueDate(token));
	}

	@Test
	public void getExpirationDateTest() {
		String token = jwtForgotPassToken.generateAccessToken("sample@ss.com");
		assertNotNull(jwtForgotPassToken.getExpirationDate(token));
	}
	
	@Test
	public void validateTest() {
		String token = jwtForgotPassToken.generateAccessToken("sample@ss.com");
		assertTrue(jwtForgotPassToken.validate(token));
	}
}
