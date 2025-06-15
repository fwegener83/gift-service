package com.giftservice.service.impl;

import com.giftservice.entity.ConcreteGift;
import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import com.giftservice.repository.ConcreteGiftRepository;
import com.giftservice.repository.GiftSuggestionRepository;
import com.giftservice.service.ConcreteGiftService;
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
 * Implementation of ConcreteGiftService providing business logic for concrete gift management.
 */
@Service
@Transactional
public class ConcreteGiftServiceImpl implements ConcreteGiftService {

    private static final Logger logger = LoggerFactory.getLogger(ConcreteGiftServiceImpl.class);

    private final ConcreteGiftRepository concreteGiftRepository;
    private final GiftSuggestionRepository giftSuggestionRepository;

    @Autowired
    public ConcreteGiftServiceImpl(ConcreteGiftRepository concreteGiftRepository,
                                   GiftSuggestionRepository giftSuggestionRepository) {
        this.concreteGiftRepository = concreteGiftRepository;
        this.giftSuggestionRepository = giftSuggestionRepository;
    }

    @Override
    public ConcreteGift create(ConcreteGift concreteGift) {
        if (concreteGift == null) {
            throw new IllegalArgumentException("Concrete gift cannot be null");
        }
        
        logger.info("Creating new concrete gift: {}", concreteGift.getName());
        
        if (concreteGift.getId() != null) {
            throw new IllegalArgumentException("Concrete gift ID must be null for creation");
        }
        
        validateConcreteGift(concreteGift);
        
        // Validate gift suggestion exists if provided
        if (concreteGift.getGiftSuggestion() != null) {
            GiftSuggestion giftSuggestion = giftSuggestionRepository.findById(concreteGift.getGiftSuggestion().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Gift suggestion not found with ID: " 
                            + concreteGift.getGiftSuggestion().getId()));
            
            validateGiftSuggestionAssociation(concreteGift, giftSuggestion);
        }
        
        ConcreteGift savedConcreteGift = concreteGiftRepository.save(concreteGift);
        logger.info("Successfully created concrete gift with ID: {}", savedConcreteGift.getId());
        
