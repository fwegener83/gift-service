package com.giftservice.repository;

import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for GiftSuggestionRepository using TestContainers.
 * Tests all repository methods with real PostgreSQL database.
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false",
    "spring.jpa.show-sql=false"
})
class GiftSuggestionRepositoryTest {

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

    @Autowired
    private GiftSuggestionRepository repository;

    private GiftSuggestion adultMaleBook;
    private GiftSuggestion childFemaleArt;
    private GiftSuggestion seniorUnisexTech;
    private GiftSuggestion cheapGift;
    private GiftSuggestion expensiveGift;

    @BeforeEach
    void setUp() {
        // Create test data with different combinations
        adultMaleBook = new GiftSuggestion(
            "Programming Book",
            "Learn Java programming",
            new BigDecimal("25.00"),
            new BigDecimal("50.00"),
            AgeGroup.ADULT,
            Gender.MALE,
            Interest.READING,
            Occasion.BIRTHDAY,
            Relationship.FRIEND,
            PersonalityType.INTELLECTUAL
        );

        childFemaleArt = new GiftSuggestion(
            "Art Supplies",
            "Creativity kit for children",
            new BigDecimal("15.00"),
            new BigDecimal("35.00"),
            AgeGroup.CHILD,
            Gender.FEMALE,
            Interest.ART,
            Occasion.CHRISTMAS,
            Relationship.FAMILY,
            PersonalityType.CREATIVE
        );

        seniorUnisexTech = new GiftSuggestion(
            "Smart Watch",
            "Health monitoring device",
            new BigDecimal("150.00"),
            new BigDecimal("300.00"),
            AgeGroup.SENIOR,
            Gender.UNISEX,
            Interest.TECHNOLOGY,
            Occasion.ANNIVERSARY,
            Relationship.ROMANTIC_PARTNER,
            PersonalityType.PRACTICAL
        );

        cheapGift = new GiftSuggestion(
            "Coffee Mug",
            "Simple ceramic mug",
            new BigDecimal("5.00"),
            new BigDecimal("12.00"),
            AgeGroup.ADULT,
            Gender.UNISEX,
            Interest.COOKING,
            Occasion.JUST_BECAUSE,
            Relationship.COLLEAGUE,
            PersonalityType.PRACTICAL
        );

        expensiveGift = new GiftSuggestion(
            "Luxury Jewelry",
            "Diamond necklace",
            new BigDecimal("500.00"),
            new BigDecimal("1000.00"),
            AgeGroup.ADULT,
            Gender.FEMALE,
            Interest.FASHION,
            Occasion.ANNIVERSARY,
            Relationship.ROMANTIC_PARTNER,
            PersonalityType.SOPHISTICATED
        );

        repository.saveAll(List.of(adultMaleBook, childFemaleArt, seniorUnisexTech, cheapGift, expensiveGift));
    }

    @Test
    void findByAgeGroup_shouldReturnCorrectGifts() {
        // When
        List<GiftSuggestion> adultGifts = repository.findByAgeGroup(AgeGroup.ADULT);
        List<GiftSuggestion> childGifts = repository.findByAgeGroup(AgeGroup.CHILD);

        // Then
        assertThat(adultGifts).hasSize(3);
        assertThat(childGifts).hasSize(1);
        assertThat(childGifts.get(0).getName()).isEqualTo("Art Supplies");
    }

    @Test
    void findByGender_shouldReturnCorrectGifts() {
        // When
        List<GiftSuggestion> maleGifts = repository.findByGender(Gender.MALE);
        List<GiftSuggestion> femaleGifts = repository.findByGender(Gender.FEMALE);
        List<GiftSuggestion> unisexGifts = repository.findByGender(Gender.UNISEX);

        // Then
        assertThat(maleGifts).hasSize(1);
        assertThat(femaleGifts).hasSize(2);
        assertThat(unisexGifts).hasSize(2);
    }

    @Test
    void findByInterest_shouldReturnCorrectGifts() {
        // When
        List<GiftSuggestion> readingGifts = repository.findByInterest(Interest.READING);
        List<GiftSuggestion> technologyGifts = repository.findByInterest(Interest.TECHNOLOGY);

        // Then
        assertThat(readingGifts).hasSize(1);
        assertThat(readingGifts.get(0).getName()).isEqualTo("Programming Book");
        assertThat(technologyGifts).hasSize(1);
        assertThat(technologyGifts.get(0).getName()).isEqualTo("Smart Watch");
    }

