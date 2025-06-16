package com.giftservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giftservice.dto.request.CreateConcreteGiftRequest;
import com.giftservice.dto.request.UpdateConcreteGiftRequest;
import com.giftservice.entity.ConcreteGift;
import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import com.giftservice.exception.ResourceNotFoundException;
import com.giftservice.service.ConcreteGiftService;
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

@WebMvcTest(controllers = ConcreteGiftController.class,
            excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class,
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
            })
class ConcreteGiftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConcreteGiftService concreteGiftService;

    @MockBean
    private GiftSuggestionService giftSuggestionService;

    @Autowired
    private ObjectMapper objectMapper;

    private ConcreteGift testConcreteGift;
    private GiftSuggestion testGiftSuggestion;
    private UUID testConcreteGiftId;
    private UUID testGiftSuggestionId;

    @BeforeEach
    void setUp() {
        testConcreteGiftId = UUID.randomUUID();
        testGiftSuggestionId = UUID.randomUUID();
        
        testGiftSuggestion = new GiftSuggestion(
                "Test Gift Suggestion",
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
        testGiftSuggestion.setId(testGiftSuggestionId);

        testConcreteGift = new ConcreteGift(
                "Test Concrete Gift",
                "Test concrete description",
                new BigDecimal("25.99"),
                "Test Vendor",
                "http://example.com/product",
                "SKU123",
                true,
                testGiftSuggestion
        );
        testConcreteGift.setId(testConcreteGiftId);
        testConcreteGift.setCreatedDate(LocalDateTime.now());
        testConcreteGift.setLastModifiedDate(LocalDateTime.now());
    }

    @Test
    void getAllConcreteGifts_ShouldReturnPagedResults() throws Exception {
        List<ConcreteGift> concreteGifts = Arrays.asList(testConcreteGift);
        Page<ConcreteGift> page = new PageImpl<>(concreteGifts, PageRequest.of(0, 20), 1);
        
        when(concreteGiftService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/concrete-gifts")
                .param("page", "0")
                .param("size", "20")
                .param("sort", "name")
                .param("direction", "ASC"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testConcreteGiftId.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Test Concrete Gift"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(concreteGiftService).findAll(any(Pageable.class));
    }

    @Test
    void getConcreteGiftById_WhenExists_ShouldReturnConcreteGift() throws Exception {
        when(concreteGiftService.findById(testConcreteGiftId)).thenReturn(Optional.of(testConcreteGift));

        mockMvc.perform(get("/api/v1/concrete-gifts/{id}", testConcreteGiftId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testConcreteGiftId.toString()))
                .andExpect(jsonPath("$.name").value("Test Concrete Gift"))
                .andExpect(jsonPath("$.price").value(25.99))
                .andExpect(jsonPath("$.vendorName").value("Test Vendor"))
                .andExpect(jsonPath("$.available").value(true));

        verify(concreteGiftService).findById(testConcreteGiftId);
    }

    @Test
    void getConcreteGiftById_WhenNotExists_ShouldReturn404() throws Exception {
        when(concreteGiftService.findById(testConcreteGiftId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/concrete-gifts/{id}", testConcreteGiftId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));

        verify(concreteGiftService).findById(testConcreteGiftId);
    }

    @Test
    void createConcreteGift_WithValidData_ShouldReturnCreated() throws Exception {
        CreateConcreteGiftRequest request = new CreateConcreteGiftRequest(
                "New Concrete Gift",
                "New concrete description",
                new BigDecimal("35.00"),
                "New Vendor",
                "http://example.com/new-product",
                "SKU456",
                true,
                testGiftSuggestionId
        );

        when(giftSuggestionService.findById(testGiftSuggestionId)).thenReturn(Optional.of(testGiftSuggestion));
        when(concreteGiftService.create(any(ConcreteGift.class))).thenReturn(testConcreteGift);

        mockMvc.perform(post("/api/v1/concrete-gifts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testConcreteGiftId.toString()))
                .andExpect(jsonPath("$.name").value("Test Concrete Gift"));

        verify(giftSuggestionService).findById(testGiftSuggestionId);
        verify(concreteGiftService).create(any(ConcreteGift.class));
    }

    @Test
    void createConcreteGift_WithInvalidGiftSuggestionId_ShouldReturn404() throws Exception {
        CreateConcreteGiftRequest request = new CreateConcreteGiftRequest(
                "New Concrete Gift",
                "New concrete description",
                new BigDecimal("35.00"),
                "New Vendor",
                "http://example.com/new-product",
                "SKU456",
                true,
                testGiftSuggestionId
        );

        when(giftSuggestionService.findById(testGiftSuggestionId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/concrete-gifts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404));

        verify(giftSuggestionService).findById(testGiftSuggestionId);
        verify(concreteGiftService, never()).create(any());
    }

    @Test
    void updateConcreteGift_WhenExists_ShouldReturnUpdated() throws Exception {
        UpdateConcreteGiftRequest request = new UpdateConcreteGiftRequest(
                "Updated Concrete Gift",
                "Updated description",
                new BigDecimal("45.00"),
                "Updated Vendor",
                "http://example.com/updated-product",
                "SKU789",
                false,
                testGiftSuggestionId
        );

        ConcreteGift updatedConcreteGift = new ConcreteGift(
                "Updated Concrete Gift",
                "Updated description",
                new BigDecimal("45.00"),
                "Updated Vendor",
                "http://example.com/updated-product",
                "SKU789",
                false,
                testGiftSuggestion
        );
        updatedConcreteGift.setId(testConcreteGiftId);

        when(concreteGiftService.findById(testConcreteGiftId)).thenReturn(Optional.of(testConcreteGift));
        when(giftSuggestionService.findById(testGiftSuggestionId)).thenReturn(Optional.of(testGiftSuggestion));
        when(concreteGiftService.update(eq(testConcreteGiftId), any(ConcreteGift.class))).thenReturn(updatedConcreteGift);

        mockMvc.perform(put("/api/v1/concrete-gifts/{id}", testConcreteGiftId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testConcreteGiftId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Concrete Gift"));

        verify(concreteGiftService).findById(testConcreteGiftId);
        verify(giftSuggestionService).findById(testGiftSuggestionId);
        verify(concreteGiftService).update(eq(testConcreteGiftId), any(ConcreteGift.class));
    }

    @Test
    void deleteConcreteGift_WhenExists_ShouldReturnNoContent() throws Exception {
        when(concreteGiftService.existsById(testConcreteGiftId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/concrete-gifts/{id}", testConcreteGiftId))
                .andExpect(status().isNoContent());

        verify(concreteGiftService).existsById(testConcreteGiftId);
        verify(concreteGiftService).deleteById(testConcreteGiftId);
    }

    @Test
    void deleteConcreteGift_WhenNotExists_ShouldReturn404() throws Exception {
        when(concreteGiftService.existsById(testConcreteGiftId)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/concrete-gifts/{id}", testConcreteGiftId))
                .andExpect(status().isNotFound());

        verify(concreteGiftService).existsById(testConcreteGiftId);
        verify(concreteGiftService, never()).deleteById(any());
    }

    @Test
    void getConcreteGiftsByVendor_ShouldReturnFilteredResults() throws Exception {
        List<ConcreteGift> concreteGifts = Arrays.asList(testConcreteGift);
        when(concreteGiftService.findByVendorName("Test Vendor")).thenReturn(concreteGifts);

        mockMvc.perform(get("/api/v1/concrete-gifts/by-vendor/{vendorName}", "Test Vendor"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(testConcreteGiftId.toString()))
                .andExpect(jsonPath("$[0].vendorName").value("Test Vendor"));

        verify(concreteGiftService).findByVendorName("Test Vendor");
    }

    @Test
    void getConcreteGiftsByAvailability_ShouldReturnFilteredResults() throws Exception {
        List<ConcreteGift> concreteGifts = Arrays.asList(testConcreteGift);
        when(concreteGiftService.findByAvailable(true)).thenReturn(concreteGifts);

        mockMvc.perform(get("/api/v1/concrete-gifts/by-availability")
                .param("available", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(testConcreteGiftId.toString()))
                .andExpect(jsonPath("$[0].available").value(true));

        verify(concreteGiftService).findByAvailable(true);
    }

    @Test
    void getConcreteGiftsByPriceRange_ShouldReturnFilteredResults() throws Exception {
        List<ConcreteGift> concreteGifts = Arrays.asList(testConcreteGift);
        when(concreteGiftService.findByPriceRange(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(concreteGifts);

        mockMvc.perform(get("/api/v1/concrete-gifts/by-price-range")
                .param("minPrice", "20.00")
                .param("maxPrice", "30.00"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(testConcreteGiftId.toString()));

        verify(concreteGiftService).findByPriceRange(
                eq(new BigDecimal("20.00")), eq(new BigDecimal("30.00")));
    }

    @Test
    void searchConcreteGifts_WithCriteria_ShouldReturnResults() throws Exception {
        List<ConcreteGift> concreteGifts = Arrays.asList(testConcreteGift);
        Page<ConcreteGift> page = new PageImpl<>(concreteGifts, PageRequest.of(0, 20), 1);
        
        when(concreteGiftService.findByAdvancedCriteria(
                any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/concrete-gifts/search")
                .param("giftSuggestionId", testGiftSuggestionId.toString())
                .param("vendorName", "Test Vendor")
                .param("available", "true")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(testConcreteGiftId.toString()));

        verify(concreteGiftService).findByAdvancedCriteria(
                eq(testGiftSuggestionId), eq("Test Vendor"), eq(true), 
                any(), any(), any(Pageable.class));
    }
}