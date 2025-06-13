package com.giftservice.entity;

import com.giftservice.enums.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive unit tests for GiftSuggestion entity.
 * Tests entity creation, validation, business logic, and relationships.
 */
class GiftSuggestionTest {

    private Validator validator;
    private GiftSuggestionTestDataBuilder testDataBuilder;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        testDataBuilder = new GiftSuggestionTestDataBuilder();
    }

    @Test
    void constructor_withValidData_shouldCreateEntity() {
        // When
        GiftSuggestion giftSuggestion = testDataBuilder.build();

        // Then
        assertThat(giftSuggestion.getName()).isEqualTo("Test Gift");
        assertThat(giftSuggestion.getDescription()).isEqualTo("A wonderful test gift");
        assertThat(giftSuggestion.getMinPrice()).isEqualTo(new BigDecimal("10.00"));
        assertThat(giftSuggestion.getMaxPrice()).isEqualTo(new BigDecimal("50.00"));
        assertThat(giftSuggestion.getAgeGroup()).isEqualTo(AgeGroup.ADULT);
        assertThat(giftSuggestion.getGender()).isEqualTo(Gender.UNISEX);
        assertThat(giftSuggestion.getInterest()).isEqualTo(Interest.READING);
        assertThat(giftSuggestion.getOccasion()).isEqualTo(Occasion.BIRTHDAY);
        assertThat(giftSuggestion.getRelationship()).isEqualTo(Relationship.FRIEND);
        assertThat(giftSuggestion.getPersonalityType()).isEqualTo(PersonalityType.INTELLECTUAL);
    }

    @Test
    void validation_withValidEntity_shouldPassValidation() {
        // Given
        GiftSuggestion giftSuggestion = testDataBuilder.build();

        // When
        Set<ConstraintViolation<GiftSuggestion>> violations = validator.validate(giftSuggestion);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_withBlankName_shouldFailValidation() {
        // Given
        GiftSuggestion giftSuggestion = testDataBuilder.withName("").build();

        // When
        Set<ConstraintViolation<GiftSuggestion>> violations = validator.validate(giftSuggestion);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Name is required");
    }

    @Test
    void validation_withTooLongName_shouldFailValidation() {
        // Given
        String longName = "a".repeat(101); // 101 characters
        GiftSuggestion giftSuggestion = testDataBuilder.withName(longName).build();

        // When
        Set<ConstraintViolation<GiftSuggestion>> violations = validator.validate(giftSuggestion);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("100 characters");
    }

    @Test
    void validation_withBlankDescription_shouldFailValidation() {
        // Given
        GiftSuggestion giftSuggestion = testDataBuilder.withDescription("").build();

        // When
        Set<ConstraintViolation<GiftSuggestion>> violations = validator.validate(giftSuggestion);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Description is required");
    }

    @Test
    void validation_withTooLongDescription_shouldFailValidation() {
        // Given
        String longDescription = "a".repeat(501); // 501 characters
        GiftSuggestion giftSuggestion = testDataBuilder.withDescription(longDescription).build();

        // When
        Set<ConstraintViolation<GiftSuggestion>> violations = validator.validate(giftSuggestion);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("500 characters");
    }

    @Test
    void validation_withNullMinPrice_shouldFailValidation() {
        // Given
        GiftSuggestion giftSuggestion = testDataBuilder.withMinPrice(null).build();

        // When
        Set<ConstraintViolation<GiftSuggestion>> violations = validator.validate(giftSuggestion);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Minimum price is required");
    }

    @Test
    void validation_withNegativeMinPrice_shouldFailValidation() {
        // Given
        GiftSuggestion giftSuggestion = testDataBuilder.withMinPrice(new BigDecimal("-1.00")).build();

        // When
        Set<ConstraintViolation<GiftSuggestion>> violations = validator.validate(giftSuggestion);

        // Then
        assertThat(violations).hasSizeGreaterThanOrEqualTo(1);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("positive"));
    }

    @Test
    void validation_withMinPriceGreaterThanMaxPrice_shouldFailValidation() {
        // Given
        GiftSuggestion giftSuggestion = testDataBuilder
            .withMinPrice(new BigDecimal("100.00"))
            .withMaxPrice(new BigDecimal("50.00"))
            .build();

        // When
        Set<ConstraintViolation<GiftSuggestion>> violations = validator.validate(giftSuggestion);

        // Then
        assertThat(violations).hasSizeGreaterThanOrEqualTo(1);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("greater than maximum"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.00", "10.50", "100.00", "999.99"})
    void validation_withValidPrices_shouldPassValidation(String priceString) {
        // Given
        BigDecimal price = new BigDecimal(priceString);
        GiftSuggestion giftSuggestion = testDataBuilder
            .withMinPrice(price)
            .withMaxPrice(price.add(new BigDecimal("10.00")))
            .build();

        // When
        Set<ConstraintViolation<GiftSuggestion>> violations = validator.validate(giftSuggestion);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_withNullEnumFields_shouldFailValidation() {
        // Given
        GiftSuggestion giftSuggestion = testDataBuilder
            .withAgeGroup(null)
            .withGender(null)
            .build();

        // When
        Set<ConstraintViolation<GiftSuggestion>> violations = validator.validate(giftSuggestion);

        // Then
        assertThat(violations).hasSize(2);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Age group is required"));
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Gender is required"));
    }

    @Test
    void equals_withSameId_shouldBeEqual() {
        // Given
        GiftSuggestion gift1 = testDataBuilder.build();
        GiftSuggestion gift2 = testDataBuilder.build();
        gift1.setId(java.util.UUID.randomUUID());
        gift2.setId(gift1.getId());

        // When & Then
        assertThat(gift1).isEqualTo(gift2);
        assertThat(gift1.hashCode()).isEqualTo(gift2.hashCode());
    }

    @Test
    void equals_withDifferentId_shouldNotBeEqual() {
        // Given
        GiftSuggestion gift1 = testDataBuilder.build();
        GiftSuggestion gift2 = testDataBuilder.build();
        gift1.setId(java.util.UUID.randomUUID());
        gift2.setId(java.util.UUID.randomUUID());

        // When & Then
        assertThat(gift1).isNotEqualTo(gift2);
    }

    @Test
    void toString_shouldContainAllImportantFields() {
        // Given
        GiftSuggestion giftSuggestion = testDataBuilder.build();
        giftSuggestion.setId(java.util.UUID.randomUUID());

        // When
        String toString = giftSuggestion.toString();

        // Then
        assertThat(toString).contains("Test Gift");
        assertThat(toString).contains("10.00");
        assertThat(toString).contains("50.00");
        assertThat(toString).contains("ADULT");
        assertThat(toString).contains("UNISEX");
    }

    @Test
    void concreteGifts_shouldInitializeAsEmptyList() {
        // Given
        GiftSuggestion giftSuggestion = new GiftSuggestion();

        // When & Then
        assertThat(giftSuggestion.getConcreteGifts()).isNotNull();
        assertThat(giftSuggestion.getConcreteGifts()).isEmpty();
    }

    @Test
    void addConcreteGift_shouldManageBidirectionalRelationship() {
        // Given
        GiftSuggestion giftSuggestion = testDataBuilder.build();
        ConcreteGift concreteGift = new ConcreteGift();

        // When
        giftSuggestion.addConcreteGift(concreteGift);

        // Then
        assertThat(giftSuggestion.getConcreteGifts()).contains(concreteGift);
        assertThat(concreteGift.getGiftSuggestion()).isEqualTo(giftSuggestion);
    }

    @Test
    void removeConcreteGift_shouldManageBidirectionalRelationship() {
        // Given
        GiftSuggestion giftSuggestion = testDataBuilder.build();
        ConcreteGift concreteGift = new ConcreteGift();
        giftSuggestion.addConcreteGift(concreteGift);

        // When
        giftSuggestion.removeConcreteGift(concreteGift);

        // Then
        assertThat(giftSuggestion.getConcreteGifts()).doesNotContain(concreteGift);
        assertThat(concreteGift.getGiftSuggestion()).isNull();
    }

    /**
     * Test data builder for GiftSuggestion entity
     */
    static class GiftSuggestionTestDataBuilder {
        private String name = "Test Gift";
        private String description = "A wonderful test gift";
        private BigDecimal minPrice = new BigDecimal("10.00");
        private BigDecimal maxPrice = new BigDecimal("50.00");
        private AgeGroup ageGroup = AgeGroup.ADULT;
        private Gender gender = Gender.UNISEX;
        private Interest interest = Interest.READING;
        private Occasion occasion = Occasion.BIRTHDAY;
        private Relationship relationship = Relationship.FRIEND;
        private PersonalityType personalityType = PersonalityType.INTELLECTUAL;

        public GiftSuggestionTestDataBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public GiftSuggestionTestDataBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public GiftSuggestionTestDataBuilder withMinPrice(BigDecimal minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public GiftSuggestionTestDataBuilder withMaxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public GiftSuggestionTestDataBuilder withAgeGroup(AgeGroup ageGroup) {
            this.ageGroup = ageGroup;
            return this;
        }

        public GiftSuggestionTestDataBuilder withGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public GiftSuggestion build() {
            return new GiftSuggestion(name, description, minPrice, maxPrice, 
                ageGroup, gender, interest, occasion, relationship, personalityType);
        }
    }
}