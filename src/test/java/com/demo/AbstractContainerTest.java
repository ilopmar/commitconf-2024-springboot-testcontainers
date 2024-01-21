package com.demo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT
)
public abstract class AbstractContainerTest {

//  @Container
//  @ServiceConnection
//  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
//      DockerImageName.parse("postgres:16.1")
//  );
//
//  @Container
//  @ServiceConnection
//  static KafkaContainer kafkaContainer = new KafkaContainer(
//      DockerImageName.parse("confluentinc/cp-kafka:7.5.3")
//  );
//
//  @DynamicPropertySource
//  static void init(DynamicPropertyRegistry registry) {
//    Startables.deepStart(postgreSQLContainer, kafkaContainer).join();
//
//    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//    registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
//  }

  @LocalServerPort
  int port;

  protected RequestSpecification requestSpecification;

  @BeforeEach
  void setup() {
    requestSpecification = new RequestSpecBuilder()
        .setPort(port)
        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

}