    @Test
    void findByOccasion_shouldReturnCorrectGifts() {
        // When
        List<GiftSuggestion> birthdayGifts = repository.findByOccasion(Occasion.BIRTHDAY);
        List<GiftSuggestion> anniversaryGifts = repository.findByOccasion(Occasion.ANNIVERSARY);

        // Then
        assertThat(birthdayGifts).hasSize(1);
        assertThat(anniversaryGifts).hasSize(2);
    }

    @Test
    void findByRelationship_shouldReturnCorrectGifts() {
        // When
        List<GiftSuggestion> familyGifts = repository.findByRelationship(Relationship.FAMILY);
        List<GiftSuggestion> romanticGifts = repository.findByRelationship(Relationship.ROMANTIC_PARTNER);

        // Then
        assertThat(familyGifts).hasSize(1);
        assertThat(romanticGifts).hasSize(2);
    }

    @Test
    void findByPersonalityType_shouldReturnCorrectGifts() {
        // When
        List<GiftSuggestion> practicalGifts = repository.findByPersonalityType(PersonalityType.PRACTICAL);
        List<GiftSuggestion> intellectualGifts = repository.findByPersonalityType(PersonalityType.INTELLECTUAL);

        // Then
        assertThat(practicalGifts).hasSize(2);
        assertThat(intellectualGifts).hasSize(1);
    }

    @Test
    void findByAgeGroupAndGender_shouldReturnCorrectCombination() {
        // When
        List<GiftSuggestion> adultMaleGifts = repository.findByAgeGroupAndGender(AgeGroup.ADULT, Gender.MALE);
        List<GiftSuggestion> childFemaleGifts = repository.findByAgeGroupAndGender(AgeGroup.CHILD, Gender.FEMALE);

        // Then
        assertThat(adultMaleGifts).hasSize(1);
        assertThat(adultMaleGifts.get(0).getName()).isEqualTo("Programming Book");
        assertThat(childFemaleGifts).hasSize(1);
        assertThat(childFemaleGifts.get(0).getName()).isEqualTo("Art Supplies");
    }

    @Test
    void findByAgeGroupAndInterest_shouldReturnCorrectCombination() {
        // When
        List<GiftSuggestion> adultReadingGifts = repository.findByAgeGroupAndInterest(AgeGroup.ADULT, Interest.READING);
        List<GiftSuggestion> childArtGifts = repository.findByAgeGroupAndInterest(AgeGroup.CHILD, Interest.ART);

        // Then
        assertThat(adultReadingGifts).hasSize(1);
        assertThat(childArtGifts).hasSize(1);
    }

    @Test
    void findByGenderAndInterest_shouldReturnCorrectCombination() {
        // When
        List<GiftSuggestion> maleReadingGifts = repository.findByGenderAndInterest(Gender.MALE, Interest.READING);
        List<GiftSuggestion> femaleArtGifts = repository.findByGenderAndInterest(Gender.FEMALE, Interest.ART);

        // Then
        assertThat(maleReadingGifts).hasSize(1);
        assertThat(femaleArtGifts).hasSize(1);
    }

    @Test
    void findByOccasionAndRelationship_shouldReturnCorrectCombination() {
        // When
        List<GiftSuggestion> anniversaryRomanticGifts = repository.findByOccasionAndRelationship(
            Occasion.ANNIVERSARY, Relationship.ROMANTIC_PARTNER);

        // Then
        assertThat(anniversaryRomanticGifts).hasSize(2);
    }

    @Test
    void findByAgeGroupAndGenderAndInterest_shouldReturnCorrectTripleCombination() {
        // When
        List<GiftSuggestion> specificGifts = repository.findByAgeGroupAndGenderAndInterest(
            AgeGroup.ADULT, Gender.MALE, Interest.READING);

        // Then
        assertThat(specificGifts).hasSize(1);
        assertThat(specificGifts.get(0).getName()).isEqualTo("Programming Book");
    }

    @Test
    void findByMinPriceLessThanEqual_shouldReturnAffordableGifts() {
        // When
        List<GiftSuggestion> budgetGifts = repository.findByMinPriceLessThanEqual(new BigDecimal("20.00"));

        // Then
        assertThat(budgetGifts).hasSize(2); // cheapGift and childFemaleArt
        assertThat(budgetGifts).extracting(GiftSuggestion::getName)
            .contains("Coffee Mug", "Art Supplies");
    }

    @Test
    void findByMaxPriceGreaterThanEqual_shouldReturnHighEndGifts() {
        // When
        List<GiftSuggestion> premiumGifts = repository.findByMaxPriceGreaterThanEqual(new BigDecimal("100.00"));

        // Then
        assertThat(premiumGifts).hasSize(2); // seniorUnisexTech and expensiveGift
        assertThat(premiumGifts).extracting(GiftSuggestion::getName)
            .contains("Smart Watch", "Luxury Jewelry");
    }

