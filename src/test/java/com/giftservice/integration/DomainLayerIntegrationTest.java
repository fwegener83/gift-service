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
import jakarta.validation.ConstraintViolationException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for the complete domain layer using TestContainers.
 * Tests JPA persistence, relationships, and database constraints in real PostgreSQL.
 */
@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false",
    "spring.jpa.show-sql=true"
})
class DomainLayerIntegrationTest {

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
    void giftSuggestion_basicPersistence_shouldWork() {
        // Given
        GiftSuggestion giftSuggestion = new GiftSuggestion(
            "Test Gift",
            "A wonderful test gift",
            new BigDecimal("10.00"),
            new BigDecimal("50.00"),
            AgeGroup.ADULT,
            Gender.UNISEX,
            Interest.READING,
            Occasion.BIRTHDAY,
            Relationship.FRIEND,
            PersonalityType.INTELLECTUAL
        );

        // When
        entityManager.persist(giftSuggestion);
        entityManager.flush();

        // Then
        assertThat(giftSuggestion.getId()).isNotNull();
        assertThat(giftSuggestion.getCreatedDate()).isNotNull();
        assertThat(giftSuggestion.getLastModifiedDate()).isNotNull();
    }

    @Test
    @Transactional
    void giftSuggestion_withInvalidPriceRange_shouldFailValidation() {
        // Given
        GiftSuggestion giftSuggestion = new GiftSuggestion(
            "Test Gift",
            "Description",
            new BigDecimal("100.00"), // min > max
            new BigDecimal("50.00"),
            AgeGroup.ADULT,
            Gender.UNISEX,
            Interest.READING,
            Occasion.BIRTHDAY,
            Relationship.FRIEND,
            PersonalityType.INTELLECTUAL
        );

        // When & Then
        assertThatThrownBy(() -> {
            entityManager.persist(giftSuggestion);
            entityManager.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @Transactional
    void concreteGift_basicPersistence_shouldWork() {
        // Given
        GiftSuggestion giftSuggestion = createAndPersistGiftSuggestion();
        
        ConcreteGift concreteGift = new ConcreteGift(
            "Concrete Gift",
            "A specific gift",
            new BigDecimal("25.99"),
            "Test Vendor",
            giftSuggestion
        );

        // When
        entityManager.persist(concreteGift);
        entityManager.flush();

        // Then
        assertThat(concreteGift.getId()).isNotNull();
        assertThat(concreteGift.getCreatedDate()).isNotNull();
        assertThat(concreteGift.getAvailable()).isTrue();
        assertThat(concreteGift.getGiftSuggestion()).isEqualTo(giftSuggestion);
    }

    @Test
    @Transactional
    void concreteGift_withNegativePrice_shouldFailValidation() {
        // Given
        GiftSuggestion giftSuggestion = createAndPersistGiftSuggestion();
        
        ConcreteGift concreteGift = new ConcreteGift(
            "Concrete Gift",
            "Description",
            new BigDecimal("-10.00"), // negative price
            "Vendor",
            giftSuggestion
        );

        // When & Then
        assertThatThrownBy(() -> {
            entityManager.persist(concreteGift);
            entityManager.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @Transactional
    void bidirectionalRelationship_shouldWorkCorrectly() {
        // Given
        GiftSuggestion giftSuggestion = createAndPersistGiftSuggestion();
        
        ConcreteGift gift1 = new ConcreteGift("Gift 1", "Desc 1", new BigDecimal("20.00"), "Vendor 1", giftSuggestion);
        ConcreteGift gift2 = new ConcreteGift("Gift 2", "Desc 2", new BigDecimal("30.00"), "Vendor 2", giftSuggestion);
        
        // When
        giftSuggestion.addConcreteGift(gift1);
        giftSuggestion.addConcreteGift(gift2);
        entityManager.flush();
        entityManager.clear(); // Clear persistence context

        // Then
        GiftSuggestion foundSuggestion = entityManager.find(GiftSuggestion.class, giftSuggestion.getId());
        assertThat(foundSuggestion.getConcreteGifts()).hasSize(2);
        
        // Verify each concrete gift points back to the suggestion
        for (ConcreteGift gift : foundSuggestion.getConcreteGifts()) {
            assertThat(gift.getGiftSuggestion().getId()).isEqualTo(giftSuggestion.getId());
        }
    }

    @Test
    @Transactional
    void enumPersistence_shouldWorkForAllValues() {
        // Test that all enum values can be persisted and retrieved correctly
        
        // Given
        GiftSuggestion giftSuggestion = new GiftSuggestion(
            "Enum Test Gift",
            "Testing enum persistence",
            new BigDecimal("15.00"),
            new BigDecimal("45.00"),
            AgeGroup.SENIOR,
            Gender.NON_BINARY,
            Interest.TECHNOLOGY,
            Occasion.CHRISTMAS,
            Relationship.FAMILY,
            PersonalityType.CREATIVE
        );

        // When
        entityManager.persist(giftSuggestion);
        entityManager.flush();
        entityManager.clear();

        // Then
        GiftSuggestion found = entityManager.find(GiftSuggestion.class, giftSuggestion.getId());
        assertThat(found.getAgeGroup()).isEqualTo(AgeGroup.SENIOR);
        assertThat(found.getGender()).isEqualTo(Gender.NON_BINARY);
        assertThat(found.getInterest()).isEqualTo(Interest.TECHNOLOGY);
        assertThat(found.getOccasion()).isEqualTo(Occasion.CHRISTMAS);
        assertThat(found.getRelationship()).isEqualTo(Relationship.FAMILY);
        assertThat(found.getPersonalityType()).isEqualTo(PersonalityType.CREATIVE);
    }

    @Test
    @Transactional
    void jpqlQueries_shouldWorkWithEnums() {
        // Given
        GiftSuggestion adultGift = new GiftSuggestion(
            "Adult Gift", "For adults", new BigDecimal("20.00"), new BigDecimal("60.00"),
            AgeGroup.ADULT, Gender.MALE, Interest.SPORTS, Occasion.BIRTHDAY,
            Relationship.FRIEND, PersonalityType.COMPETITIVE
        );
        
        GiftSuggestion childGift = new GiftSuggestion(
            "Child Gift", "For children", new BigDecimal("5.00"), new BigDecimal("25.00"),
            AgeGroup.CHILD, Gender.FEMALE, Interest.ART, Occasion.BIRTHDAY,
            Relationship.FAMILY, PersonalityType.CREATIVE
        );

        entityManager.persist(adultGift);
        entityManager.persist(childGift);
        entityManager.flush();

        // When - Query by age group
        List<GiftSuggestion> adultGifts = entityManager
            .createQuery("SELECT gs FROM GiftSuggestion gs WHERE gs.ageGroup = :ageGroup", GiftSuggestion.class)
            .setParameter("ageGroup", AgeGroup.ADULT)
            .getResultList();

        // Then
        assertThat(adultGifts).hasSize(1);
        assertThat(adultGifts.get(0).getName()).isEqualTo("Adult Gift");

        // When - Query by multiple criteria
        List<GiftSuggestion> specificGifts = entityManager
            .createQuery("SELECT gs FROM GiftSuggestion gs WHERE gs.ageGroup = :age AND gs.interest = :interest", GiftSuggestion.class)
            .setParameter("age", AgeGroup.CHILD)
            .setParameter("interest", Interest.ART)
            .getResultList();

        // Then
        assertThat(specificGifts).hasSize(1);
        assertThat(specificGifts.get(0).getName()).isEqualTo("Child Gift");
    }

    @Test
    @Transactional
    void priceRangeQueries_shouldWork() {
        // Given
        GiftSuggestion cheapGift = new GiftSuggestion(
            "Cheap Gift", "Affordable", new BigDecimal("5.00"), new BigDecimal("15.00"),
            AgeGroup.ADULT, Gender.UNISEX, Interest.READING, Occasion.BIRTHDAY,
            Relationship.FRIEND, PersonalityType.PRACTICAL
        );
        
        GiftSuggestion expensiveGift = new GiftSuggestion(
            "Expensive Gift", "Premium", new BigDecimal("100.00"), new BigDecimal("200.00"),
            AgeGroup.ADULT, Gender.UNISEX, Interest.TECHNOLOGY, Occasion.ANNIVERSARY,
            Relationship.ROMANTIC_PARTNER, PersonalityType.SOPHISTICATED
        );

        entityManager.persist(cheapGift);
        entityManager.persist(expensiveGift);
        entityManager.flush();

        // When - Find gifts within budget
        BigDecimal budget = new BigDecimal("50.00");
        List<GiftSuggestion> affordableGifts = entityManager
            .createQuery("SELECT gs FROM GiftSuggestion gs WHERE gs.minPrice <= :budget", GiftSuggestion.class)
            .setParameter("budget", budget)
            .getResultList();

        // Then
        assertThat(affordableGifts).hasSize(1);
        assertThat(affordableGifts.get(0).getName()).isEqualTo("Cheap Gift");
    }

    @Test
    @Transactional
    void cascadePersistence_shouldWork() {
        // Given
        GiftSuggestion giftSuggestion = new GiftSuggestion(
            "Cascade Test", "Testing cascade", new BigDecimal("10.00"), new BigDecimal("50.00"),
            AgeGroup.ADULT, Gender.UNISEX, Interest.READING, Occasion.BIRTHDAY,
            Relationship.FRIEND, PersonalityType.INTELLECTUAL
        );
        
        ConcreteGift concreteGift = new ConcreteGift(
            "Cascaded Gift", "Should be persisted", new BigDecimal("25.00"),
            "Vendor", giftSuggestion
        );
        
        giftSuggestion.addConcreteGift(concreteGift);

        // When - Persist only the parent
        entityManager.persist(giftSuggestion);
        entityManager.flush();

        // Then - Child should also be persisted due to cascade
        assertThat(concreteGift.getId()).isNotNull();
        assertThat(giftSuggestion.getConcreteGifts()).hasSize(1);
    }

    /**
     * Helper method to create and persist a valid GiftSuggestion
     */
    private GiftSuggestion createAndPersistGiftSuggestion() {
        GiftSuggestion giftSuggestion = new GiftSuggestion(
            "Test Suggestion",
            "Test Description",
            new BigDecimal("10.00"),
            new BigDecimal("100.00"),
            AgeGroup.ADULT,
            Gender.UNISEX,
            Interest.READING,
            Occasion.BIRTHDAY,
            Relationship.FRIEND,
            PersonalityType.INTELLECTUAL
        );
        entityManager.persist(giftSuggestion);
        entityManager.flush();
        return giftSuggestion;
    }
}