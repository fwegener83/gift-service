package com.giftservice.dto.mapper;

import com.giftservice.dto.request.CreateGiftSuggestionRequest;
import com.giftservice.dto.request.UpdateGiftSuggestionRequest;
import com.giftservice.dto.response.GiftSuggestionResponse;
import com.giftservice.entity.GiftSuggestion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper utility for converting between GiftSuggestion entities and DTOs.
 */
public class GiftSuggestionMapper {

    private GiftSuggestionMapper() {
        // Utility class
    }

    /**
     * Convert CreateGiftSuggestionRequest to GiftSuggestion entity.
     */
    public static GiftSuggestion toEntity(CreateGiftSuggestionRequest request) {
        if (request == null) {
            return null;
        }
        
        return new GiftSuggestion(
                request.getName(),
                request.getDescription(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getAgeGroup(),
                request.getGender(),
                request.getInterest(),
                request.getOccasion(),
                request.getRelationship(),
                request.getPersonalityType()
        );
    }

    /**
     * Update GiftSuggestion entity from UpdateGiftSuggestionRequest.
     */
    public static void updateEntity(GiftSuggestion entity, UpdateGiftSuggestionRequest request) {
        if (entity == null || request == null) {
            return;
        }
        
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setMinPrice(request.getMinPrice());
        entity.setMaxPrice(request.getMaxPrice());
        entity.setAgeGroup(request.getAgeGroup());
        entity.setGender(request.getGender());
        entity.setInterest(request.getInterest());
        entity.setOccasion(request.getOccasion());
        entity.setRelationship(request.getRelationship());
        entity.setPersonalityType(request.getPersonalityType());
    }

    /**
     * Convert GiftSuggestion entity to GiftSuggestionResponse.
     */
    public static GiftSuggestionResponse toResponse(GiftSuggestion entity) {
        if (entity == null) {
            return null;
        }
        
        return new GiftSuggestionResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getMinPrice(),
                entity.getMaxPrice(),
                entity.getAgeGroup(),
                entity.getGender(),
                entity.getInterest(),
                entity.getOccasion(),
                entity.getRelationship(),
                entity.getPersonalityType(),
                entity.getCreatedDate(),
                entity.getLastModifiedDate(),
                entity.getConcreteGifts() != null 
                    ? entity.getConcreteGifts().stream()
                        .map(ConcreteGiftMapper::toResponse)
                        .collect(Collectors.toList())
                    : null
        );
    }

    /**
     * Convert GiftSuggestion entity to GiftSuggestionResponse without nested concrete gifts.
     */
    public static GiftSuggestionResponse toResponseWithoutConcreteGifts(GiftSuggestion entity) {
        if (entity == null) {
            return null;
        }
        
        return new GiftSuggestionResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getMinPrice(),
                entity.getMaxPrice(),
                entity.getAgeGroup(),
                entity.getGender(),
                entity.getInterest(),
                entity.getOccasion(),
                entity.getRelationship(),
                entity.getPersonalityType(),
                entity.getCreatedDate(),
                entity.getLastModifiedDate(),
                null // Don't include concrete gifts in list views
        );
    }

    /**
     * Convert list of GiftSuggestion entities to GiftSuggestionResponse list.
     */
    public static List<GiftSuggestionResponse> toResponseList(List<GiftSuggestion> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(GiftSuggestionMapper::toResponseWithoutConcreteGifts)
                .collect(Collectors.toList());
    }
}