    @Test
    void findGiftsWithinBudget_shouldReturnOverlappingPriceRanges() {
        // When - Budget of 20-40 should overlap with gifts that have ranges touching this budget
        List<GiftSuggestion> budgetGifts = repository.findGiftsWithinBudget(
            new BigDecimal("20.00"), new BigDecimal("40.00"));

        // Then - Should include adultMaleBook (25-50) and childFemaleArt (15-35)
        assertThat(budgetGifts).hasSize(2);
        assertThat(budgetGifts).extracting(GiftSuggestion::getName)
            .contains("Programming Book", "Art Supplies");
    }

    @Test
    void findAffordableGifts_shouldReturnGiftsWithinBudget() {
        // When
        List<GiftSuggestion> affordableGifts = repository.findAffordableGifts(new BigDecimal("30.00"));

        // Then - Should include gifts where minPrice <= 30
        assertThat(affordableGifts).hasSize(3); // adultMaleBook, childFemaleArt, cheapGift
    }

    @Test
    void findByAgeGroupAndGenderAndInterestAndOccasion_withPagination_shouldWork() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<GiftSuggestion> page = repository.findByAgeGroupAndGenderAndInterestAndOccasion(
            AgeGroup.ADULT, Gender.MALE, Interest.READING, Occasion.BIRTHDAY, pageable);

        // Then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getName()).isEqualTo("Programming Book");
    }

    @Test
    void findByAdvancedCriteria_withAllParametersNull_shouldReturnAllGifts() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<GiftSuggestion> page = repository.findByAdvancedCriteria(
            null, null, null, null, null, null, null, pageable);

        // Then
        assertThat(page.getContent()).hasSize(5);
        assertThat(page.getTotalElements()).isEqualTo(5);
    }

    @Test
    void findByAdvancedCriteria_withSpecificCriteria_shouldFilterCorrectly() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<GiftSuggestion> page = repository.findByAdvancedCriteria(
            AgeGroup.ADULT, null, null, null, null, null, new BigDecimal("100.00"), pageable);

        // Then - Should find adult gifts under 100 budget
        assertThat(page.getContent()).hasSize(2); // adultMaleBook and cheapGift
    }

    @Test
    void findByPriceRangeWithPagination_shouldReturnCorrectRange() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<GiftSuggestion> page = repository.findByPriceRangeWithPagination(
            new BigDecimal("10.00"), new BigDecimal("100.00"), pageable);

        // Then - Should include gifts that overlap with 10-100 range
        assertThat(page.getContent()).hasSize(3); // adultMaleBook, childFemaleArt, cheapGift
    }

    @Test
    void countByAdvancedCriteria_shouldReturnCorrectCount() {
        // When
        long count = repository.countByAdvancedCriteria(
            AgeGroup.ADULT, null, null, null, null, null, null);

        // Then
        assertThat(count).isEqualTo(3); // Three adult gifts
    }

    @Test
    void edgeCases_shouldHandleEmptyResults() {
        // When - Search for non-existent combination
        List<GiftSuggestion> emptyResults = repository.findByAgeGroupAndGenderAndInterest(
            AgeGroup.TEEN, Gender.NON_BINARY, Interest.MUSIC);

        // Then
        assertThat(emptyResults).isEmpty();
    }

    @Test
    void edgeCases_shouldHandleExtremePrices() {
        // When - Search with very high budget
        List<GiftSuggestion> expensiveResults = repository.findAffordableGifts(new BigDecimal("10000.00"));

        // Then - Should return all gifts
        assertThat(expensiveResults).hasSize(5);

        // When - Search with very low budget
        List<GiftSuggestion> cheapResults = repository.findAffordableGifts(new BigDecimal("1.00"));

        // Then - Should return no gifts
        assertThat(cheapResults).isEmpty();
    }

    @Test
    void pagination_shouldWorkCorrectly() {
        // When - First page with size 2
        Pageable firstPage = PageRequest.of(0, 2);
        Page<GiftSuggestion> page1 = repository.findAll(firstPage);

        // When - Second page with size 2
        Pageable secondPage = PageRequest.of(1, 2);
        Page<GiftSuggestion> page2 = repository.findAll(secondPage);

        // Then
        assertThat(page1.getContent()).hasSize(2);
        assertThat(page2.getContent()).hasSize(2);
        assertThat(page1.getTotalElements()).isEqualTo(5);
        assertThat(page2.getTotalElements()).isEqualTo(5);
        assertThat(page1.getTotalPages()).isEqualTo(3);
    }
}