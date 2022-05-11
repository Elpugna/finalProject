package com.applaudostudios.resourceserver.repository;

import java.util.Objects;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = Replace.NONE
)
public abstract class BaseContainer {
  static final PostgreSQLContainer<?> postgreSqlContainer = (PostgreSQLContainer)(new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))).withDatabaseName("testdatabase").withUsername("extra/test").withPassword("extra/test").withReuse(true);

  public BaseContainer() {
  }

  @DynamicPropertySource
  static void datasourceConfig(final DynamicPropertyRegistry registry) {
    PostgreSQLContainer var10002 = postgreSqlContainer;
    Objects.requireNonNull(var10002);
    registry.add("spring.datasource.url", var10002::getJdbcUrl);
    var10002 = postgreSqlContainer;
    Objects.requireNonNull(var10002);
    registry.add("spring.datasource.password", var10002::getPassword);
    var10002 = postgreSqlContainer;
    Objects.requireNonNull(var10002);
    registry.add("spring.datasource.username", var10002::getUsername);
  }

  static {
    postgreSqlContainer.start();
  }
}
