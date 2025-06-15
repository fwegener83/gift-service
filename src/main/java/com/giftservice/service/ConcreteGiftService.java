package com.giftservice.service;

import com.giftservice.entity.ConcreteGift;
import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing ConcreteGift entities.
 * Provides CRUD operations and relationship management with GiftSuggestion.
 */
public interface ConcreteGiftService {

    /**
     * Create a new concrete gift.
     *
     * @param concreteGift the concrete gift to create
     * @return the created concrete gift
     * @throws IllegalArgumentException if the concrete gift is invalid
     */
    ConcreteGift create(ConcreteGift concreteGift);

    /**
     * Find a concrete gift by its ID.
     *
     * @param id the ID of the concrete gift
     * @return an Optional containing the concrete gift if found
     */
    Optional<ConcreteGift> findById(UUID id);

    /**
     * Find all concrete gifts with pagination.
     *
     * @param pageable pagination parameters
     * @return a page of concrete gifts
     */
    Page<ConcreteGift> findAll(Pageable pageable);

    /**
     * Find all concrete gifts.
     *
     * @return list of all concrete gifts
     */
    List<ConcreteGift> findAll();

    /**
     * Update an existing concrete gift.
     *
     * @param id the ID of the concrete gift to update
     * @param concreteGift the updated concrete gift data
     * @return the updated concrete gift
     * @throws IllegalArgumentException if the concrete gift is invalid
     * @throws RuntimeException if the concrete gift is not found
     */
    ConcreteGift update(UUID id, ConcreteGift concreteGift);

    /**
     * Delete a concrete gift by its ID.
     *
     * @param id the ID of the concrete gift to delete
     * @throws RuntimeException if the concrete gift is not found
     */
    void deleteById(UUID id);

    /**
     * Check if a concrete gift exists by its ID.
     *
     * @param id the ID to check
     * @return true if the concrete gift exists, false otherwise
     */
    boolean existsById(UUID id);

    /**
     * Find all concrete gifts for a specific gift suggestion.
     *
     * @param giftSuggestionId the ID of the gift suggestion
     * @return list of concrete gifts for the suggestion
     */
    List<ConcreteGift> findByGiftSuggestionId(UUID giftSuggestionId);

    /**
     * Find all concrete gifts for a specific gift suggestion with pagination.
     *
     * @param giftSuggestionId the ID of the gift suggestion
     * @param pageable pagination parameters
     * @return a page of concrete gifts for the suggestion
     */
    Page<ConcreteGift> findByGiftSuggestionId(UUID giftSuggestionId, Pageable pageable);

    /**
     * Find concrete gifts by vendor name.
     *
     * @param vendorName the vendor name to filter by
     * @return list of matching concrete gifts
     */
    List<ConcreteGift> findByVendorName(String vendorName);

    /**
     * Find concrete gifts by availability status.
     *
     * @param available the availability status to filter by
     * @return list of matching concrete gifts
     */
    List<ConcreteGift> findByAvailable(boolean available);

    /**
     * Find concrete gifts within a price range.
     *
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of matching concrete gifts
     */
    List<ConcreteGift> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Advanced search with multiple optional criteria.
     *
     * @param giftSuggestionId optional gift suggestion ID filter
     * @param vendorName optional vendor name filter
     * @param available optional availability status filter
     * @param minPrice optional minimum price filter
     * @param maxPrice optional maximum price filter
     * @param pageable pagination parameters
     * @return a page of matching concrete gifts
     */
    Page<ConcreteGift> findByAdvancedCriteria(
            UUID giftSuggestionId,
            String vendorName,
            Boolean available,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    /**
     * Advanced search for concrete gifts with gift suggestion criteria.
     *
     * @param ageGroup optional age group filter from gift suggestion
     * @param gender optional gender filter from gift suggestion
     * @param interest optional interest filter from gift suggestion
     * @param occasion optional occasion filter from gift suggestion
     * @param relationship optional relationship filter from gift suggestion
     * @param personalityType optional personality type filter from gift suggestion
     * @param vendorName optional vendor name filter
     * @param available optional availability status filter
     * @param minPrice optional minimum price filter
     * @param maxPrice optional maximum price filter
     * @param pageable pagination parameters
     * @return a page of matching concrete gifts
     */
    Page<ConcreteGift> findBySuggestionAndCriteria(
            AgeGroup ageGroup,
            Gender gender,
            Interest interest,
            Occasion occasion,
            Relationship relationship,
            PersonalityType personalityType,
            String vendorName,
            Boolean available,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    /**
     * Count concrete gifts for a specific gift suggestion.
     *
     * @param giftSuggestionId the ID of the gift suggestion
     * @return count of concrete gifts for the suggestion
     */
    long countByGiftSuggestionId(UUID giftSuggestionId);

    /**
     * Validate that a concrete gift can be associated with a gift suggestion.
     *
     * @param concreteGift the concrete gift to validate
     * @param giftSuggestion the gift suggestion to associate with
     * @throws IllegalArgumentException if the association is invalid
     */
    void validateGiftSuggestionAssociation(ConcreteGift concreteGift, GiftSuggestion giftSuggestion);
}