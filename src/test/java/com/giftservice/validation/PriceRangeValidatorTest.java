package com.giftservice.validation;

import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for PriceRangeValidator.
 * Tests all validation scenarios and edge cases.
 */
@ExtendWith(MockitoExtension.class)
class PriceRangeValidatorTest {

    private PriceRangeValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

    @BeforeEach
    void setUp() {
        validator = new PriceRangeValidator();
        validator.initialize(null); // No initialization needed
    }
    
    private void setupMocksForConstraintViolation() {
        // Setup mock chain for constraint violation building
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    void isValid_withNullGiftSuggestion_shouldReturnTrue() {
        // When
        boolean result = validator.isValid(null, context);

        // Then
        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void isValid_withNullPrices_shouldReturnTrue() {
        // Given
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(null);
        giftSuggestion.setMaxPrice(null);

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void isValid_withNullMinPrice_shouldReturnTrue() {
        // Given
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(null);
        giftSuggestion.setMaxPrice(new BigDecimal("50.00"));

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void isValid_withNullMaxPrice_shouldReturnTrue() {
        // Given
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(new BigDecimal("10.00"));
        giftSuggestion.setMaxPrice(null);

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void isValid_withValidPriceRange_shouldReturnTrue() {
        // Given
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(new BigDecimal("10.00"));
        giftSuggestion.setMaxPrice(new BigDecimal("50.00"));

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void isValid_withEqualPrices_shouldReturnTrue() {
        // Given
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        BigDecimal samePrice = new BigDecimal("25.00");
        giftSuggestion.setMinPrice(samePrice);
        giftSuggestion.setMaxPrice(samePrice);

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void isValid_withNegativeMinPrice_shouldReturnFalse() {
        // Given
        setupMocksForConstraintViolation();
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(new BigDecimal("-10.00"));
        giftSuggestion.setMaxPrice(new BigDecimal("50.00"));

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Minimum price must be positive");
        verify(violationBuilder).addPropertyNode("minPrice");
        verify(nodeBuilder).addConstraintViolation();
    }

    @Test
    void isValid_withZeroMinPrice_shouldReturnFalse() {
        // Given
        setupMocksForConstraintViolation();
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(BigDecimal.ZERO);
        giftSuggestion.setMaxPrice(new BigDecimal("50.00"));

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Minimum price must be positive");
        verify(violationBuilder).addPropertyNode("minPrice");
        verify(nodeBuilder).addConstraintViolation();
    }

    @Test
    void isValid_withNegativeMaxPrice_shouldReturnFalse() {
        // Given
        setupMocksForConstraintViolation();
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(new BigDecimal("10.00"));
        giftSuggestion.setMaxPrice(new BigDecimal("-5.00"));

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Maximum price must be positive");
        verify(violationBuilder).addPropertyNode("maxPrice");
        verify(nodeBuilder).addConstraintViolation();
    }

    @Test
    void isValid_withZeroMaxPrice_shouldReturnFalse() {
        // Given
        setupMocksForConstraintViolation();
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(new BigDecimal("10.00"));
        giftSuggestion.setMaxPrice(BigDecimal.ZERO);

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Maximum price must be positive");
        verify(violationBuilder).addPropertyNode("maxPrice");
        verify(nodeBuilder).addConstraintViolation();
    }

    @Test
    void isValid_withMinPriceGreaterThanMaxPrice_shouldReturnFalse() {
        // Given
        setupMocksForConstraintViolation();
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(new BigDecimal("100.00"));
        giftSuggestion.setMaxPrice(new BigDecimal("50.00"));

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Minimum price cannot be greater than maximum price");
        verify(violationBuilder).addPropertyNode("minPrice");
        verify(nodeBuilder).addConstraintViolation();
    }

    @Test
    void isValid_withVerySmallPositivePrices_shouldReturnTrue() {
        // Given
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(new BigDecimal("0.01"));
        giftSuggestion.setMaxPrice(new BigDecimal("0.02"));

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void isValid_withVeryLargePrices_shouldReturnTrue() {
        // Given
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(new BigDecimal("9999.99"));
        giftSuggestion.setMaxPrice(new BigDecimal("99999.99"));

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void isValid_withPreciseBigDecimalValues_shouldReturnTrue() {
        // Given
        GiftSuggestion giftSuggestion = createValidGiftSuggestion();
        giftSuggestion.setMinPrice(new BigDecimal("10.555"));
        giftSuggestion.setMaxPrice(new BigDecimal("10.556"));

        // When
        boolean result = validator.isValid(giftSuggestion, context);

        // Then
        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void initialize_shouldNotThrowException() {
        // Given
        ValidPriceRange annotation = mock(ValidPriceRange.class);
        PriceRangeValidator newValidator = new PriceRangeValidator();

        // When & Then - should not throw any exception
        newValidator.initialize(annotation);
    }

    /**
     * Helper method to create a valid GiftSuggestion for testing
     */
    private GiftSuggestion createValidGiftSuggestion() {
        return new GiftSuggestion(
            "Test Gift",
            "Test Description",
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