package com.giftservice.dto.mapper;

import com.giftservice.dto.request.CreateConcreteGiftRequest;
import com.giftservice.dto.request.UpdateConcreteGiftRequest;
import com.giftservice.dto.response.ConcreteGiftResponse;
import com.giftservice.entity.ConcreteGift;
import com.giftservice.entity.GiftSuggestion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper utility for converting between ConcreteGift entities and DTOs.
 */
public class ConcreteGiftMapper {

    private ConcreteGiftMapper() {
        // Utility class
    }

    /**
     * Convert CreateConcreteGiftRequest to ConcreteGift entity.
     */
    public static ConcreteGift toEntity(CreateConcreteGiftRequest request, GiftSuggestion giftSuggestion) {
        if (request == null) {
            return null;
        }
        
        ConcreteGift entity = new ConcreteGift(
                request.getName(),
                request.getDescription(),
                request.getExactPrice(),
                request.getVendorName(),
                giftSuggestion
        );
        
        entity.setProductUrl(request.getProductUrl());
        entity.setProductSku(request.getProductSku());
        entity.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        
        return entity;
    }

    /**
     * Update ConcreteGift entity from UpdateConcreteGiftRequest.
     */
    public static void updateEntity(ConcreteGift entity, UpdateConcreteGiftRequest request, GiftSuggestion giftSuggestion) {
        if (entity == null || request == null) {
            return;
        }
        
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setExactPrice(request.getExactPrice());
        entity.setVendorName(request.getVendorName());
        entity.setProductUrl(request.getProductUrl());
        entity.setProductSku(request.getProductSku());
        entity.setAvailable(request.getAvailable());
        entity.setGiftSuggestion(giftSuggestion);
    }

    /**
     * Convert ConcreteGift entity to ConcreteGiftResponse.
     */
    public static ConcreteGiftResponse toResponse(ConcreteGift entity) {
        if (entity == null) {
            return null;
        }
        
        return new ConcreteGiftResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getExactPrice(),
                entity.getVendorName(),
                entity.getProductUrl(),
                entity.getProductSku(),
                entity.getAvailable(),
                entity.getCreatedDate(),
                entity.getLastModifiedDate(),
                entity.getGiftSuggestion() != null ? entity.getGiftSuggestion().getId() : null,
                entity.getGiftSuggestion() != null ? entity.getGiftSuggestion().getName() : null
        );
    }

    /**
     * Convert list of ConcreteGift entities to ConcreteGiftResponse list.
     */
    public static List<ConcreteGiftResponse> toResponseList(List<ConcreteGift> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(ConcreteGiftMapper::toResponse)
                .collect(Collectors.toList());
    }
}