package com.nightcrowler.spring.banking.utils;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;


@Log4j2
public class TestDbContainerInitializer implements BeforeAllCallback, AfterAllCallback {

    private final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16-bullseye")
                    .withDatabaseName("fake-bank-db-instance")
                    .withUsername("bank-user")
                    .withExposedPorts(5432)
                    .withPassword("fake-bank-db-password")
                    .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 1));


    private void setRuntimeProperties() {
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
        System.setProperty("spring.datasource.driver-class-name", "org.postgresql.Driver");
        System.setProperty("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
        System.setProperty("spring.jpa.hibernate.ddl-auto", "create-drop");
        System.setProperty("spring.datasource.hikari.connection-timeout", "250");
    }


    @Override
    public void afterAll(ExtensionContext extensionContext) {
        log.info("******* STOPPING POSTGRES DB *******");
        var containerCmd = postgreSQLContainer.getDockerClient().stopContainerCmd(postgreSQLContainer.getContainerId());
        containerCmd.withTimeout(1000);
        log.info("STOPPING ContainerId : {}", containerCmd.getContainerId());
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        postgreSQLContainer.start();
        postgreSQLContainer.setWaitStrategy(Wait.forListeningPort());
        setRuntimeProperties();
    }
}
