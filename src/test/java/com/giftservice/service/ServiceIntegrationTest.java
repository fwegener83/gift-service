package com.giftservice.service;

import com.giftservice.entity.ConcreteGift;
import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import com.giftservice.repository.ConcreteGiftRepository;
import com.giftservice.repository.GiftSuggestionRepository;
import com.giftservice.service.impl.ConcreteGiftServiceImpl;
import com.giftservice.service.impl.GiftSuggestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
class ServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private GiftSuggestionRepository giftSuggestionRepository;

    @Autowired
    private ConcreteGiftRepository concreteGiftRepository;

    @Autowired
    private GiftSuggestionService giftSuggestionService;

    @Autowired
    private ConcreteGiftService concreteGiftService;

    private GiftSuggestion testGiftSuggestion;

    @BeforeEach
    void setUp() {
        // Clean up database
        concreteGiftRepository.deleteAll();
        giftSuggestionRepository.deleteAll();

        // Create test data
        testGiftSuggestion = new GiftSuggestion(
                "Integration Test Gift",
                "A gift for integration testing",
                new BigDecimal("10.00"),
                new BigDecimal("100.00"),
                AgeGroup.ADULT,
                Gender.UNISEX,
                Interest.TECHNOLOGY,
                Occasion.BIRTHDAY,
                Relationship.FRIEND,
                PersonalityType.CREATIVE
        );
    }

    @Test
    void giftSuggestionService_CreateAndRetrieve_ShouldWork() {
        // When
        GiftSuggestion savedGiftSuggestion = giftSuggestionService.create(testGiftSuggestion);

        // Then
        assertThat(savedGiftSuggestion.getId()).isNotNull();
        assertThat(savedGiftSuggestion.getName()).isEqualTo("Integration Test Gift");
        assertThat(savedGiftSuggestion.getCreatedDate()).isNotNull();

        // Verify retrieval
        Optional<GiftSuggestion> retrieved = giftSuggestionService.findById(savedGiftSuggestion.getId());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Integration Test Gift");
    }

    @Test
    void giftSuggestionService_UpdateAndDelete_ShouldWork() {
        // Given
        GiftSuggestion savedGiftSuggestion = giftSuggestionService.create(testGiftSuggestion);

        // When - Update
        GiftSuggestion updateData = new GiftSuggestion(
                "Updated Integration Test Gift",
                "Updated description",
                new BigDecimal("15.00"),
                new BigDecimal("150.00"),
                AgeGroup.TEEN,
                Gender.MALE,
                Interest.SPORTS,
                Occasion.CHRISTMAS,
                Relationship.FAMILY,
                PersonalityType.ADVENTUROUS
        );

        GiftSuggestion updatedGiftSuggestion = giftSuggestionService.update(savedGiftSuggestion.getId(), updateData);

        // Then - Verify update
        assertThat(updatedGiftSuggestion.getName()).isEqualTo("Updated Integration Test Gift");
        assertThat(updatedGiftSuggestion.getMinPrice()).isEqualTo(new BigDecimal("15.00"));
        assertThat(updatedGiftSuggestion.getAgeGroup()).isEqualTo(AgeGroup.TEEN);

        // When - Delete
        giftSuggestionService.deleteById(savedGiftSuggestion.getId());

        // Then - Verify deletion
        Optional<GiftSuggestion> deletedGiftSuggestion = giftSuggestionService.findById(savedGiftSuggestion.getId());
        assertThat(deletedGiftSuggestion).isEmpty();
    }

    @Test
    void giftSuggestionService_FindByAttributes_ShouldWork() {
        // Given
        giftSuggestionService.create(testGiftSuggestion);

        GiftSuggestion anotherGiftSuggestion = new GiftSuggestion(
                "Another Test Gift",
                "Another description",
                new BigDecimal("20.00"),
                new BigDecimal("200.00"),
                AgeGroup.CHILD,
                Gender.FEMALE,
                Interest.ART,
                Occasion.EASTER,
                Relationship.EXTENDED_FAMILY,
                PersonalityType.INTELLECTUAL
        );
        giftSuggestionService.create(anotherGiftSuggestion);

        // When & Then
        List<GiftSuggestion> adultGifts = giftSuggestionService.findByAgeGroup(AgeGroup.ADULT);
        assertThat(adultGifts).hasSize(1);
        assertThat(adultGifts.get(0).getName()).isEqualTo("Integration Test Gift");

        List<GiftSuggestion> technologyGifts = giftSuggestionService.findByInterest(Interest.TECHNOLOGY);
        assertThat(technologyGifts).hasSize(1);

        List<GiftSuggestion> childGifts = giftSuggestionService.findByAgeGroup(AgeGroup.CHILD);
        assertThat(childGifts).hasSize(1);
        assertThat(childGifts.get(0).getName()).isEqualTo("Another Test Gift");
    }

    @Test
    void giftSuggestionService_FindByPriceRange_ShouldWork() {
        // Given
        giftSuggestionService.create(testGiftSuggestion);

        GiftSuggestion expensiveGift = new GiftSuggestion(
                "Expensive Gift",
                "Very expensive",
                new BigDecimal("500.00"),
                new BigDecimal("1000.00"),
                AgeGroup.ADULT,
                Gender.UNISEX,
                Interest.FASHION,
                Occasion.ANNIVERSARY,
                Relationship.ROMANTIC_PARTNER,
                PersonalityType.SOPHISTICATED
        );
        giftSuggestionService.create(expensiveGift);

        // When
        List<GiftSuggestion> affordableGifts = giftSuggestionService.findByPriceRange(
                new BigDecimal("5.00"), new BigDecimal("200.00"));

        // Then
        assertThat(affordableGifts).hasSize(1);
        assertThat(affordableGifts.get(0).getName()).isEqualTo("Integration Test Gift");
    }

    @Test
    void giftSuggestionService_AdvancedSearch_ShouldWork() {
        // Given
        giftSuggestionService.create(testGiftSuggestion);

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<GiftSuggestion> results = giftSuggestionService.findByAdvancedCriteria(
                AgeGroup.ADULT,
                Gender.UNISEX,
                Interest.TECHNOLOGY,
                Occasion.BIRTHDAY,
                Relationship.FRIEND,
                PersonalityType.CREATIVE,
                new BigDecimal("150.00"),
                pageable
        );

        // Then
        assertThat(results.getContent()).hasSize(1);
        assertThat(results.getContent().get(0).getName()).isEqualTo("Integration Test Gift");
        assertThat(results.getTotalElements()).isEqualTo(1);

        // Test count method
        long count = giftSuggestionService.countByAdvancedCriteria(
                AgeGroup.ADULT, Gender.UNISEX, Interest.TECHNOLOGY,
                Occasion.BIRTHDAY, Relationship.FRIEND, PersonalityType.CREATIVE,
                new BigDecimal("150.00")
        );
        assertThat(count).isEqualTo(1);
    }

    @Test
    void concreteGiftService_CreateAndRetrieve_ShouldWork() {
        // Given
        GiftSuggestion savedGiftSuggestion = giftSuggestionService.create(testGiftSuggestion);

        ConcreteGift concreteGift = new ConcreteGift(
                "Integration Test Concrete Gift",
                "A concrete gift for testing",
                new BigDecimal("25.00"),
                "Amazon",
                savedGiftSuggestion
        );
        concreteGift.setProductUrl("https://amazon.com/test-product");
        concreteGift.setProductSku("TEST-SKU-123");

        // When
        ConcreteGift savedConcreteGift = concreteGiftService.create(concreteGift);

        // Then
        assertThat(savedConcreteGift.getId()).isNotNull();
        assertThat(savedConcreteGift.getName()).isEqualTo("Integration Test Concrete Gift");
        assertThat(savedConcreteGift.getCreatedDate()).isNotNull();
        assertThat(savedConcreteGift.getGiftSuggestion()).isNotNull();

        // Verify retrieval
        Optional<ConcreteGift> retrieved = concreteGiftService.findById(savedConcreteGift.getId());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Integration Test Concrete Gift");
    }

    @Test
    void concreteGiftService_RelationshipManagement_ShouldWork() {
        // Given
        GiftSuggestion savedGiftSuggestion = giftSuggestionService.create(testGiftSuggestion);

        ConcreteGift concreteGift1 = new ConcreteGift(
                "First Concrete Gift",
                "First description",
                new BigDecimal("30.00"),
                "Amazon",
                savedGiftSuggestion
        );

        ConcreteGift concreteGift2 = new ConcreteGift(
                "Second Concrete Gift",
                "Second description",
                new BigDecimal("45.00"),
                "eBay",
                savedGiftSuggestion
        );

        // When
        ConcreteGift savedGift1 = concreteGiftService.create(concreteGift1);
        ConcreteGift savedGift2 = concreteGiftService.create(concreteGift2);

        // Then
        List<ConcreteGift> relatedGifts = concreteGiftService.findByGiftSuggestionId(savedGiftSuggestion.getId());
        assertThat(relatedGifts).hasSize(2);

        long count = concreteGiftService.countByGiftSuggestionId(savedGiftSuggestion.getId());
        assertThat(count).isEqualTo(2);

        // Test pagination
        Pageable pageable = PageRequest.of(0, 1);
        Page<ConcreteGift> pagedResults = concreteGiftService.findByGiftSuggestionId(savedGiftSuggestion.getId(), pageable);
        assertThat(pagedResults.getContent()).hasSize(1);
        assertThat(pagedResults.getTotalElements()).isEqualTo(2);
    }

    @Test
    void concreteGiftService_VendorAndAvailabilityFiltering_ShouldWork() {
        // Given
        GiftSuggestion savedGiftSuggestion = giftSuggestionService.create(testGiftSuggestion);

        ConcreteGift amazonGift = new ConcreteGift(
                "Amazon Gift",
                "Available on Amazon",
                new BigDecimal("30.00"),
                "Amazon",
                savedGiftSuggestion
        );
        amazonGift.setAvailable(true);

        ConcreteGift ebayGift = new ConcreteGift(
                "eBay Gift",
                "Available on eBay",
                new BigDecimal("40.00"),
                "eBay",
                savedGiftSuggestion
        );
        ebayGift.setAvailable(false);

        concreteGiftService.create(amazonGift);
        concreteGiftService.create(ebayGift);

        // When & Then
        List<ConcreteGift> amazonGifts = concreteGiftService.findByVendorName("Amazon");
        assertThat(amazonGifts).hasSize(1);
        assertThat(amazonGifts.get(0).getName()).isEqualTo("Amazon Gift");

        List<ConcreteGift> availableGifts = concreteGiftService.findByAvailable(true);
        assertThat(availableGifts).hasSize(1);
        assertThat(availableGifts.get(0).getName()).isEqualTo("Amazon Gift");

        List<ConcreteGift> unavailableGifts = concreteGiftService.findByAvailable(false);
        assertThat(unavailableGifts).hasSize(1);
        assertThat(unavailableGifts.get(0).getName()).isEqualTo("eBay Gift");
    }

    @Test
    void concreteGiftService_PriceRangeFiltering_ShouldWork() {
        // Given
        GiftSuggestion savedGiftSuggestion = giftSuggestionService.create(testGiftSuggestion);

        ConcreteGift cheapGift = new ConcreteGift(
                "Cheap Gift",
                "Affordable option",
                new BigDecimal("15.00"),
                "Discount Store",
                savedGiftSuggestion
        );

        ConcreteGift expensiveGift = new ConcreteGift(
                "Expensive Gift",
                "Premium option",
                new BigDecimal("85.00"),
                "Premium Store",
                savedGiftSuggestion
        );

        concreteGiftService.create(cheapGift);
        concreteGiftService.create(expensiveGift);

        // When
        List<ConcreteGift> midRangeGifts = concreteGiftService.findByPriceRange(
                new BigDecimal("20.00"), new BigDecimal("90.00"));

        // Then
        assertThat(midRangeGifts).hasSize(1);
        assertThat(midRangeGifts.get(0).getName()).isEqualTo("Expensive Gift");
    }

    @Test
    void concreteGiftService_AdvancedCriteria_ShouldWork() {
        // Given
        GiftSuggestion savedGiftSuggestion = giftSuggestionService.create(testGiftSuggestion);

        ConcreteGift concreteGift = new ConcreteGift(
                "Advanced Search Gift",
                "For advanced search testing",
                new BigDecimal("50.00"),
                "TestVendor",
                savedGiftSuggestion
        );
        concreteGift.setAvailable(true);

        concreteGiftService.create(concreteGift);

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<ConcreteGift> results = concreteGiftService.findByAdvancedCriteria(
                savedGiftSuggestion.getId(),
                "TestVendor",
                true,
                new BigDecimal("40.00"),
                new BigDecimal("60.00"),
                pageable
        );

        // Then
        assertThat(results.getContent()).hasSize(1);
        assertThat(results.getContent().get(0).getName()).isEqualTo("Advanced Search Gift");
    }

    @Test
    void concreteGiftService_ValidationAssociation_ShouldWork() {
        // Given
        GiftSuggestion savedGiftSuggestion = giftSuggestionService.create(testGiftSuggestion);

        ConcreteGift validGift = new ConcreteGift(
                "Valid Gift",
                "Within price range",
                new BigDecimal("50.00"), // Within 10-100 range
                "TestVendor",
                savedGiftSuggestion
        );

        ConcreteGift invalidGift = new ConcreteGift(
                "Invalid Gift",
                "Outside price range",
                new BigDecimal("200.00"), // Above 100 range
                "TestVendor",
                savedGiftSuggestion
        );

        // When & Then - Valid gift should work
        ConcreteGift savedValidGift = concreteGiftService.create(validGift);
        assertThat(savedValidGift).isNotNull();

        // Invalid gift should throw exception
        assertThatThrownBy(() -> concreteGiftService.create(invalidGift))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("above maximum price");
    }

    @Test
    void serviceLayer_TransactionBehavior_ShouldWork() {
        // Given - Create a gift suggestion and concrete gift
        GiftSuggestion savedGiftSuggestion = giftSuggestionService.create(testGiftSuggestion);

        ConcreteGift concreteGift = new ConcreteGift(
                "Related Gift",
                "Should be deleted with suggestion",
                new BigDecimal("25.00"),
                "Amazon",
                savedGiftSuggestion
        );
        ConcreteGift savedConcreteGift = concreteGiftService.create(concreteGift);

        // Verify both exist
        assertThat(giftSuggestionService.existsById(savedGiftSuggestion.getId())).isTrue();
        assertThat(concreteGiftService.existsById(savedConcreteGift.getId())).isTrue();
        assertThat(concreteGiftService.countByGiftSuggestionId(savedGiftSuggestion.getId())).isEqualTo(1);

        // When - Delete concrete gift first
        concreteGiftService.deleteById(savedConcreteGift.getId());

        // Then - Only concrete gift should be deleted, suggestion should remain
        assertThat(giftSuggestionService.existsById(savedGiftSuggestion.getId())).isTrue();
        assertThat(concreteGiftService.existsById(savedConcreteGift.getId())).isFalse();
        assertThat(concreteGiftService.countByGiftSuggestionId(savedGiftSuggestion.getId())).isEqualTo(0);
    }

    @Test
    void serviceLayer_PaginationIntegration_ShouldWork() {
        // Given - Create multiple gift suggestions
        for (int i = 0; i < 15; i++) {
            GiftSuggestion gift = new GiftSuggestion(
                    "Gift " + i,
                    "Description " + i,
                    new BigDecimal("10.00"),
                    new BigDecimal("100.00"),
                    AgeGroup.ADULT,
                    Gender.UNISEX,
                    Interest.TECHNOLOGY,
                    Occasion.BIRTHDAY,
                    Relationship.FRIEND,
                    PersonalityType.CREATIVE
            );
            giftSuggestionService.create(gift);
        }

        // When - Test pagination
        Pageable firstPage = PageRequest.of(0, 5);
        Pageable secondPage = PageRequest.of(1, 5);
        Pageable thirdPage = PageRequest.of(2, 5);

        Page<GiftSuggestion> page1 = giftSuggestionService.findAll(firstPage);
        Page<GiftSuggestion> page2 = giftSuggestionService.findAll(secondPage);
        Page<GiftSuggestion> page3 = giftSuggestionService.findAll(thirdPage);

        // Then
        assertThat(page1.getContent()).hasSize(5);
        assertThat(page1.getTotalElements()).isEqualTo(15);
        assertThat(page1.getTotalPages()).isEqualTo(3);
        assertThat(page1.isFirst()).isTrue();
        assertThat(page1.isLast()).isFalse();

        assertThat(page2.getContent()).hasSize(5);
        assertThat(page2.isFirst()).isFalse();
        assertThat(page2.isLast()).isFalse();

        assertThat(page3.getContent()).hasSize(5);
        assertThat(page3.isFirst()).isFalse();
        assertThat(page3.isLast()).isTrue();
    }
}