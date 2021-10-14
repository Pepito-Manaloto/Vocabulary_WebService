package com.aaron.vocabularyapi.test.utils;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractContainerBaseTest
{
    private static final DockerImageName MARIADB_IMAGE = DockerImageName.parse("mariadb:10.5.12");
    @SuppressWarnings("rawtypes")
    private static final MariaDBContainer mariadb;
    //GRANT ALL PRIVILEGES ON db.* TO another_user@'localhost';
    static
    {
        mariadb = new MariaDBContainer<>(MARIADB_IMAGE)
                .withDatabaseName("my_vocabulary")
                .withUsername("root")
                .withPassword("")
                //.withEnv("MYSQL_ROOT_PASSWORD", "test")
                .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");
        mariadb.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry)
    {
        registry.add("spring.datasource.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb::getUsername);
        registry.add("spring.datasource.password", mariadb::getPassword);
    }
}