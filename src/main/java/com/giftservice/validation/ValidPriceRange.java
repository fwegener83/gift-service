package com.giftservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation to ensure that minPrice is less than or equal to maxPrice
 * and both prices are positive values.
 * 
 * This annotation can be applied at the class level to validate the relationship
 * between multiple fields in an entity.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriceRangeValidator.class)
@Documented
public @interface ValidPriceRange {
    
    /**
     * The error message to display when validation fails
     */
    String message() default "Minimum price must be less than or equal to maximum price, and both must be positive";
    
    /**
     * Allows the specification of validation groups
     */
    Class<?>[] groups() default {};
    
    /**
     * Can be used to assign custom payload objects to a constraint
     */
    Class<? extends Payload>[] payload() default {};
}