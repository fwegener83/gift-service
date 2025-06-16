package com.giftservice.dto.response;

import com.giftservice.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for gift suggestion response.
 */
@Schema(description = "Gift suggestion response")
public class GiftSuggestionResponse {

    @Schema(description = "Unique identifier of the gift suggestion", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Name of the gift suggestion", example = "Wireless Bluetooth Headphones")
    private String name;

    @Schema(description = "Detailed description of the gift suggestion", 
            example = "High-quality wireless headphones perfect for music lovers")
    private String description;

    @Schema(description = "Minimum price range for the gift", example = "25.00")
    private BigDecimal minPrice;

    @Schema(description = "Maximum price range for the gift", example = "150.00")
    private BigDecimal maxPrice;

    @Schema(description = "Target age group for the gift", example = "ADULT")
    private AgeGroup ageGroup;

    @Schema(description = "Target gender for the gift", example = "UNISEX")
    private Gender gender;

    @Schema(description = "Interest category for the gift", example = "TECHNOLOGY")
    private Interest interest;

    @Schema(description = "Occasion for giving the gift", example = "BIRTHDAY")
    private Occasion occasion;

    @Schema(description = "Relationship to the gift recipient", example = "FRIEND")
    private Relationship relationship;

    @Schema(description = "Personality type of the gift recipient", example = "CREATIVE")
    private PersonalityType personalityType;

    @Schema(description = "Date and time when the gift suggestion was created")
    private LocalDateTime createdDate;

    @Schema(description = "Date and time when the gift suggestion was last modified")
    private LocalDateTime lastModifiedDate;

    @Schema(description = "List of concrete gift implementations")
    private List<ConcreteGiftResponse> concreteGifts;

    // Default constructor
    public GiftSuggestionResponse() {}

    // Constructor with all fields
    public GiftSuggestionResponse(UUID id, String name, String description, BigDecimal minPrice, BigDecimal maxPrice,
                                AgeGroup ageGroup, Gender gender, Interest interest, Occasion occasion,
                                Relationship relationship, PersonalityType personalityType,
                                LocalDateTime createdDate, LocalDateTime lastModifiedDate,
                                List<ConcreteGiftResponse> concreteGifts) {
        this.id = id;
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
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.concreteGifts = concreteGifts;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<ConcreteGiftResponse> getConcreteGifts() {
        return concreteGifts;
    }

    public void setConcreteGifts(List<ConcreteGiftResponse> concreteGifts) {
        this.concreteGifts = concreteGifts;
    }
}