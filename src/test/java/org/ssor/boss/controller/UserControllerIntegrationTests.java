package org.ssor.boss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.ssor.boss.dto.CreateUserInputDTO;
import org.ssor.boss.dto.CreateUserResultDTO;
import org.ssor.boss.entity.UserEntity;
import org.ssor.boss.entity.UserType;
import org.ssor.boss.exception.UserAlreadyExistsException;
import org.ssor.boss.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(SpringExtension.class)
class UserControllerIntegrationTests
{
  static final String FAKE_PASSWORD = "b7aa75b24ace50c798eea4fe4ed0e36136032a438b43b0042b3ffe3a422d13ab";

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper mapper;

  @MockBean
  UserService userService;
  ArrayList<UserEntity> users;

  @BeforeEach
  void setup()
  {
    final var typeId = UserType.DEFAULT.ordinal();
    final var user1 = new UserEntity(1, typeId, 1, "username", "me@example.com", FAKE_PASSWORD, LocalDateTime.now(), null, true);
    final var user2 = new UserEntity(2, typeId, 1, "username", "me@example.com", FAKE_PASSWORD, LocalDateTime.now(), null, true);
    final var user3 = new UserEntity(3, typeId, 1, "username", "me@example.com", FAKE_PASSWORD, LocalDateTime.now(), null, true);
    final var user4 = new UserEntity(4, typeId, 1, "username", "me@example.com", FAKE_PASSWORD, LocalDateTime.now(), null, true);
    mapper.registerModule(new JavaTimeModule());
    users = Lists.newArrayList(user1, user2, user3, user4);
  }

  @AfterEach
  void teardown()
  {
    users = null;
  }

  @Test
  void test_GetAllUsers() throws Exception
  {
    when(userService.getAllUsers()).thenReturn(users);

    final var returned = mockMvc.perform(get("/api/v1/users"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andReturn();

    // TODO: Convert returned into array list of UserEntity and compare each item.
  }

  @Test
  void test_CanAddNewUser() throws Exception
  {
    final var timeNow = LocalDateTime.now();
    final var userDTO = new CreateUserInputDTO("monkey", "me.and.you@example.com", FAKE_PASSWORD);
    final var resultDTO = new CreateUserResultDTO(5, UserType.DEFAULT.ordinal(), 1, "monkey", "me.and.you@example.com", timeNow);
    when(userService.createUser(eq(userDTO), any())).thenReturn(resultDTO);

    final var returned = mockMvc.perform(post("/api/v1/users")
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(mapper.writeValueAsString(userDTO)))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andReturn();
    final var jsonString = returned.getResponse().getContentAsString();
    final var returnedDTO = mapper.readValue(jsonString, CreateUserResultDTO.class);
    assertThat(returnedDTO).isEqualTo(resultDTO);
  }

  @Test
  void test_CannotAddNewUserBecauseUserExists() throws Exception
  {
    final var userDTO = new CreateUserInputDTO("username", "me@example.com", FAKE_PASSWORD);
    when(userService.createUser(eq(userDTO), any())).thenThrow(UserAlreadyExistsException.class);

    mockMvc.perform(post("/api/v1/users")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(mapper.writeValueAsString(userDTO)))
           .andDo(print())
           .andExpect(status().isConflict());
  }

  @Test
  void test_CannotAddNewUserBecauseDTOContainsIllegalArguments() throws Exception
  {
    final var userDTO = new CreateUserInputDTO(null, null, null);
    when(userService.createUser(eq(userDTO), any())).thenThrow(IllegalArgumentException.class);

    mockMvc.perform(post("/api/v1/users")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(mapper.writeValueAsString(userDTO)))
           .andDo(print())
           .andExpect(status().isBadRequest());
  }
}
