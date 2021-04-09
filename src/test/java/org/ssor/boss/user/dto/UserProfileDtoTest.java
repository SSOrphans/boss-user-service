package org.ssor.boss.user.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserProfileDtoTest {

	private static UserProfileDto userActual, userExpected, userNotExpected;

	@BeforeAll
	public static void setUserDto() {
		userActual = UserProfileDto.builder().displayName("Test").email("test@ss.com").password("1234").build();
		userExpected = UserProfileDto.builder().displayName("Test").email("test@ss.com").password("1234").build();
		userNotExpected = UserProfileDto.builder().build();
		userNotExpected.setDisplayName("Sample");
		userNotExpected.setEmail("sample@ss.com");
		userNotExpected.setPassword("4321");
	}

	@Test
	public void equalsToTest() {
		assertEquals(userExpected, userActual);
		assertEquals(userExpected.getDisplayName(), userActual.getDisplayName());
		assertEquals(userExpected.getEmail(), userActual.getEmail());
		assertEquals(userExpected.getPassword(), userActual.getPassword());
	}

	@Test
	public void notEqualsToTest() {
		assertNotEquals(userNotExpected, userActual);
		assertNotEquals(userNotExpected.getDisplayName(), userActual.getDisplayName());
		assertNotEquals(userNotExpected.getEmail(), userActual.getEmail());
		assertNotEquals(userNotExpected.getPassword(), userActual.getPassword());
	}

	@Test
	public void hashCodeTest() {
		assertEquals(userExpected.hashCode(), userActual.hashCode());
		assertNotEquals(userNotExpected.hashCode(), userActual.hashCode());
	}
}
