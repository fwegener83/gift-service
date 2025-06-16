package com.giftservice.dto.request;

import com.giftservice.enums.*;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * DTO for updating an existing gift suggestion.
 */
@Schema(description = "Request to update an existing gift suggestion")
public class UpdateGiftSuggestionRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Schema(description = "Name of the gift suggestion", example = "Updated Wireless Bluetooth Headphones", required = true)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Schema(description = "Detailed description of the gift suggestion", 
            example = "Updated high-quality wireless headphones perfect for music lovers", required = true)
    private String description;

    @NotNull(message = "Minimum price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Minimum price must be positive")
    @Schema(description = "Minimum price range for the gift", example = "30.00", required = true)
    private BigDecimal minPrice;

    @NotNull(message = "Maximum price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum price must be positive")
    @Schema(description = "Maximum price range for the gift", example = "200.00", required = true)
    private BigDecimal maxPrice;

    @NotNull(message = "Age group is required")
    @Schema(description = "Target age group for the gift", example = "YOUNG_ADULT", required = true)
    private AgeGroup ageGroup;

    @NotNull(message = "Gender is required")
    @Schema(description = "Target gender for the gift", example = "MALE", required = true)
    private Gender gender;

    @NotNull(message = "Interest is required")
    @Schema(description = "Interest category for the gift", example = "MUSIC", required = true)
    private Interest interest;

    @NotNull(message = "Occasion is required")
    @Schema(description = "Occasion for giving the gift", example = "GRADUATION", required = true)
    private Occasion occasion;

    @NotNull(message = "Relationship is required")
    @Schema(description = "Relationship to the gift recipient", example = "FAMILY", required = true)
    private Relationship relationship;

    @NotNull(message = "Personality type is required")
    @Schema(description = "Personality type of the gift recipient", example = "ADVENTUROUS", required = true)
    private PersonalityType personalityType;

    // Default constructor
    public UpdateGiftSuggestionRequest() {}

    // Constructor with all fields
    public UpdateGiftSuggestionRequest(String name, String description, BigDecimal minPrice, BigDecimal maxPrice,
                                     AgeGroup ageGroup, Gender gender, Interest interest, Occasion occasion,
                                     Relationship relationship, PersonalityType personalityType) {
        this.name = name;
        this.description = description;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.ageGroup = ageGroup;
        this.gender = gender;
        this.interest = interest;
        this.occasion = occasion;
        this.relationship = relationship;
        this.personalityType = personalityType;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Interest getInterest() {
        return interest;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }

    public Occasion getOccasion() {
        return occasion;
    }

    public void setOccasion(Occasion occasion) {
        this.occasion = occasion;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public PersonalityType getPersonalityType() {
        return personalityType;
    }

    public void setPersonalityType(PersonalityType personalityType) {
        this.personalityType = personalityType;
    }
}