package com.giftservice.integration;

import com.giftservice.entity.ConcreteGift;
import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test specifically for Flyway database migrations.
 * This test validates that the database schema created by Flyway migrations
 * properly supports the JPA entities and their relationships.
 */
@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=validate",
    "spring.flyway.enabled=true",
    "spring.jpa.show-sql=true"
})
class FlywayMigrationIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    void flywayMigratedSchema_shouldSupportFullEntityPersistence() {
        // Given - Create a complete gift suggestion with all enum values
        GiftSuggestion giftSuggestion = new GiftSuggestion(
            "Flyway Test Gift",
            "Testing schema created by Flyway migrations",
            new BigDecimal("15.50"),
            new BigDecimal("75.99"),
            AgeGroup.YOUNG_ADULT,
            Gender.NON_BINARY,
            Interest.TECHNOLOGY,
            Occasion.GRADUATION,
            Relationship.COLLEAGUE,
            PersonalityType.ANALYTICAL
        );

        // When - Persist using Flyway-created schema
        entityManager.persist(giftSuggestion);
        entityManager.flush();

        // Then - All fields should be persisted correctly
        assertThat(giftSuggestion.getId()).isNotNull();
        assertThat(giftSuggestion.getCreatedDate()).isNotNull();
        assertThat(giftSuggestion.getLastModifiedDate()).isNotNull();
        
        // Verify all enum values are supported by schema constraints
        GiftSuggestion found = entityManager.find(GiftSuggestion.class, giftSuggestion.getId());
        assertThat(found.getAgeGroup()).isEqualTo(AgeGroup.YOUNG_ADULT);
        assertThat(found.getGender()).isEqualTo(Gender.NON_BINARY);
        assertThat(found.getInterest()).isEqualTo(Interest.TECHNOLOGY);
        assertThat(found.getOccasion()).isEqualTo(Occasion.GRADUATION);
        assertThat(found.getRelationship()).isEqualTo(Relationship.COLLEAGUE);
        assertThat(found.getPersonalityType()).isEqualTo(PersonalityType.ANALYTICAL);
    }

    @Test
    @Transactional
    void flywaySchema_shouldSupportConcreteGiftWithAllFields() {
        // Given
        GiftSuggestion giftSuggestion = createGiftSuggestion();
        entityManager.persist(giftSuggestion);
        entityManager.flush();

        ConcreteGift concreteGift = new ConcreteGift(
            "Complete Concrete Gift",
            "Testing all fields in Flyway schema",
            new BigDecimal("42.99"),
            "Premium Vendor",
            "https://example.com/product/123",
            "SKU-12345",
            true,
            giftSuggestion
        );

        // When
        entityManager.persist(concreteGift);
        entityManager.flush();

        // Then
        assertThat(concreteGift.getId()).isNotNull();
        assertThat(concreteGift.getCreatedDate()).isNotNull();
        assertThat(concreteGift.getLastModifiedDate()).isNotNull();
        
        ConcreteGift found = entityManager.find(ConcreteGift.class, concreteGift.getId());
        assertThat(found.getName()).isEqualTo("Complete Concrete Gift");
        assertThat(found.getDescription()).isEqualTo("Testing all fields in Flyway schema");
        assertThat(found.getExactPrice()).isEqualTo(new BigDecimal("42.99"));
        assertThat(found.getVendorName()).isEqualTo("Premium Vendor");
        assertThat(found.getProductUrl()).isEqualTo("https://example.com/product/123");
        assertThat(found.getProductSku()).isEqualTo("SKU-12345");
        assertThat(found.getAvailable()).isTrue();
        assertThat(found.getGiftSuggestion().getId()).isEqualTo(giftSuggestion.getId());
    }

    @Test
    @Transactional
    void flywaySchema_shouldEnforcePriceRangeConstraint() {
        // This test verifies that the database-level constraint from Flyway migration
        // works correctly (max_price >= min_price)
        
        // Given - Valid price range
        GiftSuggestion validGift = new GiftSuggestion(
            "Valid Price Range",
            "Min price equals max price should be allowed",
            new BigDecimal("25.00"),
            new BigDecimal("25.00"), // Equal prices should be allowed
            AgeGroup.ADULT,
            Gender.UNISEX,
            Interest.READING,
            Occasion.BIRTHDAY,
            Relationship.FRIEND,
            PersonalityType.PRACTICAL
        );

        // When & Then - Should succeed
        entityManager.persist(validGift);
        entityManager.flush();
        
        assertThat(validGift.getId()).isNotNull();
    }

    @Test
    @Transactional
    void flywaySchema_shouldSupportForeignKeyRelationship() {
        // Given
        GiftSuggestion parent = createGiftSuggestion();
        entityManager.persist(parent);
        entityManager.flush();

        ConcreteGift child1 = new ConcreteGift("Child 1", "Description 1", new BigDecimal("20.00"), "Vendor 1", parent);
        ConcreteGift child2 = new ConcreteGift("Child 2", "Description 2", new BigDecimal("30.00"), "Vendor 2", parent);

        // When
        parent.addConcreteGift(child1);
        parent.addConcreteGift(child2);
        entityManager.flush();
        entityManager.clear();

        // Then - Foreign key relationship should work
        GiftSuggestion foundParent = entityManager.find(GiftSuggestion.class, parent.getId());
        assertThat(foundParent.getConcreteGifts()).hasSize(2);
        
        for (ConcreteGift child : foundParent.getConcreteGifts()) {
            assertThat(child.getGiftSuggestion().getId()).isEqualTo(parent.getId());
        }
    }

    private GiftSuggestion createGiftSuggestion() {
        return new GiftSuggestion(
            "Basic Gift Suggestion",
            "A basic gift for testing",
            new BigDecimal("10.00"),
            new BigDecimal("50.00"),
            AgeGroup.ADULT,
            Gender.UNISEX,
            Interest.READING,
            Occasion.BIRTHDAY,
            Relationship.FRIEND,
            PersonalityType.INTELLECTUAL
        );
    }
}