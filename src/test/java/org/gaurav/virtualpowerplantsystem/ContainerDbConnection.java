package org.gaurav.virtualpowerplantsystem;

import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.MySQLContainer;

public abstract class ContainerDbConnection {

    private static MySQLContainer<?> mysqlContainer;

    @BeforeEach
    public void setUp() {
        if (mysqlContainer == null) {
            mysqlContainer = new MySQLContainer<>("mysql:latest")
                    .withDatabaseName("testdb")
                    .withUsername("root")
                    .withPassword("root");
            mysqlContainer.start();
        }
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
    }
}
