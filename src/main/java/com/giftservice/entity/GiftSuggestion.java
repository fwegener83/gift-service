package com.giftservice.entity;

import com.giftservice.enums.*;
import com.giftservice.validation.ValidPriceRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Main entity representing gift suggestion concepts.
 * This entity aggregates all categorization attributes and serves as the foundation
 * for the gift recommendation system.
 */
@Entity
@Table(name = "gift_suggestions")
@EntityListeners(AuditingEntityListener.class)
@ValidPriceRange
public class GiftSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(nullable = false, length = 500)
    private String description;

    @NotNull(message = "Minimum price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Minimum price must be positive")
    @Column(name = "min_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal minPrice;

    @NotNull(message = "Maximum price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum price must be positive")
    @Column(name = "max_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal maxPrice;

    @NotNull(message = "Age group is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false)
    private AgeGroup ageGroup;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @NotNull(message = "Interest is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Interest interest;

    @NotNull(message = "Occasion is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Occasion occasion;

    @NotNull(message = "Relationship is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Relationship relationship;

    @NotNull(message = "Personality type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "personality_type", nullable = false)
    private PersonalityType personalityType;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "giftSuggestion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ConcreteGift> concreteGifts = new ArrayList<>();

    /**
     * Default constructor for JPA
     */
    public GiftSuggestion() {
    }

    /**
     * Constructor with all required fields
     */
    public GiftSuggestion(String name, String description, BigDecimal minPrice, BigDecimal maxPrice,
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

    // Getters and Setters

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

    public List<ConcreteGift> getConcreteGifts() {
        return concreteGifts;
    }

    public void setConcreteGifts(List<ConcreteGift> concreteGifts) {
        this.concreteGifts = concreteGifts;
    }

    // Convenience methods for managing the bidirectional relationship
    public void addConcreteGift(ConcreteGift concreteGift) {
        concreteGifts.add(concreteGift);
        concreteGift.setGiftSuggestion(this);
    }

    public void removeConcreteGift(ConcreteGift concreteGift) {
        concreteGifts.remove(concreteGift);
        concreteGift.setGiftSuggestion(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftSuggestion that = (GiftSuggestion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GiftSuggestion{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", ageGroup=" + ageGroup +
                ", gender=" + gender +
                ", interest=" + interest +
                ", occasion=" + occasion +
                ", relationship=" + relationship +
                ", personalityType=" + personalityType +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}