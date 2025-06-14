package com.giftservice.repository;

import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}