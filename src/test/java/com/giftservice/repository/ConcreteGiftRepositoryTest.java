package com.giftservice.repository;

import com.giftservice.entity.ConcreteGift;
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
 * Integration tests for ConcreteGiftRepository using TestContainers.
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
class ConcreteGiftRepositoryTest {

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
    private ConcreteGiftRepository concreteGiftRepository;

    @Autowired
    private GiftSuggestionRepository giftSuggestionRepository;

    private GiftSuggestion bookSuggestion;
    private GiftSuggestion techSuggestion;
    private ConcreteGift javaBook;
    private ConcreteGift pythonBook;
    private ConcreteGift smartWatch;
    private ConcreteGift laptop;
    private ConcreteGift unavailableGift;

    @BeforeEach
    void setUp() {
        // Create gift suggestions
        bookSuggestion = new GiftSuggestion(
            "Programming Books",
            "Books for developers",
            new BigDecimal("20.00"),
            new BigDecimal("60.00"),
            AgeGroup.ADULT,
            Gender.UNISEX,
            Interest.READING,
            Occasion.BIRTHDAY,
            Relationship.FRIEND,
            PersonalityType.INTELLECTUAL
        );

        techSuggestion = new GiftSuggestion(
            "Tech Gadgets",
            "Modern technology gifts",
            new BigDecimal("100.00"),
            new BigDecimal("500.00"),
            AgeGroup.ADULT,
            Gender.UNISEX,
            Interest.TECHNOLOGY,
            Occasion.CHRISTMAS,
            Relationship.FAMILY,
            PersonalityType.PRACTICAL
        );

        giftSuggestionRepository.saveAll(List.of(bookSuggestion, techSuggestion));

        // Create concrete gifts
        javaBook = new ConcreteGift(
            "Effective Java",
            "Joshua Bloch's classic",
            new BigDecimal("45.99"),
            "Amazon",
            bookSuggestion
        );

        pythonBook = new ConcreteGift(
            "Python Crash Course",
            "Learn Python programming",
            new BigDecimal("32.50"),
            "BookStore",
            bookSuggestion
        );

        smartWatch = new ConcreteGift(
            "Apple Watch Series 9",
            "Latest smartwatch",
            new BigDecimal("399.00"),
            "Apple Store",
            techSuggestion
        );

        laptop = new ConcreteGift(
            "MacBook Air",
            "Lightweight laptop",
            new BigDecimal("999.00"),
            "Apple Store",
            techSuggestion
        );

        unavailableGift = new ConcreteGift(
            "Out of Stock Book",
            "Currently unavailable",
            new BigDecimal("25.00"),
            "Amazon",
            bookSuggestion
        );
        unavailableGift.setAvailable(false);

        concreteGiftRepository.saveAll(List.of(javaBook, pythonBook, smartWatch, laptop, unavailableGift));
    }

    @Test
    void findByGiftSuggestion_shouldReturnCorrectGifts() {
        // When
        List<ConcreteGift> bookGifts = concreteGiftRepository.findByGiftSuggestion(bookSuggestion);
        List<ConcreteGift> techGifts = concreteGiftRepository.findByGiftSuggestion(techSuggestion);

        // Then
        assertThat(bookGifts).hasSize(3); // Including unavailable one
        assertThat(techGifts).hasSize(2);
        
        assertThat(bookGifts).extracting(ConcreteGift::getName)
            .contains("Effective Java", "Python Crash Course", "Out of Stock Book");
        assertThat(techGifts).extracting(ConcreteGift::getName)
            .contains("Apple Watch Series 9", "MacBook Air");
    }

    @Test
    void findByGiftSuggestionId_shouldReturnCorrectGifts() {
        // When
        List<ConcreteGift> bookGifts = concreteGiftRepository.findByGiftSuggestionId(bookSuggestion.getId());
        List<ConcreteGift> techGifts = concreteGiftRepository.findByGiftSuggestionId(techSuggestion.getId());

        // Then
        assertThat(bookGifts).hasSize(3);
        assertThat(techGifts).hasSize(2);
    }

    @Test
    void findByVendorName_shouldReturnCorrectGifts() {
        // When
        List<ConcreteGift> amazonGifts = concreteGiftRepository.findByVendorName("Amazon");
        List<ConcreteGift> appleGifts = concreteGiftRepository.findByVendorName("Apple Store");
        List<ConcreteGift> bookStoreGifts = concreteGiftRepository.findByVendorName("BookStore");

        // Then
        assertThat(amazonGifts).hasSize(2); // javaBook and unavailableGift
        assertThat(appleGifts).hasSize(2); // smartWatch and laptop
        assertThat(bookStoreGifts).hasSize(1); // pythonBook
    }

