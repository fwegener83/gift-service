package com.giftservice.exception;

/**
 * Exception thrown when business validation rules are violated.
 */
public class BusinessValidationException extends RuntimeException {

    private final String validationRule;
    private final Object invalidValue;

    public BusinessValidationException(String message) {
        super(message);
        this.validationRule = null;
        this.invalidValue = null;
    }

    public BusinessValidationException(String validationRule, Object invalidValue, String message) {
        super(message);
        this.validationRule = validationRule;
        this.invalidValue = invalidValue;
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
        this.validationRule = null;
        this.invalidValue = null;
    }

    public String getValidationRule() {
        return validationRule;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }
}