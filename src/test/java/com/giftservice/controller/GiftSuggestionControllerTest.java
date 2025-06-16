package com.giftservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giftservice.dto.request.CreateGiftSuggestionRequest;
import com.giftservice.dto.request.UpdateGiftSuggestionRequest;
import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import com.giftservice.exception.ResourceNotFoundException;
import com.giftservice.service.GiftSuggestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GiftSuggestionController.class, 
            excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class,
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
            })
class GiftSuggestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GiftSuggestionService giftSuggestionService;

    @Autowired
    private ObjectMapper objectMapper;

    private GiftSuggestion testGiftSuggestion;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testGiftSuggestion = new GiftSuggestion(
                "Test Gift",
                "Test Description",
                new BigDecimal("10.00"),
                new BigDecimal("50.00"),
                AgeGroup.ADULT,
                Gender.UNISEX,
                Interest.TECHNOLOGY,
                Occasion.BIRTHDAY,
                Relationship.FRIEND,
                PersonalityType.CREATIVE
        );
        testGiftSuggestion.setId(testId);
        testGiftSuggestion.setCreatedDate(LocalDateTime.now());
        testGiftSuggestion.setLastModifiedDate(LocalDateTime.now());
    }

    @Test
    void getAllGiftSuggestions_ShouldReturnPagedResults() throws Exception {
        // Given
        List<GiftSuggestion> giftSuggestions = Arrays.asList(testGiftSuggestion);
        Page<GiftSuggestion> page = new PageImpl<>(giftSuggestions, PageRequest.of(0, 20), 1);
        
        when(giftSuggestionService.findAll(any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/v1/gift-suggestions")
                .param("page", "0")
                .param("size", "20")
                .param("sort", "name")
                .param("direction", "ASC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testId.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Test Gift"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(giftSuggestionService).findAll(any(Pageable.class));
    }

    @Test
    void getGiftSuggestionById_WhenExists_ShouldReturnGiftSuggestion() throws Exception {
        // Given
        when(giftSuggestionService.findById(testId)).thenReturn(Optional.of(testGiftSuggestion));

        // When & Then
        mockMvc.perform(get("/api/v1/gift-suggestions/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value("Test Gift"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.minPrice").value(10.00))
                .andExpect(jsonPath("$.maxPrice").value(50.00))
                .andExpect(jsonPath("$.ageGroup").value("ADULT"))
                .andExpect(jsonPath("$.gender").value("UNISEX"));

        verify(giftSuggestionService).findById(testId);
    }

    @Test
    void getGiftSuggestionById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        when(giftSuggestionService.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/gift-suggestions/{id}", testId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));

        verify(giftSuggestionService).findById(testId);
    }

    @Test
    void createGiftSuggestion_WithValidData_ShouldReturnCreated() throws Exception {
        // Given
        CreateGiftSuggestionRequest request = new CreateGiftSuggestionRequest(
                "New Gift",
                "New Description", 
                new BigDecimal("20.00"),
                new BigDecimal("100.00"),
                AgeGroup.TEEN,
                Gender.MALE,
                Interest.SPORTS,
                Occasion.CHRISTMAS,
                Relationship.FAMILY,
                PersonalityType.ADVENTUROUS
        );

        when(giftSuggestionService.create(any(GiftSuggestion.class))).thenReturn(testGiftSuggestion);

        // When & Then
        mockMvc.perform(post("/api/v1/gift-suggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value("Test Gift"));

        verify(giftSuggestionService).create(any(GiftSuggestion.class));
    }

    @Test
    void createGiftSuggestion_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        CreateGiftSuggestionRequest request = new CreateGiftSuggestionRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/v1/gift-suggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors").isArray());

        verify(giftSuggestionService, never()).create(any());
    }

    @Test
    void updateGiftSuggestion_WhenExists_ShouldReturnUpdated() throws Exception {
        // Given
        UpdateGiftSuggestionRequest request = new UpdateGiftSuggestionRequest(
                "Updated Gift",
                "Updated Description",
                new BigDecimal("25.00"),
                new BigDecimal("75.00"),
                AgeGroup.CHILD,
                Gender.FEMALE,
                Interest.ART,
                Occasion.EASTER,
                Relationship.EXTENDED_FAMILY,
                PersonalityType.INTELLECTUAL
        );

        GiftSuggestion updatedGiftSuggestion = new GiftSuggestion(
                "Updated Gift",
                "Updated Description",
                new BigDecimal("25.00"),
                new BigDecimal("75.00"),
                AgeGroup.CHILD,
                Gender.FEMALE,
                Interest.ART,
                Occasion.EASTER,
                Relationship.EXTENDED_FAMILY,
                PersonalityType.INTELLECTUAL
        );
        updatedGiftSuggestion.setId(testId);

        when(giftSuggestionService.findById(testId)).thenReturn(Optional.of(testGiftSuggestion));
        when(giftSuggestionService.update(eq(testId), any(GiftSuggestion.class))).thenReturn(updatedGiftSuggestion);

        // When & Then
        mockMvc.perform(put("/api/v1/gift-suggestions/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Gift"));

        verify(giftSuggestionService).findById(testId);
        verify(giftSuggestionService).update(eq(testId), any(GiftSuggestion.class));
    }

    @Test
    void updateGiftSuggestion_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        UpdateGiftSuggestionRequest request = new UpdateGiftSuggestionRequest(
                "Updated Gift",
                "Updated Description",
                new BigDecimal("25.00"),
                new BigDecimal("75.00"),
                AgeGroup.CHILD,
                Gender.FEMALE,
                Interest.ART,
                Occasion.EASTER,
                Relationship.EXTENDED_FAMILY,
                PersonalityType.INTELLECTUAL
        );

        when(giftSuggestionService.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/v1/gift-suggestions/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(giftSuggestionService).findById(testId);
        verify(giftSuggestionService, never()).update(any(), any());
    }

    @Test
    void deleteGiftSuggestion_WhenExists_ShouldReturnNoContent() throws Exception {
        // Given
        when(giftSuggestionService.existsById(testId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/v1/gift-suggestions/{id}", testId))
                .andExpect(status().isNoContent());

        verify(giftSuggestionService).existsById(testId);
        verify(giftSuggestionService).deleteById(testId);
    }

    @Test
    void deleteGiftSuggestion_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        when(giftSuggestionService.existsById(testId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/v1/gift-suggestions/{id}", testId))
                .andExpect(status().isNotFound());

        verify(giftSuggestionService).existsById(testId);
        verify(giftSuggestionService, never()).deleteById(any());
    }

    @Test
    void searchGiftSuggestions_WithCriteria_ShouldReturnResults() throws Exception {
        // Given
        List<GiftSuggestion> giftSuggestions = Arrays.asList(testGiftSuggestion);
        Page<GiftSuggestion> page = new PageImpl<>(giftSuggestions, PageRequest.of(0, 20), 1);
        
        when(giftSuggestionService.findByAdvancedCriteria(
                any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/v1/gift-suggestions/search")
                .param("ageGroup", "ADULT")
                .param("gender", "UNISEX")
                .param("interest", "TECHNOLOGY")
                .param("maxBudget", "100.00")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testId.toString()));

        verify(giftSuggestionService).findByAdvancedCriteria(
                eq(AgeGroup.ADULT), eq(Gender.UNISEX), eq(Interest.TECHNOLOGY), 
                any(), any(), any(), eq(new BigDecimal("100.00")), any(Pageable.class));
    }

    @Test
    void getGiftSuggestionsByAgeGroup_ShouldReturnFilteredResults() throws Exception {
        // Given
        List<GiftSuggestion> giftSuggestions = Arrays.asList(testGiftSuggestion);
        when(giftSuggestionService.findByAgeGroup(AgeGroup.ADULT)).thenReturn(giftSuggestions);

        // When & Then
        mockMvc.perform(get("/api/v1/gift-suggestions/by-age-group/{ageGroup}", "ADULT"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(testId.toString()))
                .andExpect(jsonPath("$[0].ageGroup").value("ADULT"));

        verify(giftSuggestionService).findByAgeGroup(AgeGroup.ADULT);
    }

    @Test
    void getGiftSuggestionsByPriceRange_ShouldReturnFilteredResults() throws Exception {
        // Given
        List<GiftSuggestion> giftSuggestions = Arrays.asList(testGiftSuggestion);
        when(giftSuggestionService.findByPriceRange(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(giftSuggestions);

        // When & Then
        mockMvc.perform(get("/api/v1/gift-suggestions/by-price-range")
                .param("minPrice", "5.00")
                .param("maxPrice", "100.00"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(testId.toString()));

        verify(giftSuggestionService).findByPriceRange(
                eq(new BigDecimal("5.00")), eq(new BigDecimal("100.00")));
    }
}