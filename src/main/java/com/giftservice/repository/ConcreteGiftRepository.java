package com.giftservice.repository;

import com.giftservice.entity.ConcreteGift;
import com.giftservice.entity.GiftSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for ConcreteGift entity.
 * Provides CRUD operations and custom query methods for finding concrete gifts
 * based on relationships, vendor information, and availability status.
 */
@Repository
public interface ConcreteGiftRepository extends JpaRepository<ConcreteGift, UUID> {

    /**
     * Find all concrete gifts associated with a specific gift suggestion.
     *
     * @param giftSuggestion the gift suggestion entity
     * @return list of concrete gifts linked to the given gift suggestion
     */
    List<ConcreteGift> findByGiftSuggestion(GiftSuggestion giftSuggestion);

    /**
     * Find all concrete gifts associated with a specific gift suggestion by ID.
     *
     * @param giftSuggestionId the UUID of the gift suggestion
     * @return list of concrete gifts linked to the gift suggestion with the given ID
     */
    List<ConcreteGift> findByGiftSuggestionId(UUID giftSuggestionId);

    /**
     * Find all concrete gifts from a specific vendor.
     *
     * @param vendor the vendor name
     * @return list of concrete gifts from the specified vendor
     */
    List<ConcreteGift> findByVendor(String vendor);

    /**
     * Find all concrete gifts by availability status.
     *
     * @param available the availability status (true for available, false for unavailable)
     * @return list of concrete gifts with the specified availability status
     */
    List<ConcreteGift> findByAvailable(Boolean available);

    /**
     * Find all available concrete gifts from a specific vendor.
     *
     * @param vendor the vendor name
     * @param available the availability status
     * @return list of available concrete gifts from the specified vendor
     */
    List<ConcreteGift> findByVendorAndAvailable(String vendor, Boolean available);

    /**
     * Find all concrete gifts for a specific gift suggestion that are available.
     *
     * @param giftSuggestion the gift suggestion entity
     * @param available the availability status
     * @return list of available concrete gifts linked to the given gift suggestion
     */
    List<ConcreteGift> findByGiftSuggestionAndAvailable(GiftSuggestion giftSuggestion, Boolean available);

    /**
     * Find all concrete gifts for a specific gift suggestion ID that are available.
     *
     * @param giftSuggestionId the UUID of the gift suggestion
     * @param available the availability status
     * @return list of available concrete gifts linked to the gift suggestion with the given ID
     */
    List<ConcreteGift> findByGiftSuggestionIdAndAvailable(UUID giftSuggestionId, Boolean available);

    /**
     * Find all available concrete gifts (convenience method).
     *
     * @return list of all available concrete gifts
     */
    default List<ConcreteGift> findAllAvailable() {
        return findByAvailable(true);
    }

    /**
     * Find all unavailable concrete gifts (convenience method).
     *
     * @return list of all unavailable concrete gifts
     */
    default List<ConcreteGift> findAllUnavailable() {
        return findByAvailable(false);
    }
}