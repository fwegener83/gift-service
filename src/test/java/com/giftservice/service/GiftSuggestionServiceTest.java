package com.giftservice.service;

import com.giftservice.entity.GiftSuggestion;
import com.giftservice.enums.*;
import com.giftservice.repository.GiftSuggestionRepository;
import com.giftservice.service.impl.GiftSuggestionServiceImpl;
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
class GiftSuggestionServiceTest {

    @Mock
    private GiftSuggestionRepository giftSuggestionRepository;

    @InjectMocks
    private GiftSuggestionServiceImpl giftSuggestionService;

    private GiftSuggestion giftSuggestion;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        giftSuggestion = new GiftSuggestion(
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
        giftSuggestion.setId(testId);
    }

    @Test
    void create_WithValidGiftSuggestion_ShouldReturnSavedGiftSuggestion() {
        // Given
        GiftSuggestion newGiftSuggestion = new GiftSuggestion(
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
        
        when(giftSuggestionRepository.save(any(GiftSuggestion.class))).thenReturn(giftSuggestion);

        // When
        GiftSuggestion result = giftSuggestionService.create(newGiftSuggestion);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        verify(giftSuggestionRepository).save(newGiftSuggestion);
    }

    @Test
    void create_WithNullGiftSuggestion_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> giftSuggestionService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Gift suggestion cannot be null");

        verify(giftSuggestionRepository, never()).save(any());
    }