    @Test
    void findByAvailable_shouldReturnCorrectAvailabilityStatus() {
        // When
        List<ConcreteGift> availableGifts = concreteGiftRepository.findByAvailable(true);
        List<ConcreteGift> unavailableGifts = concreteGiftRepository.findByAvailable(false);

        // Then
        assertThat(availableGifts).hasSize(4);
        assertThat(unavailableGifts).hasSize(1);
        assertThat(unavailableGifts.get(0).getName()).isEqualTo("Out of Stock Book");
    }

    @Test
    void findByVendorNameAndAvailable_shouldReturnCorrectCombination() {
        // When
        List<ConcreteGift> availableAmazonGifts = concreteGiftRepository.findByVendorNameAndAvailable("Amazon", true);
        List<ConcreteGift> unavailableAmazonGifts = concreteGiftRepository.findByVendorNameAndAvailable("Amazon", false);
        List<ConcreteGift> availableAppleGifts = concreteGiftRepository.findByVendorNameAndAvailable("Apple Store", true);

        // Then
        assertThat(availableAmazonGifts).hasSize(1); // Only javaBook
        assertThat(unavailableAmazonGifts).hasSize(1); // Only unavailableGift
        assertThat(availableAppleGifts).hasSize(2); // Both Apple products
    }

    @Test
    void findByGiftSuggestionAndAvailable_shouldReturnCorrectCombination() {
        // When
        List<ConcreteGift> availableBookGifts = concreteGiftRepository.findByGiftSuggestionAndAvailable(bookSuggestion, true);
        List<ConcreteGift> unavailableBookGifts = concreteGiftRepository.findByGiftSuggestionAndAvailable(bookSuggestion, false);

        // Then
        assertThat(availableBookGifts).hasSize(2); // javaBook and pythonBook
        assertThat(unavailableBookGifts).hasSize(1); // unavailableGift
    }

    @Test
    void findByGiftSuggestionIdAndAvailable_shouldReturnCorrectCombination() {
        // When
        List<ConcreteGift> availableBookGifts = concreteGiftRepository.findByGiftSuggestionIdAndAvailable(
            bookSuggestion.getId(), true);

        // Then
        assertThat(availableBookGifts).hasSize(2);
        assertThat(availableBookGifts).extracting(ConcreteGift::getName)
            .contains("Effective Java", "Python Crash Course");
    }

    @Test
    void findAllAvailable_convenienceMethod_shouldWork() {
        // When
        List<ConcreteGift> availableGifts = concreteGiftRepository.findAllAvailable();

        // Then
        assertThat(availableGifts).hasSize(4);
        assertThat(availableGifts).noneMatch(gift -> !gift.getAvailable());
    }

    @Test
    void findAllUnavailable_convenienceMethod_shouldWork() {
        // When
        List<ConcreteGift> unavailableGifts = concreteGiftRepository.findAllUnavailable();

        // Then
        assertThat(unavailableGifts).hasSize(1);
        assertThat(unavailableGifts).allMatch(gift -> !gift.getAvailable());
    }

    @Test
    void findByPriceRange_shouldReturnCorrectRange() {
        // When
        List<ConcreteGift> midRangeGifts = concreteGiftRepository.findByPriceRange(
            new BigDecimal("30.00"), new BigDecimal("50.00"));

        // Then
        assertThat(midRangeGifts).hasSize(2); // pythonBook and javaBook
        assertThat(midRangeGifts).extracting(ConcreteGift::getName)
            .contains("Python Crash Course", "Effective Java");
    }

    @Test
    void findByPriceLessThanEqual_shouldReturnAffordableGifts() {
        // When
        List<ConcreteGift> budgetGifts = concreteGiftRepository.findByExactPriceLessThanEqual(new BigDecimal("50.00"));

        // Then
        assertThat(budgetGifts).hasSize(3); // All books
        assertThat(budgetGifts).noneMatch(gift -> gift.getExactPrice().compareTo(new BigDecimal("50.00")) > 0);
    }

    @Test
    void findByPriceGreaterThanEqual_shouldReturnExpensiveGifts() {
        // When
        List<ConcreteGift> expensiveGifts = concreteGiftRepository.findByExactPriceGreaterThanEqual(new BigDecimal("100.00"));

        // Then
        assertThat(expensiveGifts).hasSize(2); // smartWatch and laptop
        assertThat(expensiveGifts).extracting(ConcreteGift::getName)
            .contains("Apple Watch Series 9", "MacBook Air");
    }

    @Test
    void findByGiftSuggestion_withPagination_shouldWork() {
        // When
        Pageable pageable = PageRequest.of(0, 2);
        Page<ConcreteGift> page = concreteGiftRepository.findByGiftSuggestion(bookSuggestion, pageable);

        // Then
        assertThat(page.getContent()).hasSize(2); // First 2 of 3 book gifts
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void findByVendorNameAndAvailable_withPagination_shouldWork() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<ConcreteGift> page = concreteGiftRepository.findByVendorNameAndAvailable("Apple Store", true, pageable);

        // Then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }

    @Test
    void findByAdvancedCriteria_withAllParametersNull_shouldReturnAllGifts() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<ConcreteGift> page = concreteGiftRepository.findByAdvancedCriteria(
            null, null, null, null, pageable);

        // Then
        assertThat(page.getContent()).hasSize(5);
        assertThat(page.getTotalElements()).isEqualTo(5);
    }

