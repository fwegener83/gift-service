package com.giftservice.service;

import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing GiftSuggestion entities.
 * Provides CRUD operations and advanced search functionality.
 */
public interface GiftSuggestionService {

    /**
     * Create a new gift suggestion.
     *
     * @param giftSuggestion the gift suggestion to create
     * @return the created gift suggestion
     * @throws IllegalArgumentException if the gift suggestion is invalid
     */
    GiftSuggestion create(GiftSuggestion giftSuggestion);

    /**
     * Find a gift suggestion by its ID.
     *
     * @param id the ID of the gift suggestion
     * @return an Optional containing the gift suggestion if found
     */
    Optional<GiftSuggestion> findById(UUID id);

    /**
     * Find all gift suggestions with pagination.
     *
     * @param pageable pagination parameters
     * @return a page of gift suggestions
     */
    Page<GiftSuggestion> findAll(Pageable pageable);

    /**
     * Find all gift suggestions.
     *
     * @return list of all gift suggestions
     */
    List<GiftSuggestion> findAll();

    /**
     * Update an existing gift suggestion.
     *
     * @param id the ID of the gift suggestion to update
     * @param giftSuggestion the updated gift suggestion data
     * @return the updated gift suggestion
     * @throws IllegalArgumentException if the gift suggestion is invalid
     * @throws RuntimeException if the gift suggestion is not found
     */
    GiftSuggestion update(UUID id, GiftSuggestion giftSuggestion);

    /**
     * Delete a gift suggestion by its ID.
     *
     * @param id the ID of the gift suggestion to delete
     * @throws RuntimeException if the gift suggestion is not found
     */
    void deleteById(UUID id);

    /**
     * Check if a gift suggestion exists by its ID.
     *
     * @param id the ID to check
     * @return true if the gift suggestion exists, false otherwise
     */
    boolean existsById(UUID id);

    /**
     * Find gift suggestions by age group.
     *
     * @param ageGroup the age group to filter by
     * @return list of matching gift suggestions
     */
    List<GiftSuggestion> findByAgeGroup(AgeGroup ageGroup);

    /**
     * Find gift suggestions by gender.
     *
     * @param gender the gender to filter by
     * @return list of matching gift suggestions
     */
    List<GiftSuggestion> findByGender(Gender gender);

    /**
     * Find gift suggestions by interest.
     *
     * @param interest the interest to filter by
     * @return list of matching gift suggestions
     */
    List<GiftSuggestion> findByInterest(Interest interest);

    /**
     * Find gift suggestions by occasion.
     *
     * @param occasion the occasion to filter by
     * @return list of matching gift suggestions
     */
    List<GiftSuggestion> findByOccasion(Occasion occasion);

    /**
     * Find gift suggestions by relationship.
     *
     * @param relationship the relationship to filter by
     * @return list of matching gift suggestions
     */
    List<GiftSuggestion> findByRelationship(Relationship relationship);

    /**
     * Find gift suggestions by personality type.
     *
     * @param personalityType the personality type to filter by
     * @return list of matching gift suggestions
     */
    List<GiftSuggestion> findByPersonalityType(PersonalityType personalityType);

    /**
     * Find gift suggestions within a price range.
     *
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of matching gift suggestions
     */
    List<GiftSuggestion> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Advanced search with multiple optional criteria.
     *
     * @param ageGroup optional age group filter
     * @param gender optional gender filter
     * @param interest optional interest filter
     * @param occasion optional occasion filter
     * @param relationship optional relationship filter
     * @param personalityType optional personality type filter
     * @param maxBudget optional maximum budget filter
     * @param pageable pagination parameters
     * @return a page of matching gift suggestions
     */
    Page<GiftSuggestion> findByAdvancedCriteria(
            AgeGroup ageGroup,
            Gender gender,
            Interest interest,
            Occasion occasion,
            Relationship relationship,
            PersonalityType personalityType,
            BigDecimal maxBudget,
            Pageable pageable
    );

    /**
     * Count gift suggestions matching advanced criteria.
     *
     * @param ageGroup optional age group filter
     * @param gender optional gender filter
     * @param interest optional interest filter
     * @param occasion optional occasion filter
     * @param relationship optional relationship filter
     * @param personalityType optional personality type filter
     * @param maxBudget optional maximum budget filter
     * @return count of matching gift suggestions
     */
    long countByAdvancedCriteria(
            AgeGroup ageGroup,
            Gender gender,
            Interest interest,
            Occasion occasion,
            Relationship relationship,
            PersonalityType personalityType,
            BigDecimal maxBudget
    );
}