    @Test
    void create_WithExistingId_ShouldThrowException() {
        // Given
        giftSuggestion.setId(UUID.randomUUID());

        // When & Then
        assertThatThrownBy(() -> giftSuggestionService.create(giftSuggestion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Gift suggestion ID must be null for creation");

        verify(giftSuggestionRepository, never()).save(any());
    }

    @Test
    void create_WithInvalidPriceRange_ShouldThrowException() {
        // Given
        GiftSuggestion invalidGiftSuggestion = new GiftSuggestion(
                "Test Gift",
                "Test Description",
                new BigDecimal("100.00"), // min > max
                new BigDecimal("50.00"),
                AgeGroup.ADULT,
                Gender.UNISEX,
                Interest.TECHNOLOGY,
                Occasion.BIRTHDAY,
                Relationship.FRIEND,
                PersonalityType.CREATIVE
        );

        // When & Then
        assertThatThrownBy(() -> giftSuggestionService.create(invalidGiftSuggestion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Minimum price cannot be greater than maximum price");

        verify(giftSuggestionRepository, never()).save(any());
    }

    @Test
    void findById_WithValidId_ShouldReturnGiftSuggestion() {
        // Given
        when(giftSuggestionRepository.findById(testId)).thenReturn(Optional.of(giftSuggestion));

        // When
        Optional<GiftSuggestion> result = giftSuggestionService.findById(testId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(giftSuggestion);
        verify(giftSuggestionRepository).findById(testId);
    }

    @Test
    void findById_WithInvalidId_ShouldReturnEmpty() {
        // Given
        when(giftSuggestionRepository.findById(testId)).thenReturn(Optional.empty());

        // When
        Optional<GiftSuggestion> result = giftSuggestionService.findById(testId);

        // Then
        assertThat(result).isEmpty();
        verify(giftSuggestionRepository).findById(testId);
    }

    @Test
    void findById_WithNullId_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> giftSuggestionService.findById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID cannot be null");

        verify(giftSuggestionRepository, never()).findById(any());
    }

    @Test
    void findAll_WithPageable_ShouldReturnPagedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<GiftSuggestion> giftSuggestions = Arrays.asList(giftSuggestion);
        Page<GiftSuggestion> page = new PageImpl<>(giftSuggestions, pageable, 1);
        
        when(giftSuggestionRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<GiftSuggestion> result = giftSuggestionService.findAll(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(giftSuggestion);
        verify(giftSuggestionRepository).findAll(pageable);
    }

    @Test
    void findAll_WithNullPageable_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> giftSuggestionService.findAll((Pageable) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Pageable cannot be null");

        verify(giftSuggestionRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void findAll_WithoutPageable_ShouldReturnAllResults() {
        // Given
        List<GiftSuggestion> giftSuggestions = Arrays.asList(giftSuggestion);
        when(giftSuggestionRepository.findAll()).thenReturn(giftSuggestions);

        // When
        List<GiftSuggestion> result = giftSuggestionService.findAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(giftSuggestion);
        verify(giftSuggestionRepository).findAll();
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedGiftSuggestion() {
        // Given
        GiftSuggestion updateData = new GiftSuggestion(
                "Updated Name",
                "Updated Description",
                new BigDecimal("15.00"),
                new BigDecimal("75.00"),
                AgeGroup.CHILD,
                Gender.FEMALE,
                Interest.ART,
                Occasion.EASTER,
                Relationship.EXTENDED_FAMILY,
                PersonalityType.INTELLECTUAL
        );

        when(giftSuggestionRepository.findById(testId)).thenReturn(Optional.of(giftSuggestion));
        when(giftSuggestionRepository.save(any(GiftSuggestion.class))).thenReturn(giftSuggestion);

        // When
        GiftSuggestion result = giftSuggestionService.update(testId, updateData);

        // Then
        assertThat(result).isNotNull();
        verify(giftSuggestionRepository).findById(testId);
        verify(giftSuggestionRepository).save(giftSuggestion);
    }

    @Test
    void update_WithNonExistentId_ShouldThrowException() {
        // Given
        GiftSuggestion updateData = new GiftSuggestion(
                "Updated Name",
                "Updated Description",
                new BigDecimal("15.00"),
                new BigDecimal("75.00"),
                AgeGroup.CHILD,
                Gender.FEMALE,
                Interest.ART,
                Occasion.EASTER,
                Relationship.EXTENDED_FAMILY,
                PersonalityType.INTELLECTUAL
        );

        when(giftSuggestionRepository.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> giftSuggestionService.update(testId, updateData))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Gift suggestion not found with ID: " + testId);

        verify(giftSuggestionRepository).findById(testId);
        verify(giftSuggestionRepository, never()).save(any());
    }

    @Test
    void deleteById_WithValidId_ShouldDeleteGiftSuggestion() {
        // Given
        when(giftSuggestionRepository.existsById(testId)).thenReturn(true);

        // When
        giftSuggestionService.deleteById(testId);

        // Then
        verify(giftSuggestionRepository).existsById(testId);
        verify(giftSuggestionRepository).deleteById(testId);
    }

    @Test
    void deleteById_WithNonExistentId_ShouldThrowException() {
        // Given
        when(giftSuggestionRepository.existsById(testId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> giftSuggestionService.deleteById(testId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Gift suggestion not found with ID: " + testId);

        verify(giftSuggestionRepository).existsById(testId);
        verify(giftSuggestionRepository, never()).deleteById(testId);
    }

    @Test
    void existsById_WithValidId_ShouldReturnTrue() {
        // Given
        when(giftSuggestionRepository.existsById(testId)).thenReturn(true);

        // When
        boolean result = giftSuggestionService.existsById(testId);

        // Then
        assertThat(result).isTrue();
        verify(giftSuggestionRepository).existsById(testId);
    }

    @Test
    void findByAgeGroup_WithValidAgeGroup_ShouldReturnGiftSuggestions() {
        // Given
        List<GiftSuggestion> giftSuggestions = Arrays.asList(giftSuggestion);
        when(giftSuggestionRepository.findByAgeGroup(AgeGroup.ADULT)).thenReturn(giftSuggestions);

        // When
        List<GiftSuggestion> result = giftSuggestionService.findByAgeGroup(AgeGroup.ADULT);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(giftSuggestion);
        verify(giftSuggestionRepository).findByAgeGroup(AgeGroup.ADULT);
    }

    @Test
    void findByAdvancedCriteria_WithValidCriteria_ShouldReturnPagedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<GiftSuggestion> giftSuggestions = Arrays.asList(giftSuggestion);
        Page<GiftSuggestion> page = new PageImpl<>(giftSuggestions, pageable, 1);
        
        when(giftSuggestionRepository.findByAdvancedCriteria(
                any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(page);

        // When
        Page<GiftSuggestion> result = giftSuggestionService.findByAdvancedCriteria(
                AgeGroup.ADULT, Gender.UNISEX, Interest.TECHNOLOGY, 
                Occasion.BIRTHDAY, Relationship.FRIEND, PersonalityType.CREATIVE,
                new BigDecimal("100.00"), pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(giftSuggestionRepository).findByAdvancedCriteria(
                AgeGroup.ADULT, Gender.UNISEX, Interest.TECHNOLOGY, 
                Occasion.BIRTHDAY, Relationship.FRIEND, PersonalityType.CREATIVE,
                new BigDecimal("100.00"), pageable);
    }

    @Test
    void countByAdvancedCriteria_WithValidCriteria_ShouldReturnCount() {
        // Given
        when(giftSuggestionRepository.countByAdvancedCriteria(
                any(), any(), any(), any(), any(), any(), any())).thenReturn(5L);

        // When
        long result = giftSuggestionService.countByAdvancedCriteria(
                AgeGroup.ADULT, Gender.UNISEX, Interest.TECHNOLOGY, 
                Occasion.BIRTHDAY, Relationship.FRIEND, PersonalityType.CREATIVE,
                new BigDecimal("100.00"));

        // Then
        assertThat(result).isEqualTo(5L);
        verify(giftSuggestionRepository).countByAdvancedCriteria(
                AgeGroup.ADULT, Gender.UNISEX, Interest.TECHNOLOGY, 
                Occasion.BIRTHDAY, Relationship.FRIEND, PersonalityType.CREATIVE,
                new BigDecimal("100.00"));
    }

    @Test
    void findByPriceRange_WithValidRange_ShouldReturnGiftSuggestions() {
        // Given
        BigDecimal minPrice = new BigDecimal("10.00");
        BigDecimal maxPrice = new BigDecimal("50.00");
        List<GiftSuggestion> giftSuggestions = Arrays.asList(giftSuggestion);
        
        when(giftSuggestionRepository.findGiftsWithinBudget(minPrice, maxPrice)).thenReturn(giftSuggestions);

        // When
        List<GiftSuggestion> result = giftSuggestionService.findByPriceRange(minPrice, maxPrice);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(giftSuggestion);
        verify(giftSuggestionRepository).findGiftsWithinBudget(minPrice, maxPrice);
    }

    @Test
    void findByPriceRange_WithInvalidRange_ShouldThrowException() {
        // Given
        BigDecimal minPrice = new BigDecimal("100.00");
        BigDecimal maxPrice = new BigDecimal("50.00");

        // When & Then
        assertThatThrownBy(() -> giftSuggestionService.findByPriceRange(minPrice, maxPrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Minimum price cannot be greater than maximum price");

        verify(giftSuggestionRepository, never()).findGiftsWithinBudget(any(), any());
    }
}