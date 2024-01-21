package com.demo;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;

import com.demo.controller.PlainMessage;
import com.demo.controller.SignedMessageDTO;
import io.restassured.filter.log.LogDetail;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class SignMessageTest extends AbstractContainerTest {

  private static final Logger log = LoggerFactory.getLogger(SignMessageTest.class);

  @DisplayName("/actuator endpoint works and up starts properly")
  @Test
  void actuator() {
    given(requestSpecification)
        .when()
        .get("/actuator/health")
        .then()
        .statusCode(HttpStatus.OK.value())
        .log().ifValidationFails(LogDetail.ALL);
  }

  @DisplayName("when requesting to sign a text then it is signed")
  @Test
  void signText() {
    var response = given(requestSpecification)
        .when().body(new PlainMessage("Hola CommitConf"))
        .post("/messages");

    response.then().statusCode(HttpStatus.ACCEPTED.value());

    String location = response.then().extract().header(HttpHeaders.LOCATION);
    log.debug("location: {}", location);

    await().untilAsserted(() -> {
      // This way we extract the signature and check it manually
      String signature = given(requestSpecification)
          .when()
          .get(location)
          .then()
          .extract()
          .path("signature");
      assertThat(signature).isNotNull();

//      // With this approach we can check more than once property at the same time
//      given(requestSpecification)
//          .when()
//          .get(location)
//          .then()
//          .body("signature", notNullValue()).and()
//          .body("signed_at", notNullValue());
    });
  }

  @DisplayName("when requesting to sign some texts then they eventually are signed")
  @Test
  void signSomeTexts() {
    for (int i = 1; i <= 5; i++) {
      given(requestSpecification)
          .when().body(new PlainMessage("Hola CommitConf " + i))
          .post("/messages")
          .then()
          .statusCode(HttpStatus.ACCEPTED.value());
    }

    await().timeout(Duration.ofSeconds(30)).untilAsserted(() -> {
      var signedMessages = given(requestSpecification)
          .when()
          .get("/messages")
          .then()
          .extract()
          .body()
          .jsonPath().getList(".", SignedMessageDTO.class);

      assertThat(signedMessages)
          .isNotEmpty()
          .allMatch(signedMessageDTO -> signedMessageDTO.signature() != null);
    });
  }

}
