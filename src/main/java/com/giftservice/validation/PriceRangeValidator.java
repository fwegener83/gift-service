package com.giftservice.validation;

import com.giftservice.entity.GiftSuggestion;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

/**
 * Custom validator implementation for the @ValidPriceRange annotation.
 * 
 * This validator ensures that:
 * 1. Both minPrice and maxPrice are not null
 * 2. Both prices are positive (greater than zero)
 * 3. minPrice is less than or equal to maxPrice
 */
public class PriceRangeValidator implements ConstraintValidator<ValidPriceRange, GiftSuggestion> {

    @Override
    public void initialize(ValidPriceRange constraintAnnotation) {
        // No initialization needed for this validator
    }

    @Override
    public boolean isValid(GiftSuggestion giftSuggestion, ConstraintValidatorContext context) {
        // If the object is null, let other validators handle it
        if (giftSuggestion == null) {
            return true;
        }

        BigDecimal minPrice = giftSuggestion.getMinPrice();
        BigDecimal maxPrice = giftSuggestion.getMaxPrice();

        // If either price is null, let individual field validators handle it
        if (minPrice == null || maxPrice == null) {
            return true;
        }

        // Check if both prices are positive
        if (minPrice.compareTo(BigDecimal.ZERO) <= 0) {
            addConstraintViolation(context, "Minimum price must be positive", "minPrice");
            return false;
        }

        if (maxPrice.compareTo(BigDecimal.ZERO) <= 0) {
            addConstraintViolation(context, "Maximum price must be positive", "maxPrice");
            return false;
        }

        // Check if minPrice <= maxPrice
        if (minPrice.compareTo(maxPrice) > 0) {
            addConstraintViolation(context, "Minimum price cannot be greater than maximum price", "minPrice");
            return false;
        }

        return true;
    }

    /**
     * Helper method to add a constraint violation with a specific property path
     */
    private void addConstraintViolation(ConstraintValidatorContext context, String message, String propertyPath) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addPropertyNode(propertyPath)
               .addConstraintViolation();
    }
}