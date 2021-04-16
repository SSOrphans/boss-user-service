package org.ssor.boss.user.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserInfoDtoTest {

	private static UserInfoDto userActual, userExpected, userNotExpected;

	@BeforeAll
	@Test
	public static void setUserDto() {
		LocalDateTime time = LocalDateTime.now();
		userActual = UserInfoDto.builder().userId(1).displayName("Test").email("test@ss.com").created(time).build();
		userExpected = UserInfoDto.builder().userId(1).displayName("Test").email("test@ss.com").created(time).build();
		userNotExpected = UserInfoDto.builder().build();
		userNotExpected.setUserId(2);
		userNotExpected.setDisplayName("Sample");
		userNotExpected.setEmail("sample@ss.com");
		userNotExpected.setCreated(LocalDateTime.of(2077, 1, 1, 0, 0));
	}

	@Test
	public void equalsToTest() {
		assertEquals(userExpected.getUserId(), userActual.getUserId());
		assertEquals(userExpected.getDisplayName(), userActual.getDisplayName());
		assertEquals(userExpected.getEmail(), userActual.getEmail());
		assertEquals(userExpected.getCreated(), userActual.getCreated());
	}

	@Test
	public void notEqualsToTest() {
		assertNotEquals(userNotExpected, userActual);
		assertNotEquals(userNotExpected.getUserId(), userActual.getUserId());
		assertNotEquals(userNotExpected.getDisplayName(), userActual.getDisplayName());
		assertNotEquals(userNotExpected.getEmail(), userActual.getEmail());
	}
}
