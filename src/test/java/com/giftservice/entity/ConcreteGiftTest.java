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
 * Comprehensive unit tests for ConcreteGift entity.
 * Tests entity creation, validation, business logic, and relationships.
 */
class ConcreteGiftTest {

    private Validator validator;
    private ConcreteGiftTestDataBuilder testDataBuilder;
    private GiftSuggestion testGiftSuggestion;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        // Create a valid GiftSuggestion for relationship testing
        testGiftSuggestion = new GiftSuggestion("Test Suggestion", "Test Description",
            new BigDecimal("10.00"), new BigDecimal("100.00"),
            AgeGroup.ADULT, Gender.UNISEX, Interest.READING, Occasion.BIRTHDAY,
            Relationship.FRIEND, PersonalityType.INTELLECTUAL);
        
        testDataBuilder = new ConcreteGiftTestDataBuilder(testGiftSuggestion);
    }

    @Test
    void constructor_withEssentialFields_shouldCreateEntity() {
        // When
        ConcreteGift concreteGift = testDataBuilder.build();

        // Then
        assertThat(concreteGift.getName()).isEqualTo("Test Concrete Gift");
        assertThat(concreteGift.getDescription()).isEqualTo("A specific test gift");
        assertThat(concreteGift.getExactPrice()).isEqualTo(new BigDecimal("25.99"));
        assertThat(concreteGift.getVendorName()).isEqualTo("Test Vendor");
        assertThat(concreteGift.getGiftSuggestion()).isEqualTo(testGiftSuggestion);
        assertThat(concreteGift.getAvailable()).isTrue();
    }

    @Test
    void constructor_withAllFields_shouldCreateEntity() {
        // When
        ConcreteGift concreteGift = testDataBuilder
            .withProductUrl("https://example.com/product")
            .withProductSku("SKU123")
            .withAvailable(false)
            .build();

        // Then
        assertThat(concreteGift.getProductUrl()).isEqualTo("https://example.com/product");
        assertThat(concreteGift.getProductSku()).isEqualTo("SKU123");
        assertThat(concreteGift.getAvailable()).isFalse();
    }

    @Test
    void validation_withValidEntity_shouldPassValidation() {
        // Given
        ConcreteGift concreteGift = testDataBuilder.build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_withBlankName_shouldFailValidation() {
        // Given
        ConcreteGift concreteGift = testDataBuilder.withName("").build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Name is required");
    }

    @Test
    void validation_withTooLongName_shouldFailValidation() {
        // Given
        String longName = "a".repeat(151); // 151 characters
        ConcreteGift concreteGift = testDataBuilder.withName(longName).build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("150 characters");
    }

    @Test
    void validation_withTooLongDescription_shouldFailValidation() {
        // Given
        String longDescription = "a".repeat(1001); // 1001 characters
        ConcreteGift concreteGift = testDataBuilder.withDescription(longDescription).build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("1000 characters");
    }

    @Test
    void validation_withNullExactPrice_shouldFailValidation() {
        // Given
        ConcreteGift concreteGift = testDataBuilder.withExactPrice(null).build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Exact price is required");
    }

    @Test
    void validation_withNegativeExactPrice_shouldFailValidation() {
        // Given
        ConcreteGift concreteGift = testDataBuilder.withExactPrice(new BigDecimal("-1.00")).build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("positive");
    }

    @Test
    void validation_withZeroExactPrice_shouldFailValidation() {
        // Given
        ConcreteGift concreteGift = testDataBuilder.withExactPrice(BigDecimal.ZERO).build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("positive");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.01", "1.00", "25.99", "100.00", "999.99"})
    void validation_withValidPositivePrices_shouldPassValidation(String priceString) {
        // Given
        ConcreteGift concreteGift = testDataBuilder.withExactPrice(new BigDecimal(priceString)).build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_withBlankVendorName_shouldFailValidation() {
        // Given
        ConcreteGift concreteGift = testDataBuilder.withVendorName("").build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Vendor name is required");
    }

    @Test
    void validation_withTooLongVendorName_shouldFailValidation() {
        // Given
        String longVendorName = "a".repeat(101); // 101 characters
        ConcreteGift concreteGift = testDataBuilder.withVendorName(longVendorName).build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("100 characters");
    }

    @Test
    void validation_withInvalidProductUrl_shouldFailValidation() {
        // Given
        ConcreteGift concreteGift = testDataBuilder.withProductUrl("invalid-url").build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("valid URL");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://example.com",
        "http://shop.example.com/product/123",
        "https://www.amazon.com/dp/B08N5WRWNW"
    })
    void validation_withValidProductUrl_shouldPassValidation(String url) {
        // Given
        ConcreteGift concreteGift = testDataBuilder.withProductUrl(url).build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_withTooLongProductSku_shouldFailValidation() {
        // Given
        String longSku = "a".repeat(51); // 51 characters
        ConcreteGift concreteGift = testDataBuilder.withProductSku(longSku).build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("50 characters");
    }

    @Test
    void validation_withNullGiftSuggestion_shouldFailValidation() {
        // Given
        ConcreteGift concreteGift = testDataBuilder.withGiftSuggestion(null).build();

        // When
        Set<ConstraintViolation<ConcreteGift>> violations = validator.validate(concreteGift);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Gift suggestion is required");
    }

    @Test
    void equals_withSameId_shouldBeEqual() {
        // Given
        ConcreteGift gift1 = testDataBuilder.build();
        ConcreteGift gift2 = testDataBuilder.build();
        java.util.UUID id = java.util.UUID.randomUUID();
        gift1.setId(id);
        gift2.setId(id);

        // When & Then
        assertThat(gift1).isEqualTo(gift2);
        assertThat(gift1.hashCode()).isEqualTo(gift2.hashCode());
    }

    @Test
    void equals_withDifferentId_shouldNotBeEqual() {
        // Given
        ConcreteGift gift1 = testDataBuilder.build();
        ConcreteGift gift2 = testDataBuilder.build();
        gift1.setId(java.util.UUID.randomUUID());
        gift2.setId(java.util.UUID.randomUUID());

        // When & Then
        assertThat(gift1).isNotEqualTo(gift2);
    }

    @Test
    void toString_shouldContainAllImportantFields() {
        // Given
        ConcreteGift concreteGift = testDataBuilder
            .withProductUrl("https://example.com")
            .withProductSku("SKU123")
            .build();
        concreteGift.setId(java.util.UUID.randomUUID());

        // When
        String toString = concreteGift.toString();

        // Then
        assertThat(toString).contains("Test Concrete Gift");
        assertThat(toString).contains("25.99");
        assertThat(toString).contains("Test Vendor");
        assertThat(toString).contains("https://example.com");
        assertThat(toString).contains("SKU123");
        assertThat(toString).contains("true"); // available
    }

    @Test
    void defaultConstructor_shouldSetDefaultValues() {
        // When
        ConcreteGift concreteGift = new ConcreteGift();

        // Then
        assertThat(concreteGift.getAvailable()).isTrue(); // Default value should be true
    }

    /**
     * Test data builder for ConcreteGift entity
     */
    static class ConcreteGiftTestDataBuilder {
        private String name = "Test Concrete Gift";
        private String description = "A specific test gift";
        private BigDecimal exactPrice = new BigDecimal("25.99");
        private String vendorName = "Test Vendor";
        private String productUrl;
        private String productSku;
        private Boolean available = true;
        private GiftSuggestion giftSuggestion;

        public ConcreteGiftTestDataBuilder(GiftSuggestion giftSuggestion) {
            this.giftSuggestion = giftSuggestion;
        }

        public ConcreteGiftTestDataBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ConcreteGiftTestDataBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ConcreteGiftTestDataBuilder withExactPrice(BigDecimal exactPrice) {
            this.exactPrice = exactPrice;
            return this;
        }

        public ConcreteGiftTestDataBuilder withVendorName(String vendorName) {
            this.vendorName = vendorName;
            return this;
        }

        public ConcreteGiftTestDataBuilder withProductUrl(String productUrl) {
            this.productUrl = productUrl;
            return this;
        }

        public ConcreteGiftTestDataBuilder withProductSku(String productSku) {
            this.productSku = productSku;
            return this;
        }

        public ConcreteGiftTestDataBuilder withAvailable(Boolean available) {
            this.available = available;
            return this;
        }

        public ConcreteGiftTestDataBuilder withGiftSuggestion(GiftSuggestion giftSuggestion) {
            this.giftSuggestion = giftSuggestion;
            return this;
        }

        public ConcreteGift build() {
            if (productUrl != null || productSku != null) {
                return new ConcreteGift(name, description, exactPrice, vendorName, 
                    productUrl, productSku, available, giftSuggestion);
            } else {
                return new ConcreteGift(name, description, exactPrice, vendorName, giftSuggestion);
            }
        }
    }
}