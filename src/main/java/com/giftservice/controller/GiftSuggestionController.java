package com.giftservice.controller;

import com.giftservice.dto.mapper.GiftSuggestionMapper;
import com.giftservice.dto.request.CreateGiftSuggestionRequest;
import com.giftservice.dto.request.UpdateGiftSuggestionRequest;
import com.giftservice.dto.response.GiftSuggestionResponse;
import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import com.giftservice.exception.ResourceNotFoundException;
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
 * REST controller for gift suggestion operations.
 */
@RestController
@RequestMapping("/api/v1/gift-suggestions")
@Tag(name = "Gift Suggestions", description = "Operations for managing gift suggestions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GiftSuggestionController {

    private static final Logger logger = LoggerFactory.getLogger(GiftSuggestionController.class);

    private final GiftSuggestionService giftSuggestionService;

    @Autowired
    public GiftSuggestionController(GiftSuggestionService giftSuggestionService) {
        this.giftSuggestionService = giftSuggestionService;
    }

    @Operation(summary = "Get all gift suggestions", 
               description = "Retrieve a paginated list of all gift suggestions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved gift suggestions",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<Page<GiftSuggestionResponse>> getAllGiftSuggestions(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction", example = "ASC")
            @RequestParam(defaultValue = "ASC") String direction) {

        logger.debug("Getting all gift suggestions - page: {}, size: {}, sort: {}, direction: {}", 
                    page, size, sort, direction);

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<GiftSuggestion> giftSuggestions = giftSuggestionService.findAll(pageable);
        Page<GiftSuggestionResponse> response = giftSuggestions.map(GiftSuggestionMapper::toResponseWithoutConcreteGifts);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get gift suggestion by ID", 
               description = "Retrieve a specific gift suggestion by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved gift suggestion",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = GiftSuggestionResponse.class))),
        @ApiResponse(responseCode = "404", description = "Gift suggestion not found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<GiftSuggestionResponse> getGiftSuggestionById(
            @Parameter(description = "Gift suggestion ID", required = true)
            @PathVariable UUID id) {

        logger.debug("Getting gift suggestion by ID: {}", id);

        GiftSuggestion giftSuggestion = giftSuggestionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gift suggestion", id));

        GiftSuggestionResponse response = GiftSuggestionMapper.toResponse(giftSuggestion);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create new gift suggestion", 
               description = "Create a new gift suggestion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Gift suggestion created successfully",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = GiftSuggestionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<GiftSuggestionResponse> createGiftSuggestion(
            @Valid @RequestBody CreateGiftSuggestionRequest request) {

        logger.debug("Creating new gift suggestion: {}", request.getName());

        GiftSuggestion giftSuggestion = GiftSuggestionMapper.toEntity(request);
        GiftSuggestion savedGiftSuggestion = giftSuggestionService.create(giftSuggestion);

        GiftSuggestionResponse response = GiftSuggestionMapper.toResponse(savedGiftSuggestion);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update gift suggestion", 
               description = "Update an existing gift suggestion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Gift suggestion updated successfully",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = GiftSuggestionResponse.class))),
        @ApiResponse(responseCode = "404", description = "Gift suggestion not found",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<GiftSuggestionResponse> updateGiftSuggestion(
            @Parameter(description = "Gift suggestion ID", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGiftSuggestionRequest request) {

        logger.debug("Updating gift suggestion with ID: {}", id);

        GiftSuggestion existingGiftSuggestion = giftSuggestionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gift suggestion", id));

        GiftSuggestionMapper.updateEntity(existingGiftSuggestion, request);
        GiftSuggestion updatedGiftSuggestion = giftSuggestionService.update(id, existingGiftSuggestion);

        GiftSuggestionResponse response = GiftSuggestionMapper.toResponse(updatedGiftSuggestion);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete gift suggestion", 
               description = "Delete a gift suggestion by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Gift suggestion deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Gift suggestion not found",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiftSuggestion(
            @Parameter(description = "Gift suggestion ID", required = true)
            @PathVariable UUID id) {

        logger.debug("Deleting gift suggestion with ID: {}", id);

        if (!giftSuggestionService.existsById(id)) {
            throw new ResourceNotFoundException("Gift suggestion", id);
        }

        giftSuggestionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search gift suggestions", 
               description = "Search gift suggestions using advanced criteria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/search")
    public ResponseEntity<Page<GiftSuggestionResponse>> searchGiftSuggestions(
            @Parameter(description = "Age group filter")
            @RequestParam(required = false) AgeGroup ageGroup,
            @Parameter(description = "Gender filter")
            @RequestParam(required = false) Gender gender,
            @Parameter(description = "Interest filter")
            @RequestParam(required = false) Interest interest,
            @Parameter(description = "Occasion filter")
            @RequestParam(required = false) Occasion occasion,
            @Parameter(description = "Relationship filter")
            @RequestParam(required = false) Relationship relationship,
            @Parameter(description = "Personality type filter")
            @RequestParam(required = false) PersonalityType personalityType,
            @Parameter(description = "Maximum budget")
            @RequestParam(required = false) BigDecimal maxBudget,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction", example = "ASC")
            @RequestParam(defaultValue = "ASC") String direction) {

        logger.debug("Searching gift suggestions with criteria - ageGroup: {}, gender: {}, interest: {}, " +
                    "occasion: {}, relationship: {}, personalityType: {}, maxBudget: {}",
                    ageGroup, gender, interest, occasion, relationship, personalityType, maxBudget);

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<GiftSuggestion> giftSuggestions = giftSuggestionService.findByAdvancedCriteria(
                ageGroup, gender, interest, occasion, relationship, personalityType, maxBudget, pageable);

        Page<GiftSuggestionResponse> response = giftSuggestions.map(GiftSuggestionMapper::toResponseWithoutConcreteGifts);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get gift suggestions by age group", 
               description = "Retrieve gift suggestions filtered by age group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved gift suggestions",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid age group",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/by-age-group/{ageGroup}")
    public ResponseEntity<List<GiftSuggestionResponse>> getGiftSuggestionsByAgeGroup(
            @Parameter(description = "Age group", required = true)
            @PathVariable AgeGroup ageGroup) {

        logger.debug("Getting gift suggestions by age group: {}", ageGroup);

        List<GiftSuggestion> giftSuggestions = giftSuggestionService.findByAgeGroup(ageGroup);
        List<GiftSuggestionResponse> response = GiftSuggestionMapper.toResponseList(giftSuggestions);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get gift suggestions by interest", 
               description = "Retrieve gift suggestions filtered by interest")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved gift suggestions",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid interest",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/by-interest/{interest}")
    public ResponseEntity<List<GiftSuggestionResponse>> getGiftSuggestionsByInterest(
            @Parameter(description = "Interest", required = true)
            @PathVariable Interest interest) {

        logger.debug("Getting gift suggestions by interest: {}", interest);

        List<GiftSuggestion> giftSuggestions = giftSuggestionService.findByInterest(interest);
        List<GiftSuggestionResponse> response = GiftSuggestionMapper.toResponseList(giftSuggestions);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get gift suggestions by price range", 
               description = "Retrieve gift suggestions within a specific price range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved gift suggestions",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid price range",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/by-price-range")
    public ResponseEntity<List<GiftSuggestionResponse>> getGiftSuggestionsByPriceRange(
            @Parameter(description = "Minimum price", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price", required = true)
            @RequestParam BigDecimal maxPrice) {

        logger.debug("Getting gift suggestions by price range: {} - {}", minPrice, maxPrice);

        List<GiftSuggestion> giftSuggestions = giftSuggestionService.findByPriceRange(minPrice, maxPrice);
        List<GiftSuggestionResponse> response = GiftSuggestionMapper.toResponseList(giftSuggestions);

        return ResponseEntity.ok(response);
    }
}