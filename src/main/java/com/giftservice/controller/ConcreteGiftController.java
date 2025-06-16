package com.giftservice.controller;

import com.giftservice.dto.mapper.ConcreteGiftMapper;
import com.giftservice.dto.request.CreateConcreteGiftRequest;
import com.giftservice.dto.request.UpdateConcreteGiftRequest;
import com.giftservice.dto.response.ConcreteGiftResponse;
import com.giftservice.entity.ConcreteGift;
import com.giftservice.entity.GiftSuggestion;
import com.giftservice.exception.ResourceNotFoundException;
import com.giftservice.service.ConcreteGiftService;
import com.giftservice.service.GiftSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for concrete gift operations.
 */
@RestController
@RequestMapping("/api/v1/concrete-gifts")
@Tag(name = "Concrete Gifts", description = "Operations for managing concrete gift implementations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ConcreteGiftController {

    private static final Logger logger = LoggerFactory.getLogger(ConcreteGiftController.class);

    private final ConcreteGiftService concreteGiftService;
    private final GiftSuggestionService giftSuggestionService;

    @Autowired
    public ConcreteGiftController(ConcreteGiftService concreteGiftService, 
                                GiftSuggestionService giftSuggestionService) {
        this.concreteGiftService = concreteGiftService;
        this.giftSuggestionService = giftSuggestionService;
    }

    @Operation(summary = "Get all concrete gifts", 
               description = "Retrieve a paginated list of all concrete gifts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved concrete gifts",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<Page<ConcreteGiftResponse>> getAllConcreteGifts(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction", example = "ASC")
            @RequestParam(defaultValue = "ASC") String direction) {

        logger.debug("Getting all concrete gifts - page: {}, size: {}, sort: {}, direction: {}", 
                    page, size, sort, direction);

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<ConcreteGift> concreteGifts = concreteGiftService.findAll(pageable);
        Page<ConcreteGiftResponse> response = concreteGifts.map(ConcreteGiftMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get concrete gift by ID", 
               description = "Retrieve a specific concrete gift by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved concrete gift",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ConcreteGiftResponse.class))),
        @ApiResponse(responseCode = "404", description = "Concrete gift not found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConcreteGiftResponse> getConcreteGiftById(
            @Parameter(description = "Concrete gift ID", required = true)
            @PathVariable UUID id) {

        logger.debug("Getting concrete gift by ID: {}", id);

        ConcreteGift concreteGift = concreteGiftService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concrete gift", id));

        ConcreteGiftResponse response = ConcreteGiftMapper.toResponse(concreteGift);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create new concrete gift", 
               description = "Create a new concrete gift associated with a gift suggestion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Concrete gift created successfully",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ConcreteGiftResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Gift suggestion not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<ConcreteGiftResponse> createConcreteGift(
            @Valid @RequestBody CreateConcreteGiftRequest request) {

        logger.debug("Creating new concrete gift: {} for gift suggestion: {}", 
                    request.getName(), request.getGiftSuggestionId());

        // Validate that the gift suggestion exists
        GiftSuggestion giftSuggestion = giftSuggestionService.findById(request.getGiftSuggestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Gift suggestion", request.getGiftSuggestionId()));

        ConcreteGift concreteGift = ConcreteGiftMapper.toEntity(request, giftSuggestion);
        ConcreteGift savedConcreteGift = concreteGiftService.create(concreteGift);

        ConcreteGiftResponse response = ConcreteGiftMapper.toResponse(savedConcreteGift);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update concrete gift", 
               description = "Update an existing concrete gift")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Concrete gift updated successfully",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ConcreteGiftResponse.class))),
        @ApiResponse(responseCode = "404", description = "Concrete gift or gift suggestion not found",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConcreteGiftResponse> updateConcreteGift(
            @Parameter(description = "Concrete gift ID", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdateConcreteGiftRequest request) {

        logger.debug("Updating concrete gift with ID: {}", id);

        ConcreteGift existingConcreteGift = concreteGiftService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concrete gift", id));

        // Validate that the gift suggestion exists
        GiftSuggestion giftSuggestion = giftSuggestionService.findById(request.getGiftSuggestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Gift suggestion", request.getGiftSuggestionId()));

        ConcreteGiftMapper.updateEntity(existingConcreteGift, request, giftSuggestion);
        ConcreteGift updatedConcreteGift = concreteGiftService.update(id, existingConcreteGift);

        ConcreteGiftResponse response = ConcreteGiftMapper.toResponse(updatedConcreteGift);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete concrete gift", 
               description = "Delete a concrete gift by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Concrete gift deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Concrete gift not found",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcreteGift(
            @Parameter(description = "Concrete gift ID", required = true)
            @PathVariable UUID id) {

        logger.debug("Deleting concrete gift with ID: {}", id);

        if (!concreteGiftService.existsById(id)) {
            throw new ResourceNotFoundException("Concrete gift", id);
        }

        concreteGiftService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get concrete gifts by vendor", 
               description = "Retrieve concrete gifts filtered by vendor name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved concrete gifts",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid vendor name",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/by-vendor/{vendorName}")
    public ResponseEntity<List<ConcreteGiftResponse>> getConcreteGiftsByVendor(
            @Parameter(description = "Vendor name", required = true)
            @PathVariable String vendorName) {

        logger.debug("Getting concrete gifts by vendor: {}", vendorName);

        List<ConcreteGift> concreteGifts = concreteGiftService.findByVendorName(vendorName);
        List<ConcreteGiftResponse> response = ConcreteGiftMapper.toResponseList(concreteGifts);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get concrete gifts by availability", 
               description = "Retrieve concrete gifts filtered by availability status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved concrete gifts",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/by-availability")
    public ResponseEntity<List<ConcreteGiftResponse>> getConcreteGiftsByAvailability(
            @Parameter(description = "Availability status", required = true)
            @RequestParam boolean available) {

        logger.debug("Getting concrete gifts by availability: {}", available);

        List<ConcreteGift> concreteGifts = concreteGiftService.findByAvailable(available);
        List<ConcreteGiftResponse> response = ConcreteGiftMapper.toResponseList(concreteGifts);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get concrete gifts by price range", 
               description = "Retrieve concrete gifts within a specific price range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved concrete gifts",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid price range",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/by-price-range")
    public ResponseEntity<List<ConcreteGiftResponse>> getConcreteGiftsByPriceRange(
            @Parameter(description = "Minimum price", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price", required = true)
            @RequestParam BigDecimal maxPrice) {

        logger.debug("Getting concrete gifts by price range: {} - {}", minPrice, maxPrice);

        List<ConcreteGift> concreteGifts = concreteGiftService.findByPriceRange(minPrice, maxPrice);
        List<ConcreteGiftResponse> response = ConcreteGiftMapper.toResponseList(concreteGifts);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search concrete gifts", 
               description = "Search concrete gifts using advanced criteria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ConcreteGiftResponse>> searchConcreteGifts(
            @Parameter(description = "Gift suggestion ID filter")
            @RequestParam(required = false) UUID giftSuggestionId,
            @Parameter(description = "Vendor name filter")
            @RequestParam(required = false) String vendorName,
            @Parameter(description = "Availability filter")
            @RequestParam(required = false) Boolean available,
            @Parameter(description = "Minimum price")
            @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price")
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction", example = "ASC")
            @RequestParam(defaultValue = "ASC") String direction) {

        logger.debug("Searching concrete gifts with criteria - giftSuggestionId: {}, vendorName: {}, " +
                    "available: {}, minPrice: {}, maxPrice: {}",
                    giftSuggestionId, vendorName, available, minPrice, maxPrice);

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<ConcreteGift> concreteGifts = concreteGiftService.findByAdvancedCriteria(
                giftSuggestionId, vendorName, available, minPrice, maxPrice, pageable);

        Page<ConcreteGiftResponse> response = concreteGifts.map(ConcreteGiftMapper::toResponse);
        return ResponseEntity.ok(response);
    }
}

/**
 * Nested controller for gift suggestion related concrete gifts.
 */
@RestController
@RequestMapping("/api/v1/gift-suggestions/{suggestionId}/concrete-gifts")
@Tag(name = "Gift Suggestion Concrete Gifts", description = "Operations for managing concrete gifts within a gift suggestion")
@CrossOrigin(origins = "*", maxAge = 3600)
class GiftSuggestionConcreteGiftsController {

    private static final Logger logger = LoggerFactory.getLogger(GiftSuggestionConcreteGiftsController.class);

    private final ConcreteGiftService concreteGiftService;
    private final GiftSuggestionService giftSuggestionService;

    @Autowired
    public GiftSuggestionConcreteGiftsController(ConcreteGiftService concreteGiftService,
                                               GiftSuggestionService giftSuggestionService) {
        this.concreteGiftService = concreteGiftService;
        this.giftSuggestionService = giftSuggestionService;
    }

    @Operation(summary = "Get concrete gifts for gift suggestion", 
               description = "Retrieve all concrete gifts associated with a specific gift suggestion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved concrete gifts",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Gift suggestion not found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<Page<ConcreteGiftResponse>> getConcreteGiftsForSuggestion(
            @Parameter(description = "Gift suggestion ID", required = true)
            @PathVariable UUID suggestionId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction", example = "ASC")
            @RequestParam(defaultValue = "ASC") String direction) {

        logger.debug("Getting concrete gifts for gift suggestion: {}", suggestionId);

        // Validate that the gift suggestion exists
        if (!giftSuggestionService.existsById(suggestionId)) {
            throw new ResourceNotFoundException("Gift suggestion", suggestionId);
        }

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<ConcreteGift> concreteGifts = concreteGiftService.findByGiftSuggestionId(suggestionId, pageable);
        Page<ConcreteGiftResponse> response = concreteGifts.map(ConcreteGiftMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Count concrete gifts for gift suggestion", 
               description = "Get the count of concrete gifts associated with a specific gift suggestion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count"),
        @ApiResponse(responseCode = "404", description = "Gift suggestion not found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/count")
    public ResponseEntity<Long> getConcreteGiftsCountForSuggestion(
            @Parameter(description = "Gift suggestion ID", required = true)
            @PathVariable UUID suggestionId) {

        logger.debug("Getting concrete gifts count for gift suggestion: {}", suggestionId);

        // Validate that the gift suggestion exists
        if (!giftSuggestionService.existsById(suggestionId)) {
            throw new ResourceNotFoundException("Gift suggestion", suggestionId);
        }

        long count = concreteGiftService.countByGiftSuggestionId(suggestionId);
        return ResponseEntity.ok(count);
    }
}