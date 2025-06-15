package com.giftservice.service;

import com.giftservice.entity.ConcreteGift;
import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import com.giftservice.repository.ConcreteGiftRepository;
import com.giftservice.repository.GiftSuggestionRepository;
import com.giftservice.service.impl.ConcreteGiftServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcreteGiftServiceTest {

    @Mock
    private ConcreteGiftRepository concreteGiftRepository;

    @Mock
    private GiftSuggestionRepository giftSuggestionRepository;

    @InjectMocks
    private ConcreteGiftServiceImpl concreteGiftService;

    private ConcreteGift concreteGift;
    private GiftSuggestion giftSuggestion;
    private UUID concreteGiftId;
    private UUID giftSuggestionId;

    @BeforeEach
    void setUp() {
        concreteGiftId = UUID.randomUUID();
        giftSuggestionId = UUID.randomUUID();

        giftSuggestion = new GiftSuggestion(
                "Test Gift Suggestion",
                "Test Description",
                new BigDecimal("10.00"),
                new BigDecimal("100.00"),
                AgeGroup.ADULT,
                Gender.UNISEX,
                Interest.TECHNOLOGY,
                Occasion.BIRTHDAY,
                Relationship.FRIEND,
                PersonalityType.CREATIVE
        );
        giftSuggestion.setId(giftSuggestionId);

        concreteGift = new ConcreteGift(
                "Test Concrete Gift",
                "Test concrete description",
                new BigDecimal("25.00"),
                "Amazon",
                giftSuggestion
        );
        concreteGift.setId(concreteGiftId);
    }

    @Test
    void create_WithValidConcreteGift_ShouldReturnSavedConcreteGift() {
        // Given
        ConcreteGift newConcreteGift = new ConcreteGift(
                "New Concrete Gift",
                "New description",
                new BigDecimal("30.00"),
                "eBay",
                giftSuggestion
        );

        when(giftSuggestionRepository.findById(giftSuggestionId)).thenReturn(Optional.of(giftSuggestion));
        when(concreteGiftRepository.save(any(ConcreteGift.class))).thenReturn(concreteGift);

        // When
        ConcreteGift result = concreteGiftService.create(newConcreteGift);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(concreteGiftId);
        verify(giftSuggestionRepository).findById(giftSuggestionId);
        verify(concreteGiftRepository).save(newConcreteGift);
    }

    @Test
    void create_WithNullConcreteGift_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> concreteGiftService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Concrete gift cannot be null");

        verify(concreteGiftRepository, never()).save(any());
    }

    @Test
    void create_WithExistingId_ShouldThrowException() {
        // Given
        concreteGift.setId(UUID.randomUUID());

        // When & Then
        assertThatThrownBy(() -> concreteGiftService.create(concreteGift))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Concrete gift ID must be null for creation");

        verify(concreteGiftRepository, never()).save(any());
    }

    @Test
    void create_WithInvalidGiftSuggestion_ShouldThrowException() {
        // Given
        UUID invalidSuggestionId = UUID.randomUUID();
        giftSuggestion.setId(invalidSuggestionId);
        ConcreteGift invalidConcreteGift = new ConcreteGift(
                "Invalid Gift",
                "Invalid description",
                new BigDecimal("25.00"),
                "Amazon",
                giftSuggestion
        );

        when(giftSuggestionRepository.findById(invalidSuggestionId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> concreteGiftService.create(invalidConcreteGift))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Gift suggestion not found with ID: " + invalidSuggestionId);

        verify(giftSuggestionRepository).findById(invalidSuggestionId);
        verify(concreteGiftRepository, never()).save(any());
    }

    @Test
    void create_WithPriceOutsideRange_ShouldThrowException() {
        // Given
        ConcreteGift expensiveGift = new ConcreteGift(
                "Expensive Gift",
                "Too expensive",
                new BigDecimal("200.00"), // Above max price of 100.00
                "Luxury Store",
                giftSuggestion
        );

        when(giftSuggestionRepository.findById(giftSuggestionId)).thenReturn(Optional.of(giftSuggestion));

        // When & Then
        assertThatThrownBy(() -> concreteGiftService.create(expensiveGift))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("above maximum price");

        verify(giftSuggestionRepository).findById(giftSuggestionId);
        verify(concreteGiftRepository, never()).save(any());
    }

    @Test
    void findById_WithValidId_ShouldReturnConcreteGift() {
        // Given
        when(concreteGiftRepository.findById(concreteGiftId)).thenReturn(Optional.of(concreteGift));

        // When
        Optional<ConcreteGift> result = concreteGiftService.findById(concreteGiftId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(concreteGift);
        verify(concreteGiftRepository).findById(concreteGiftId);
    }

    @Test
    void findById_WithNullId_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> concreteGiftService.findById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID cannot be null");

        verify(concreteGiftRepository, never()).findById(any());
    }

    @Test
    void findAll_WithPageable_ShouldReturnPagedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<ConcreteGift> concreteGifts = Arrays.asList(concreteGift);
        Page<ConcreteGift> page = new PageImpl<>(concreteGifts, pageable, 1);

        when(concreteGiftRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<ConcreteGift> result = concreteGiftService.findAll(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(concreteGift);
        verify(concreteGiftRepository).findAll(pageable);
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedConcreteGift() {
        // Given
        ConcreteGift updateData = new ConcreteGift(
                "Updated Name",
                "Updated description",
                new BigDecimal("35.00"),
                "Updated Vendor",
                giftSuggestion
        );

        when(concreteGiftRepository.findById(concreteGiftId)).thenReturn(Optional.of(concreteGift));
        when(giftSuggestionRepository.findById(giftSuggestionId)).thenReturn(Optional.of(giftSuggestion));
        when(concreteGiftRepository.save(any(ConcreteGift.class))).thenReturn(concreteGift);

        // When
        ConcreteGift result = concreteGiftService.update(concreteGiftId, updateData);

        // Then
        assertThat(result).isNotNull();
        verify(concreteGiftRepository).findById(concreteGiftId);
        verify(giftSuggestionRepository).findById(giftSuggestionId);
        verify(concreteGiftRepository).save(concreteGift);
    }

    @Test
    void deleteById_WithValidId_ShouldDeleteConcreteGift() {
        // Given
        when(concreteGiftRepository.existsById(concreteGiftId)).thenReturn(true);

        // When
        concreteGiftService.deleteById(concreteGiftId);

        // Then
        verify(concreteGiftRepository).existsById(concreteGiftId);
        verify(concreteGiftRepository).deleteById(concreteGiftId);
    }

    @Test
    void deleteById_WithNonExistentId_ShouldThrowException() {
        // Given
        when(concreteGiftRepository.existsById(concreteGiftId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> concreteGiftService.deleteById(concreteGiftId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Concrete gift not found with ID: " + concreteGiftId);

        verify(concreteGiftRepository).existsById(concreteGiftId);
        verify(concreteGiftRepository, never()).deleteById(concreteGiftId);
    }

    @Test
    void findByGiftSuggestionId_WithValidId_ShouldReturnConcreteGifts() {
        // Given
        List<ConcreteGift> concreteGifts = Arrays.asList(concreteGift);
        when(concreteGiftRepository.findByGiftSuggestionId(giftSuggestionId)).thenReturn(concreteGifts);

        // When
        List<ConcreteGift> result = concreteGiftService.findByGiftSuggestionId(giftSuggestionId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(concreteGift);
        verify(concreteGiftRepository).findByGiftSuggestionId(giftSuggestionId);
    }

    @Test
    void findByGiftSuggestionId_WithPageable_ShouldReturnPagedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<ConcreteGift> concreteGifts = Arrays.asList(concreteGift);
        Page<ConcreteGift> page = new PageImpl<>(concreteGifts, pageable, 1);

        when(giftSuggestionRepository.findById(giftSuggestionId)).thenReturn(Optional.of(giftSuggestion));
        when(concreteGiftRepository.findByGiftSuggestion(giftSuggestion, pageable)).thenReturn(page);

        // When
        Page<ConcreteGift> result = concreteGiftService.findByGiftSuggestionId(giftSuggestionId, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(giftSuggestionRepository).findById(giftSuggestionId);
        verify(concreteGiftRepository).findByGiftSuggestion(giftSuggestion, pageable);
    }

    @Test
    void findByVendorName_WithValidVendor_ShouldReturnConcreteGifts() {
        // Given
        String vendorName = "Amazon";
        List<ConcreteGift> concreteGifts = Arrays.asList(concreteGift);
        when(concreteGiftRepository.findByVendorName(vendorName)).thenReturn(concreteGifts);

        // When
        List<ConcreteGift> result = concreteGiftService.findByVendorName(vendorName);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(concreteGift);
        verify(concreteGiftRepository).findByVendorName(vendorName);
    }

    @Test
    void findByAvailable_WithAvailabilityStatus_ShouldReturnConcreteGifts() {
        // Given
        List<ConcreteGift> concreteGifts = Arrays.asList(concreteGift);
        when(concreteGiftRepository.findByAvailable(true)).thenReturn(concreteGifts);

        // When
        List<ConcreteGift> result = concreteGiftService.findByAvailable(true);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(concreteGift);
        verify(concreteGiftRepository).findByAvailable(true);
    }

    @Test
    void findByPriceRange_WithValidRange_ShouldReturnConcreteGifts() {
        // Given
        BigDecimal minPrice = new BigDecimal("20.00");
        BigDecimal maxPrice = new BigDecimal("30.00");
        List<ConcreteGift> concreteGifts = Arrays.asList(concreteGift);

        when(concreteGiftRepository.findByPriceRange(minPrice, maxPrice)).thenReturn(concreteGifts);

        // When
        List<ConcreteGift> result = concreteGiftService.findByPriceRange(minPrice, maxPrice);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(concreteGift);
        verify(concreteGiftRepository).findByPriceRange(minPrice, maxPrice);
    }

    @Test
    void findByAdvancedCriteria_WithValidCriteria_ShouldReturnPagedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<ConcreteGift> concreteGifts = Arrays.asList(concreteGift);
        Page<ConcreteGift> page = new PageImpl<>(concreteGifts, pageable, 1);

        when(concreteGiftRepository.findByAdvancedCriteria(
                any(), any(), any(), any(), any())).thenReturn(page);

        // When
        Page<ConcreteGift> result = concreteGiftService.findByAdvancedCriteria(
                giftSuggestionId, "Amazon", true, 
                new BigDecimal("20.00"), new BigDecimal("30.00"), pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(concreteGiftRepository).findByAdvancedCriteria(
                "Amazon", true, new BigDecimal("20.00"), new BigDecimal("30.00"), pageable);
    }

    @Test
    void countByGiftSuggestionId_WithValidId_ShouldReturnCount() {
        // Given
        List<ConcreteGift> concreteGifts = Arrays.asList(concreteGift, concreteGift);
        when(concreteGiftRepository.findByGiftSuggestionId(giftSuggestionId)).thenReturn(concreteGifts);

        // When
        long result = concreteGiftService.countByGiftSuggestionId(giftSuggestionId);

        // Then
        assertThat(result).isEqualTo(2L);
        verify(concreteGiftRepository).findByGiftSuggestionId(giftSuggestionId);
    }

    @Test
    void validateGiftSuggestionAssociation_WithValidPrice_ShouldNotThrowException() {
        // Given
        ConcreteGift validGift = new ConcreteGift(
                "Valid Gift",
                "Valid description",
                new BigDecimal("50.00"), // Within range 10-100
                "Amazon",
                giftSuggestion
        );

        // When & Then
        concreteGiftService.validateGiftSuggestionAssociation(validGift, giftSuggestion);
        // Should not throw any exception
    }

    @Test
    void validateGiftSuggestionAssociation_WithPriceTooHigh_ShouldThrowException() {
        // Given
        ConcreteGift expensiveGift = new ConcreteGift(
                "Expensive Gift",
                "Too expensive",
                new BigDecimal("150.00"), // Above max price of 100.00
                "Luxury Store",
                giftSuggestion
        );

        // When & Then
        assertThatThrownBy(() -> 
                concreteGiftService.validateGiftSuggestionAssociation(expensiveGift, giftSuggestion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("above maximum price");
    }

    @Test
    void validateGiftSuggestionAssociation_WithPriceTooLow_ShouldThrowException() {
        // Given
        ConcreteGift cheapGift = new ConcreteGift(
                "Cheap Gift",
                "Too cheap",
                new BigDecimal("5.00"), // Below min price of 10.00
                "Discount Store",
                giftSuggestion
        );

        // When & Then
        assertThatThrownBy(() -> 
                concreteGiftService.validateGiftSuggestionAssociation(cheapGift, giftSuggestion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("below minimum price");
    }
}