    @Test
    void findByAdvancedCriteria_withSpecificCriteria_shouldFilterCorrectly() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<ConcreteGift> page = concreteGiftRepository.findByAdvancedCriteria(
            "Amazon", true, new BigDecimal("40.00"), new BigDecimal("50.00"), pageable);

        // Then - Should find only available Amazon gifts in price range 40-50
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getName()).isEqualTo("Effective Java");
    }

    @Test
    void findBySuggestionAndCriteria_shouldFilterCorrectly() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<ConcreteGift> page = concreteGiftRepository.findBySuggestionAndCriteria(
            bookSuggestion.getId(), true, new BigDecimal("30.00"), new BigDecimal("40.00"), pageable);

        // Then - Should find available book gifts in price range 30-40
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getName()).isEqualTo("Python Crash Course");
    }

    @Test
    void countByVendorAndAvailable_shouldReturnCorrectCount() {
        // When
        long amazonCount = concreteGiftRepository.countByVendorAndAvailable("Amazon", null);
        long availableAmazonCount = concreteGiftRepository.countByVendorAndAvailable("Amazon", true);
        long allAvailableCount = concreteGiftRepository.countByVendorAndAvailable(null, true);

        // Then
        assertThat(amazonCount).isEqualTo(2); // Both Amazon gifts
        assertThat(availableAmazonCount).isEqualTo(1); // Only available Amazon gift
        assertThat(allAvailableCount).isEqualTo(4); // All available gifts
    }

    @Test
    void bidirectionalRelationship_shouldWorkCorrectly() {
        // When - Access from gift suggestion to concrete gifts
        List<ConcreteGift> relationshipGifts = concreteGiftRepository.findByGiftSuggestion(bookSuggestion);

        // Then - Verify bidirectional relationship
        for (ConcreteGift gift : relationshipGifts) {
            assertThat(gift.getGiftSuggestion().getId()).isEqualTo(bookSuggestion.getId());
            assertThat(gift.getGiftSuggestion().getName()).isEqualTo("Programming Books");
        }

        // When - Access from concrete gift to suggestion
        ConcreteGift foundGift = relationshipGifts.get(0);
        GiftSuggestion foundSuggestion = foundGift.getGiftSuggestion();

        // Then
        assertThat(foundSuggestion).isNotNull();
        assertThat(foundSuggestion.getName()).isEqualTo("Programming Books");
    }

    @Test
    void edgeCases_shouldHandleEmptyResults() {
        // When - Search for non-existent vendor
        List<ConcreteGift> nonExistentVendor = concreteGiftRepository.findByVendorName("NonExistent");

        // Then
        assertThat(nonExistentVendor).isEmpty();

        // When - Search with impossible price range
        List<ConcreteGift> impossibleRange = concreteGiftRepository.findByPriceRange(
            new BigDecimal("10000.00"), new BigDecimal("20000.00"));

        // Then
        assertThat(impossibleRange).isEmpty();
    }

    @Test
    void edgeCases_shouldHandleExtremePrices() {
        // When - Search with very high price threshold
        List<ConcreteGift> expensiveResults = concreteGiftRepository.findByExactPriceGreaterThanEqual(
            new BigDecimal("10000.00"));

        // Then
        assertThat(expensiveResults).isEmpty();

        // When - Search with very low price threshold
        List<ConcreteGift> allResults = concreteGiftRepository.findByExactPriceLessThanEqual(
            new BigDecimal("10000.00"));

        // Then - Should return all gifts
        assertThat(allResults).hasSize(5);
    }

    @Test
    void pagination_shouldWorkCorrectlyForLargeDatasets() {
        // When - First page with size 2
        Pageable firstPage = PageRequest.of(0, 2);
        Page<ConcreteGift> page1 = concreteGiftRepository.findAll(firstPage);

        // When - Second page with size 2
        Pageable secondPage = PageRequest.of(1, 2);
        Page<ConcreteGift> page2 = concreteGiftRepository.findAll(secondPage);

        // Then
        assertThat(page1.getContent()).hasSize(2);
        assertThat(page2.getContent()).hasSize(2);
        assertThat(page1.getTotalElements()).isEqualTo(5);
        assertThat(page2.getTotalElements()).isEqualTo(5);
        assertThat(page1.getTotalPages()).isEqualTo(3);

        // Verify no overlap between pages
        assertThat(page1.getContent()).doesNotContainAnyElementsOf(page2.getContent());
    }
}