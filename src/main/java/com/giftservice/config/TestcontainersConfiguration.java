package com.giftservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import jakarta.annotation.PostConstruct;

/**
 * Configuration for starting PostgreSQL via external tools during local development.
 * 
 * This configuration provides guidance for automatic database setup but requires
 * Testcontainers to be available in the classpath (typically via test dependencies).
 * 
 * For automatic container management, use the GiftServiceApplicationDev main class
 * or the provided Maven profile.
 */
@Configuration
@Profile("testcontainers")
@ConditionalOnProperty(name = "testcontainers.enabled", havingValue = "true", matchIfMissing = true)
public class TestcontainersConfiguration {

    @PostConstruct
    public void initDatabase() {
        System.out.println("=================================================");
        System.out.println("🐳 TESTCONTAINERS DEVELOPMENT MODE");
        System.out.println("=================================================");
        System.out.println("This profile requires running with test dependencies.");
        System.out.println("Please use one of these alternatives:");
        System.out.println("");
        System.out.println("1. Manual PostgreSQL setup:");
        System.out.println("   docker run --name postgres-dev \\");
        System.out.println("     -e POSTGRES_PASSWORD=giftservice \\");
        System.out.println("     -e POSTGRES_USER=giftservice \\");
        System.out.println("     -e POSTGRES_DB=giftservice_dev \\");
        System.out.println("     -p 5432:5432 -d postgres:15");
        System.out.println("");
        System.out.println("2. Use test profile with H2:");
        System.out.println("   mvn spring-boot:run -Dspring-boot.run.profiles=test");
        System.out.println("");
        System.out.println("3. Set up PostgreSQL and use dev profile:");
        System.out.println("   mvn spring-boot:run -Dspring-boot.run.profiles=dev");
        System.out.println("=================================================");
    }
}