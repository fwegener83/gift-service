package com.giftservice.repository;

import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
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
 * Repository interface for GiftSuggestion entity.
 * Provides CRUD operations and custom query methods for filtering gift suggestions
 * based on various criteria such as enums, price ranges, and combinations thereof.
 */
@Repository
public interface GiftSuggestionRepository extends JpaRepository<GiftSuggestion, UUID> {

    /**
     * Find gift suggestions by age group.
     *
     * @param ageGroup the target age group
     * @return list of gift suggestions for the specified age group
     */
    List<GiftSuggestion> findByAgeGroup(AgeGroup ageGroup);

    /**
     * Find gift suggestions by gender.
     *
     * @param gender the target gender
     * @return list of gift suggestions for the specified gender
     */
    List<GiftSuggestion> findByGender(Gender gender);

    /**
     * Find gift suggestions by interest.
     *
     * @param interest the target interest
     * @return list of gift suggestions for the specified interest
     */
    List<GiftSuggestion> findByInterest(Interest interest);

    /**
     * Find gift suggestions by occasion.
     *
     * @param occasion the target occasion
     * @return list of gift suggestions for the specified occasion
     */
    List<GiftSuggestion> findByOccasion(Occasion occasion);

    /**
     * Find gift suggestions by relationship.
     *
     * @param relationship the target relationship
     * @return list of gift suggestions for the specified relationship
     */
    List<GiftSuggestion> findByRelationship(Relationship relationship);

    /**
     * Find gift suggestions by personality type.
     *
     * @param personalityType the target personality type
     * @return list of gift suggestions for the specified personality type
     */
    List<GiftSuggestion> findByPersonalityType(PersonalityType personalityType);

    /**
     * Find gift suggestions by age group and gender combination.
     *
     * @param ageGroup the target age group
     * @param gender the target gender
     * @return list of gift suggestions matching both criteria
     */
    List<GiftSuggestion> findByAgeGroupAndGender(AgeGroup ageGroup, Gender gender);

    /**
     * Find gift suggestions by age group and interest combination.
     *
     * @param ageGroup the target age group
     * @param interest the target interest
     * @return list of gift suggestions matching both criteria
     */
    List<GiftSuggestion> findByAgeGroupAndInterest(AgeGroup ageGroup, Interest interest);

    /**
     * Find gift suggestions by gender and interest combination.
     *
     * @param gender the target gender
     * @param interest the target interest
     * @return list of gift suggestions matching both criteria
     */
    List<GiftSuggestion> findByGenderAndInterest(Gender gender, Interest interest);

    /**
     * Find gift suggestions by occasion and relationship combination.
     *
     * @param occasion the target occasion
     * @param relationship the target relationship
     * @return list of gift suggestions matching both criteria
     */
    List<GiftSuggestion> findByOccasionAndRelationship(Occasion occasion, Relationship relationship);

    /**
     * Find gift suggestions by age group, gender, and interest combination.
     *
     * @param ageGroup the target age group
     * @param gender the target gender
     * @param interest the target interest
     * @return list of gift suggestions matching all three criteria
     */
    List<GiftSuggestion> findByAgeGroupAndGenderAndInterest(AgeGroup ageGroup, Gender gender, Interest interest);

    // Price range query methods

    /**
     * Find gift suggestions where the minimum price is less than or equal to the specified amount.
     *
     * @param maxBudget the maximum budget amount
     * @return list of gift suggestions within the budget range
     */
    List<GiftSuggestion> findByMinPriceLessThanEqual(BigDecimal maxBudget);

    /**
     * Find gift suggestions where the maximum price is greater than or equal to the specified amount.
     *
     * @param minBudget the minimum budget amount
     * @return list of gift suggestions above the minimum budget
     */
    List<GiftSuggestion> findByMaxPriceGreaterThanEqual(BigDecimal minBudget);

    /**
     * Find gift suggestions within a specific price range.
     *
     * @param minBudget the minimum budget
     * @param maxBudget the maximum budget
     * @return list of gift suggestions within the specified price range
     */
    @Query("SELECT gs FROM GiftSuggestion gs WHERE gs.minPrice <= :maxBudget AND gs.maxPrice >= :minBudget")
    List<GiftSuggestion> findGiftsWithinBudget(@Param("minBudget") BigDecimal minBudget, @Param("maxBudget") BigDecimal maxBudget);

