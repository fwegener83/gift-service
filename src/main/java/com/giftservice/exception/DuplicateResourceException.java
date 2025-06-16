package com.giftservice.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
public class DuplicateResourceException extends RuntimeException {

    private final String resourceType;
    private final Object conflictingValue;

    public DuplicateResourceException(String resourceType, Object conflictingValue) {
        super(String.format("%s already exists with value: %s", resourceType, conflictingValue));
        this.resourceType = resourceType;
        this.conflictingValue = conflictingValue;
    }

    public DuplicateResourceException(String message) {
        super(message);
        this.resourceType = "Resource";
        this.conflictingValue = null;
    }

    public String getResourceType() {
        return resourceType;
    }

    public Object getConflictingValue() {
        return conflictingValue;
    }
}