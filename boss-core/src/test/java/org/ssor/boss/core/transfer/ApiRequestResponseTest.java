package org.ssor.boss.core.transfer;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.OK;

public class ApiRequestResponseTest
{
  @Test
  void test_CanCreateApiRequestResponse_WithoutParameters()
  {
    final var response = new ApiRequestResponse();
    assertThat(response).isNotNull();
  }

  @Test
  void test_CanCreateApiRequestResponse_WithAllParameters()
  {
    final var timestamp = LocalDateTime.now();
    final var validationErrors = new HashMap<String, String>();
    final var status = OK.value();
    final var reason = OK.getReasonPhrase();
    final var response = new ApiRequestResponse(timestamp, status, reason, "Success", "/", validationErrors);
    assertThat(response).isNotNull();
    assertThat(response.getTimestamp()).isEqualTo(timestamp);
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getReason()).isEqualTo(reason);
    assertThat(response.getMessage()).isEqualTo("Success");
    assertThat(response.getPath()).isEqualTo("/");
    assertThat(response.getValidationErrors()).isEqualTo(validationErrors);
  }

  @Test
  void test_CanCreateApiRequestResponse_WithBuilder()
  {
    final var timestamp = LocalDateTime.now();
    final var validationErrors = new HashMap<String, String>();
    final var status = OK.value();
    final var reason = OK.getReasonPhrase();
    final var response = ApiRequestResponse.builder().timestamp(timestamp).status(status).reason(reason)
                                           .message("Success").path("/").validationErrors(validationErrors).build();
    assertThat(response).isNotNull();
    assertThat(response.getTimestamp()).isEqualTo(timestamp);
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getReason()).isEqualTo(reason);
    assertThat(response.getMessage()).isEqualTo("Success");
    assertThat(response.getPath()).isEqualTo("/");
    assertThat(response.getValidationErrors()).isEqualTo(validationErrors);
  }

  @Test
  void test_CanAssignWithSettersAndRetrieveWithGetters()
  {
    final var response = new ApiRequestResponse();
    assertThat(response).isNotNull();

    final var timestamp = LocalDateTime.now();
    final var validationErrors = new HashMap<String, String>();
    final var status = OK.value();
    final var reason = OK.getReasonPhrase();
    response.setTimestamp(timestamp);
    response.setStatus(status);
    response.setReason(reason);
    response.setMessage("Success");
    response.setPath("/");
    response.setValidationErrors(validationErrors);
    assertThat(response.getTimestamp()).isEqualTo(timestamp);
    assertThat(response.getStatus()).isEqualTo(status);
    assertThat(response.getReason()).isEqualTo(reason);
    assertThat(response.getMessage()).isEqualTo("Success");
    assertThat(response.getPath()).isEqualTo("/");
    assertThat(response.getValidationErrors()).isEqualTo(validationErrors);
  }

  @Test
  void test_CanCompare_WithEquals()
  {
    final var timestamp = LocalDateTime.now();
    final var validationErrors = new HashMap<String, String>();
    final var status = OK.value();
    final var reason = OK.getReasonPhrase();
    final var response1 = new ApiRequestResponse(timestamp, status, reason, "Success", "/", validationErrors);
    final var response2 = new ApiRequestResponse(timestamp, CONFLICT.value(), CONFLICT.getReasonPhrase(), "User already exists", "/", validationErrors);
    final var response3 = new ApiRequestResponse(timestamp, status, reason, "Success", "/", validationErrors);
    assertThat(response2).isNotEqualTo(response1);
    assertThat(response3).isEqualTo(response1);
  }

  @Test
  void test_CanCompare_WithHashCode()
  {
    final var timestamp = LocalDateTime.now();
    final var validationErrors = new HashMap<String, String>();
    final var status = OK.value();
    final var reason = OK.getReasonPhrase();
    final var response1 = new ApiRequestResponse(timestamp, status, reason, "Success", "/", validationErrors);
    final var response2 = new ApiRequestResponse(timestamp, CONFLICT.value(), CONFLICT.getReasonPhrase(), "User already exists", "/", validationErrors);
    final var response3 = new ApiRequestResponse(timestamp, status, reason, "Success", "/", validationErrors);
    assertThat(response2.hashCode()).isNotEqualTo(response1.hashCode());
    assertThat(response3.hashCode()).isEqualTo(response1.hashCode());
  }
}