    /**
     * Find gift suggestions that can be afforded with the given budget.
     *
     * @param budget the available budget
     * @return list of gift suggestions where the minimum price is within budget
     */
    @Query("SELECT gs FROM GiftSuggestion gs WHERE gs.minPrice <= :budget")
    List<GiftSuggestion> findAffordableGifts(@Param("budget") BigDecimal budget);

    // Advanced combination queries with pagination

    /**
     * Find gift suggestions by multiple criteria with pagination support.
     *
     * @param ageGroup the target age group
     * @param gender the target gender
     * @param interest the target interest
     * @param occasion the target occasion
     * @param pageable pagination information
     * @return page of gift suggestions matching all criteria
     */
    Page<GiftSuggestion> findByAgeGroupAndGenderAndInterestAndOccasion(
            AgeGroup ageGroup, Gender gender, Interest interest, Occasion occasion, Pageable pageable);

    /**
     * Advanced search for gift suggestions with multiple optional criteria and budget constraint.
     *
     * @param ageGroup the target age group (can be null)
     * @param gender the target gender (can be null)
     * @param interest the target interest (can be null)
     * @param occasion the target occasion (can be null)
     * @param relationship the target relationship (can be null)
     * @param personalityType the target personality type (can be null)
     * @param maxBudget the maximum budget (can be null for no budget limit)
     * @param pageable pagination information
     * @return page of gift suggestions matching the specified criteria
     */
    @Query("SELECT gs FROM GiftSuggestion gs WHERE " +
           "(:ageGroup IS NULL OR gs.ageGroup = :ageGroup) AND " +
           "(:gender IS NULL OR gs.gender = :gender) AND " +
           "(:interest IS NULL OR gs.interest = :interest) AND " +
           "(:occasion IS NULL OR gs.occasion = :occasion) AND " +
           "(:relationship IS NULL OR gs.relationship = :relationship) AND " +
           "(:personalityType IS NULL OR gs.personalityType = :personalityType) AND " +
           "(:maxBudget IS NULL OR gs.minPrice <= :maxBudget)")
    Page<GiftSuggestion> findByAdvancedCriteria(
            @Param("ageGroup") AgeGroup ageGroup,
            @Param("gender") Gender gender,
            @Param("interest") Interest interest,
            @Param("occasion") Occasion occasion,
            @Param("relationship") Relationship relationship,
            @Param("personalityType") PersonalityType personalityType,
            @Param("maxBudget") BigDecimal maxBudget,
            Pageable pageable);

    /**
     * Find gift suggestions by price range with pagination.
     *
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @param pageable pagination information
     * @return page of gift suggestions within the price range
     */
    @Query("SELECT gs FROM GiftSuggestion gs WHERE gs.maxPrice >= :minPrice AND gs.minPrice <= :maxPrice")
    Page<GiftSuggestion> findByPriceRangeWithPagination(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    /**
     * Count gift suggestions by advanced criteria.
     *
     * @param ageGroup the target age group (can be null)
     * @param gender the target gender (can be null)
     * @param interest the target interest (can be null)
     * @param occasion the target occasion (can be null)
     * @param relationship the target relationship (can be null)
     * @param personalityType the target personality type (can be null)
     * @param maxBudget the maximum budget (can be null for no budget limit)
     * @return count of gift suggestions matching the criteria
     */
    @Query("SELECT COUNT(gs) FROM GiftSuggestion gs WHERE " +
           "(:ageGroup IS NULL OR gs.ageGroup = :ageGroup) AND " +
           "(:gender IS NULL OR gs.gender = :gender) AND " +
           "(:interest IS NULL OR gs.interest = :interest) AND " +
           "(:occasion IS NULL OR gs.occasion = :occasion) AND " +
           "(:relationship IS NULL OR gs.relationship = :relationship) AND " +
           "(:personalityType IS NULL OR gs.personalityType = :personalityType) AND " +
           "(:maxBudget IS NULL OR gs.minPrice <= :maxBudget)")
    long countByAdvancedCriteria(
            @Param("ageGroup") AgeGroup ageGroup,
            @Param("gender") Gender gender,
            @Param("interest") Interest interest,
            @Param("occasion") Occasion occasion,
            @Param("relationship") Relationship relationship,
            @Param("personalityType") PersonalityType personalityType,
            @Param("maxBudget") BigDecimal maxBudget);
}