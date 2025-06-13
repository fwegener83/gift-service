package com.giftservice.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive unit tests for all domain enums.
 * Verifies enum completeness, values, and behavior.
 */
class EnumTest {

    @Test
    void ageGroup_shouldHaveAllExpectedValues() {
        // Given
        AgeGroup[] expectedValues = {
            AgeGroup.BABY, AgeGroup.TODDLER, AgeGroup.CHILD, 
            AgeGroup.TEEN, AgeGroup.YOUNG_ADULT, AgeGroup.ADULT, AgeGroup.SENIOR
        };
        
        // When
        AgeGroup[] actualValues = AgeGroup.values();
        
        // Then
        assertThat(actualValues).hasSize(7);
        assertThat(actualValues).containsExactlyInAnyOrder(expectedValues);
    }

    @Test
    void gender_shouldHaveAllExpectedValues() {
        // Given
        Gender[] expectedValues = {
            Gender.MALE, Gender.FEMALE, Gender.UNISEX, Gender.NON_BINARY
        };
        
        // When
        Gender[] actualValues = Gender.values();
        
        // Then
        assertThat(actualValues).hasSize(4);
        assertThat(actualValues).containsExactlyInAnyOrder(expectedValues);
    }

    @Test
    void interest_shouldHaveMinimum15Values() {
        // When
        Interest[] actualValues = Interest.values();
        
        // Then
        assertThat(actualValues).hasSizeGreaterThanOrEqualTo(15);
        assertThat(actualValues).contains(
            Interest.SPORTS, Interest.MUSIC, Interest.READING, Interest.COOKING,
            Interest.PHOTOGRAPHY, Interest.GARDENING, Interest.TECHNOLOGY, Interest.TRAVEL
        );
    }

    @Test
    void occasion_shouldHaveAllExpectedValues() {
        // When
        Occasion[] actualValues = Occasion.values();
        
        // Then
        assertThat(actualValues).hasSizeGreaterThanOrEqualTo(10);
        assertThat(actualValues).contains(
            Occasion.BIRTHDAY, Occasion.WEDDING, Occasion.ANNIVERSARY, 
            Occasion.GRADUATION, Occasion.CHRISTMAS, Occasion.VALENTINES_DAY
        );
    }

    @Test
    void relationship_shouldHaveAllExpectedValues() {
        // When
        Relationship[] actualValues = Relationship.values();
        
        // Then
        assertThat(actualValues).hasSizeGreaterThanOrEqualTo(5);
        assertThat(actualValues).contains(
            Relationship.FAMILY, Relationship.FRIEND, Relationship.COLLEAGUE,
            Relationship.ROMANTIC_PARTNER, Relationship.ACQUAINTANCE
        );
    }

    @Test
    void personalityType_shouldHaveAllExpectedValues() {
        // When
        PersonalityType[] actualValues = PersonalityType.values();
        
        // Then
        assertThat(actualValues).hasSizeGreaterThanOrEqualTo(10);
        assertThat(actualValues).contains(
            PersonalityType.EXTROVERT, PersonalityType.INTROVERT, PersonalityType.ADVENTUROUS,
            PersonalityType.CREATIVE, PersonalityType.ANALYTICAL, PersonalityType.PRACTICAL
        );
    }

    @ParameterizedTest
    @EnumSource(AgeGroup.class)
    void ageGroup_shouldHaveValidStringRepresentation(AgeGroup ageGroup) {
        // When & Then
        assertThat(ageGroup.name()).isNotBlank();
        assertThat(ageGroup.toString()).isNotBlank();
    }

    @ParameterizedTest
    @EnumSource(Gender.class)
    void gender_shouldHaveValidStringRepresentation(Gender gender) {
        // When & Then
        assertThat(gender.name()).isNotBlank();
        assertThat(gender.toString()).isNotBlank();
    }

    @ParameterizedTest
    @EnumSource(Interest.class)
    void interest_shouldHaveValidStringRepresentation(Interest interest) {
        // When & Then
        assertThat(interest.name()).isNotBlank();
        assertThat(interest.toString()).isNotBlank();
    }

    @ParameterizedTest
    @EnumSource(Occasion.class)
    void occasion_shouldHaveValidStringRepresentation(Occasion occasion) {
        // When & Then
        assertThat(occasion.name()).isNotBlank();
        assertThat(occasion.toString()).isNotBlank();
    }

    @ParameterizedTest
    @EnumSource(Relationship.class)
    void relationship_shouldHaveValidStringRepresentation(Relationship relationship) {
        // When & Then
        assertThat(relationship.name()).isNotBlank();
        assertThat(relationship.toString()).isNotBlank();
    }

    @ParameterizedTest
    @EnumSource(PersonalityType.class)
    void personalityType_shouldHaveValidStringRepresentation(PersonalityType personalityType) {
        // When & Then
        assertThat(personalityType.name()).isNotBlank();
        assertThat(personalityType.toString()).isNotBlank();
    }

    @Test
    void allEnums_shouldBeUsableInSwitchStatements() {
        // Test that enums can be used in switch statements (compilation test)
        AgeGroup ageGroup = AgeGroup.ADULT;
        String result = switch (ageGroup) {
            case BABY -> "baby";
            case TODDLER -> "toddler";
            case CHILD -> "child";
            case TEEN -> "teen";
            case YOUNG_ADULT -> "young_adult";
            case ADULT -> "adult";
            case SENIOR -> "senior";
        };
        
        assertThat(result).isEqualTo("adult");
    }

    @Test
    void allEnums_shouldSupportValueOfMethod() {
        // Test that all enums support valueOf method
        assertThat(AgeGroup.valueOf("ADULT")).isEqualTo(AgeGroup.ADULT);
        assertThat(Gender.valueOf("UNISEX")).isEqualTo(Gender.UNISEX);
        assertThat(Interest.valueOf("MUSIC")).isEqualTo(Interest.MUSIC);
        assertThat(Occasion.valueOf("BIRTHDAY")).isEqualTo(Occasion.BIRTHDAY);
        assertThat(Relationship.valueOf("FAMILY")).isEqualTo(Relationship.FAMILY);
        assertThat(PersonalityType.valueOf("CREATIVE")).isEqualTo(PersonalityType.CREATIVE);
    }
}