        return savedConcreteGift;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConcreteGift> findById(UUID id) {
        logger.debug("Finding concrete gift by ID: {}", id);
        
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        return concreteGiftRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConcreteGift> findAll(Pageable pageable) {
        logger.debug("Finding all concrete gifts with pagination: {}", pageable);
        
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        
        return concreteGiftRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcreteGift> findAll() {
        logger.debug("Finding all concrete gifts");
        return concreteGiftRepository.findAll();
    }

    @Override
    public ConcreteGift update(UUID id, ConcreteGift concreteGift) {
        logger.info("Updating concrete gift with ID: {}", id);
        
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        if (concreteGift == null) {
            throw new IllegalArgumentException("Concrete gift cannot be null");
        }
        
        ConcreteGift existingConcreteGift = concreteGiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concrete gift not found with ID: " + id));
        
        validateConcreteGift(concreteGift);
        
        // Validate gift suggestion exists if provided
        if (concreteGift.getGiftSuggestion() != null) {
            GiftSuggestion giftSuggestion = giftSuggestionRepository.findById(concreteGift.getGiftSuggestion().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Gift suggestion not found with ID: " 
                            + concreteGift.getGiftSuggestion().getId()));
            
            validateGiftSuggestionAssociation(concreteGift, giftSuggestion);
        }
        
        // Update fields
        existingConcreteGift.setName(concreteGift.getName());
        existingConcreteGift.setDescription(concreteGift.getDescription());
        existingConcreteGift.setExactPrice(concreteGift.getExactPrice());
        existingConcreteGift.setVendorName(concreteGift.getVendorName());
        existingConcreteGift.setProductUrl(concreteGift.getProductUrl());
        existingConcreteGift.setProductSku(concreteGift.getProductSku());
        existingConcreteGift.setAvailable(concreteGift.getAvailable());
        existingConcreteGift.setGiftSuggestion(concreteGift.getGiftSuggestion());
        
        ConcreteGift updatedConcreteGift = concreteGiftRepository.save(existingConcreteGift);
        logger.info("Successfully updated concrete gift with ID: {}", id);
        
        return updatedConcreteGift;
    }

    @Override
    public void deleteById(UUID id) {
        logger.info("Deleting concrete gift with ID: {}", id);
        
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        if (!concreteGiftRepository.existsById(id)) {
            throw new RuntimeException("Concrete gift not found with ID: " + id);
        }
        
        concreteGiftRepository.deleteById(id);
        logger.info("Successfully deleted concrete gift with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        logger.debug("Checking if concrete gift exists with ID: {}", id);
        
        if (id == null) {
            return false;
        }
        
        return concreteGiftRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcreteGift> findByGiftSuggestionId(UUID giftSuggestionId) {
        logger.debug("Finding concrete gifts by gift suggestion ID: {}", giftSuggestionId);
        
        if (giftSuggestionId == null) {
            throw new IllegalArgumentException("Gift suggestion ID cannot be null");
        }
        
        return concreteGiftRepository.findByGiftSuggestionId(giftSuggestionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConcreteGift> findByGiftSuggestionId(UUID giftSuggestionId, Pageable pageable) {
        logger.debug("Finding concrete gifts by gift suggestion ID with pagination: {}", giftSuggestionId);
        
        if (giftSuggestionId == null) {
            throw new IllegalArgumentException("Gift suggestion ID cannot be null");
        }
        
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        
        // Use the existing method with GiftSuggestion entity
        GiftSuggestion giftSuggestion = giftSuggestionRepository.findById(giftSuggestionId)
                .orElseThrow(() -> new IllegalArgumentException("Gift suggestion not found with ID: " + giftSuggestionId));
        
        return concreteGiftRepository.findByGiftSuggestion(giftSuggestion, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcreteGift> findByVendorName(String vendorName) {
        logger.debug("Finding concrete gifts by vendor name: {}", vendorName);
        
        if (vendorName == null || vendorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Vendor name cannot be null or empty");
        }
        
        return concreteGiftRepository.findByVendorName(vendorName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcreteGift> findByAvailable(boolean available) {
        logger.debug("Finding concrete gifts by availability: {}", available);
        return concreteGiftRepository.findByAvailable(available);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcreteGift> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.debug("Finding concrete gifts by price range: {} - {}", minPrice, maxPrice);
        
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Price range bounds cannot be null");
        }
        
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        
        return concreteGiftRepository.findByPriceRange(minPrice, maxPrice);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConcreteGift> findByAdvancedCriteria(
            UUID giftSuggestionId,
            String vendorName,
            Boolean available,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {
        
        logger.debug("Finding concrete gifts by advanced criteria");
        
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        
        return concreteGiftRepository.findByAdvancedCriteria(
                vendorName, available, minPrice, maxPrice, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConcreteGift> findBySuggestionAndCriteria(
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
            Pageable pageable) {
        
        logger.debug("Finding concrete gifts by suggestion and criteria");
        
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        
        return concreteGiftRepository.findBySuggestionAndCriteria(
                null, available, minPrice, maxPrice, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByGiftSuggestionId(UUID giftSuggestionId) {
        logger.debug("Counting concrete gifts by gift suggestion ID: {}", giftSuggestionId);
        
        if (giftSuggestionId == null) {
            throw new IllegalArgumentException("Gift suggestion ID cannot be null");
        }
        
        return concreteGiftRepository.findByGiftSuggestionId(giftSuggestionId).size();
    }

    @Override
    public void validateGiftSuggestionAssociation(ConcreteGift concreteGift, GiftSuggestion giftSuggestion) {
        logger.debug("Validating gift suggestion association for concrete gift: {}", concreteGift.getName());
        
        if (concreteGift == null) {
            throw new IllegalArgumentException("Concrete gift cannot be null");
        }
        
        if (giftSuggestion == null) {
            throw new IllegalArgumentException("Gift suggestion cannot be null");
        }
        
        // Validate price is within suggestion range
        if (concreteGift.getExactPrice() != null) {
            if (concreteGift.getExactPrice().compareTo(giftSuggestion.getMinPrice()) < 0) {
                throw new IllegalArgumentException(
                        String.format("Concrete gift price (%s) is below minimum price (%s) for gift suggestion",
                                concreteGift.getExactPrice(), giftSuggestion.getMinPrice()));
            }
            
            if (concreteGift.getExactPrice().compareTo(giftSuggestion.getMaxPrice()) > 0) {
                throw new IllegalArgumentException(
                        String.format("Concrete gift price (%s) is above maximum price (%s) for gift suggestion",
                                concreteGift.getExactPrice(), giftSuggestion.getMaxPrice()));
            }
        }
        
        logger.debug("Gift suggestion association validation passed");
    }

    /**
     * Validates a concrete gift for business rules.
     *
     * @param concreteGift the concrete gift to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateConcreteGift(ConcreteGift concreteGift) {
        if (concreteGift.getName() == null || concreteGift.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Concrete gift name cannot be null or empty");
        }
        
        if (concreteGift.getDescription() == null || concreteGift.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Concrete gift description cannot be null or empty");
        }
        
        if (concreteGift.getExactPrice() == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        
        if (concreteGift.getExactPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        
        if (concreteGift.getVendorName() == null || concreteGift.getVendorName().trim().isEmpty()) {
            throw new IllegalArgumentException("Vendor name cannot be null or empty");
        }
        
        // Validate URL format if provided
        if (concreteGift.getProductUrl() != null && !concreteGift.getProductUrl().trim().isEmpty()) {
            String url = concreteGift.getProductUrl().trim();
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                throw new IllegalArgumentException("Product URL must start with http:// or https://");
            }
        }
    }
}