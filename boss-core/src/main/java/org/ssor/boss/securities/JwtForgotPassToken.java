package org.ssor.boss.securities;

import static java.lang.String.format;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtForgotPassToken {

	private final String secret = "c2VjcmV0Ym9zc2tleQ==";

	public String generateAccessToken(String email) {
		return Jwts.builder().setSubject(format("%s", email)).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 minutes
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public String getUserEmail(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

	public Date getIssueDate(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

		return claims.getIssuedAt();
	}

	public Date getExpirationDate(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

		return claims.getExpiration();
	}

	public boolean validate(String token) throws SignatureException, MalformedJwtException, ExpiredJwtException,
			UnsupportedJwtException, IllegalArgumentException {
		if (Jwts.parser().setSigningKey(secret).parseClaimsJws(token) != null) {
			return true;
		}
		return false;
	}
}
