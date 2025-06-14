package com.giftservice.repository;

import com.giftservice.entity.ConcreteGift;
import com.giftservice.entity.GiftSuggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    // Price range query methods

    /**
     * Find concrete gifts within a specific price range.
     *
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of concrete gifts within the price range
     */
    @Query("SELECT cg FROM ConcreteGift cg WHERE cg.price >= :minPrice AND cg.price <= :maxPrice")
    List<ConcreteGift> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Find concrete gifts under a specific price limit.
     *
     * @param maxPrice the maximum price limit
     * @return list of concrete gifts under the price limit
     */
    List<ConcreteGift> findByPriceLessThanEqual(BigDecimal maxPrice);

    /**
     * Find concrete gifts above a specific price threshold.
     *
     * @param minPrice the minimum price threshold
     * @return list of concrete gifts above the price threshold
     */
    List<ConcreteGift> findByPriceGreaterThanEqual(BigDecimal minPrice);

    // Advanced queries with pagination

    /**
     * Find concrete gifts by gift suggestion with pagination.
     *
     * @param giftSuggestion the gift suggestion entity
     * @param pageable pagination information
     * @return page of concrete gifts linked to the gift suggestion
     */
    Page<ConcreteGift> findByGiftSuggestion(GiftSuggestion giftSuggestion, Pageable pageable);

    /**
     * Find available concrete gifts by vendor with pagination.
     *
     * @param vendor the vendor name
     * @param available the availability status
     * @param pageable pagination information
     * @return page of available concrete gifts from the vendor
     */
    Page<ConcreteGift> findByVendorAndAvailable(String vendor, Boolean available, Pageable pageable);

    /**
     * Find concrete gifts by advanced criteria including price range and availability.
     *
     * @param vendor the vendor name (can be null)
     * @param available the availability status (can be null)
     * @param minPrice the minimum price (can be null)
     * @param maxPrice the maximum price (can be null)
     * @param pageable pagination information
     * @return page of concrete gifts matching the criteria
     */
    @Query("SELECT cg FROM ConcreteGift cg WHERE " +
           "(:vendor IS NULL OR cg.vendor = :vendor) AND " +
           "(:available IS NULL OR cg.available = :available) AND " +
           "(:minPrice IS NULL OR cg.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR cg.price <= :maxPrice)")
    Page<ConcreteGift> findByAdvancedCriteria(
            @Param("vendor") String vendor,
            @Param("available") Boolean available,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    /**
     * Find concrete gifts by gift suggestion criteria and price range.
     * This method joins with GiftSuggestion to filter by both concrete gift and suggestion properties.
     *
     * @param giftSuggestionId the gift suggestion ID (can be null)
     * @param available the availability status (can be null)
     * @param minPrice the minimum concrete gift price (can be null)
     * @param maxPrice the maximum concrete gift price (can be null)
     * @param pageable pagination information
     * @return page of concrete gifts matching all criteria
     */
    @Query("SELECT cg FROM ConcreteGift cg WHERE " +
           "(:giftSuggestionId IS NULL OR cg.giftSuggestion.id = :giftSuggestionId) AND " +
           "(:available IS NULL OR cg.available = :available) AND " +
           "(:minPrice IS NULL OR cg.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR cg.price <= :maxPrice)")
    Page<ConcreteGift> findBySuggestionAndCriteria(
            @Param("giftSuggestionId") UUID giftSuggestionId,
            @Param("available") Boolean available,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    /**
     * Count concrete gifts by vendor and availability.
     *
     * @param vendor the vendor name (can be null)
     * @param available the availability status (can be null)
     * @return count of concrete gifts matching the criteria
     */
    @Query("SELECT COUNT(cg) FROM ConcreteGift cg WHERE " +
           "(:vendor IS NULL OR cg.vendor = :vendor) AND " +
           "(:available IS NULL OR cg.available = :available)")
    long countByVendorAndAvailable(
            @Param("vendor") String vendor,
            @Param("available") Boolean available);
}