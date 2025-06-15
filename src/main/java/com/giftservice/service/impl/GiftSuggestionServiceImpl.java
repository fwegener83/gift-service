package com.giftservice.service.impl;

import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import com.giftservice.repository.GiftSuggestionRepository;
import com.giftservice.service.GiftSuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of GiftSuggestionService providing business logic for gift suggestion management.
 */
@Service
@Transactional
public class GiftSuggestionServiceImpl implements GiftSuggestionService {

    private static final Logger logger = LoggerFactory.getLogger(GiftSuggestionServiceImpl.class);

    private final GiftSuggestionRepository giftSuggestionRepository;

    @Autowired
    public GiftSuggestionServiceImpl(GiftSuggestionRepository giftSuggestionRepository) {
        this.giftSuggestionRepository = giftSuggestionRepository;
    }

    @Override
    public GiftSuggestion create(GiftSuggestion giftSuggestion) {
        if (giftSuggestion == null) {
            throw new IllegalArgumentException("Gift suggestion cannot be null");
        }
        
        logger.info("Creating new gift suggestion: {}", giftSuggestion.getName());
        
        if (giftSuggestion.getId() != null) {
            throw new IllegalArgumentException("Gift suggestion ID must be null for creation");
        }
        
        validateGiftSuggestion(giftSuggestion);
        
        GiftSuggestion savedGiftSuggestion = giftSuggestionRepository.save(giftSuggestion);
        logger.info("Successfully created gift suggestion with ID: {}", savedGiftSuggestion.getId());
        
        return savedGiftSuggestion;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GiftSuggestion> findById(UUID id) {
        logger.debug("Finding gift suggestion by ID: {}", id);
        
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        return giftSuggestionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GiftSuggestion> findAll(Pageable pageable) {
        logger.debug("Finding all gift suggestions with pagination: {}", pageable);
        
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        
        return giftSuggestionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftSuggestion> findAll() {
        logger.debug("Finding all gift suggestions");
        return giftSuggestionRepository.findAll();
    }

    @Override
    public GiftSuggestion update(UUID id, GiftSuggestion giftSuggestion) {
        logger.info("Updating gift suggestion with ID: {}", id);
        
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        if (giftSuggestion == null) {
            throw new IllegalArgumentException("Gift suggestion cannot be null");
        }
        
        GiftSuggestion existingGiftSuggestion = giftSuggestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gift suggestion not found with ID: " + id));
        
        validateGiftSuggestion(giftSuggestion);
        
        // Update fields
        existingGiftSuggestion.setName(giftSuggestion.getName());
        existingGiftSuggestion.setDescription(giftSuggestion.getDescription());
        existingGiftSuggestion.setMinPrice(giftSuggestion.getMinPrice());
        existingGiftSuggestion.setMaxPrice(giftSuggestion.getMaxPrice());
        existingGiftSuggestion.setAgeGroup(giftSuggestion.getAgeGroup());
        existingGiftSuggestion.setGender(giftSuggestion.getGender());
        existingGiftSuggestion.setInterest(giftSuggestion.getInterest());
        existingGiftSuggestion.setOccasion(giftSuggestion.getOccasion());
        existingGiftSuggestion.setRelationship(giftSuggestion.getRelationship());
        existingGiftSuggestion.setPersonalityType(giftSuggestion.getPersonalityType());
        
        GiftSuggestion updatedGiftSuggestion = giftSuggestionRepository.save(existingGiftSuggestion);
        logger.info("Successfully updated gift suggestion with ID: {}", id);
        
        return updatedGiftSuggestion;
    }

    @Override
    public void deleteById(UUID id) {
        logger.info("Deleting gift suggestion with ID: {}", id);
        
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        if (!giftSuggestionRepository.existsById(id)) {
            throw new RuntimeException("Gift suggestion not found with ID: " + id);
        }
        
        giftSuggestionRepository.deleteById(id);
        logger.info("Successfully deleted gift suggestion with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        logger.debug("Checking if gift suggestion exists with ID: {}", id);
        
        if (id == null) {
            return false;
        }
        
        return giftSuggestionRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftSuggestion> findByAgeGroup(AgeGroup ageGroup) {
        logger.debug("Finding gift suggestions by age group: {}", ageGroup);
        
        if (ageGroup == null) {
            throw new IllegalArgumentException("Age group cannot be null");
        }
        
        return giftSuggestionRepository.findByAgeGroup(ageGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftSuggestion> findByGender(Gender gender) {
        logger.debug("Finding gift suggestions by gender: {}", gender);
        
        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null");
        }
        
        return giftSuggestionRepository.findByGender(gender);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftSuggestion> findByInterest(Interest interest) {
        logger.debug("Finding gift suggestions by interest: {}", interest);
        
        if (interest == null) {
            throw new IllegalArgumentException("Interest cannot be null");
        }
        
        return giftSuggestionRepository.findByInterest(interest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftSuggestion> findByOccasion(Occasion occasion) {
        logger.debug("Finding gift suggestions by occasion: {}", occasion);
        
        if (occasion == null) {
            throw new IllegalArgumentException("Occasion cannot be null");
        }
        
        return giftSuggestionRepository.findByOccasion(occasion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftSuggestion> findByRelationship(Relationship relationship) {
        logger.debug("Finding gift suggestions by relationship: {}", relationship);
        
        if (relationship == null) {
            throw new IllegalArgumentException("Relationship cannot be null");
        }
        
        return giftSuggestionRepository.findByRelationship(relationship);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftSuggestion> findByPersonalityType(PersonalityType personalityType) {
        logger.debug("Finding gift suggestions by personality type: {}", personalityType);
        
        if (personalityType == null) {
            throw new IllegalArgumentException("Personality type cannot be null");
        }
        
        return giftSuggestionRepository.findByPersonalityType(personalityType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftSuggestion> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.debug("Finding gift suggestions by price range: {} - {}", minPrice, maxPrice);
        
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Price range bounds cannot be null");
        }
        
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        
        return giftSuggestionRepository.findGiftsWithinBudget(minPrice, maxPrice);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GiftSuggestion> findByAdvancedCriteria(
            AgeGroup ageGroup,
            Gender gender,
            Interest interest,
            Occasion occasion,
            Relationship relationship,
            PersonalityType personalityType,
            BigDecimal maxBudget,
            Pageable pageable) {
        
        logger.debug("Finding gift suggestions by advanced criteria");
        
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        
        return giftSuggestionRepository.findByAdvancedCriteria(
                ageGroup, gender, interest, occasion, relationship, personalityType, maxBudget, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByAdvancedCriteria(
            AgeGroup ageGroup,
            Gender gender,
            Interest interest,
            Occasion occasion,
            Relationship relationship,
            PersonalityType personalityType,
            BigDecimal maxBudget) {
        
        logger.debug("Counting gift suggestions by advanced criteria");
        
        return giftSuggestionRepository.countByAdvancedCriteria(
                ageGroup, gender, interest, occasion, relationship, personalityType, maxBudget);
    }

    /**
     * Validates a gift suggestion for business rules.
     *
     * @param giftSuggestion the gift suggestion to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateGiftSuggestion(GiftSuggestion giftSuggestion) {
        if (giftSuggestion.getName() == null || giftSuggestion.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Gift suggestion name cannot be null or empty");
        }
        
        if (giftSuggestion.getDescription() == null || giftSuggestion.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Gift suggestion description cannot be null or empty");
        }
        
        if (giftSuggestion.getMinPrice() == null) {
            throw new IllegalArgumentException("Minimum price cannot be null");
        }
        
        if (giftSuggestion.getMaxPrice() == null) {
            throw new IllegalArgumentException("Maximum price cannot be null");
        }
        
        if (giftSuggestion.getMinPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimum price cannot be negative");
        }
        
        if (giftSuggestion.getMaxPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Maximum price must be positive");
        }
        
        if (giftSuggestion.getMinPrice().compareTo(giftSuggestion.getMaxPrice()) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        
        if (giftSuggestion.getAgeGroup() == null) {
            throw new IllegalArgumentException("Age group cannot be null");
        }
        
        if (giftSuggestion.getGender() == null) {
            throw new IllegalArgumentException("Gender cannot be null");
        }
        
        if (giftSuggestion.getInterest() == null) {
            throw new IllegalArgumentException("Interest cannot be null");
        }
        
        if (giftSuggestion.getOccasion() == null) {
            throw new IllegalArgumentException("Occasion cannot be null");
        }
        
        if (giftSuggestion.getRelationship() == null) {
            throw new IllegalArgumentException("Relationship cannot be null");
        }
        
        if (giftSuggestion.getPersonalityType() == null) {
            throw new IllegalArgumentException("Personality type cannot be null");
        }
    }
}