package com.giftservice.enums;

/**
 * Relationship context categorization for gift suggestions.
 * This enum defines different types of relationships between
 * the gift giver and recipient to help match appropriate gifts
 * based on the social context and intimacy level.
 */
public enum Relationship {
    
    /**
     * Family members (parents, siblings, children, etc.)
     */
    FAMILY,
    
    /**
     * Close personal friends
     */
    FRIEND,
    
    /**
     * Work colleagues and professional contacts
     */
    COLLEAGUE,
    
    /**
     * Romantic partners (spouse, boyfriend, girlfriend, etc.)
     */
    ROMANTIC_PARTNER,
    
    /**
     * Acquaintances and casual contacts
     */
    ACQUAINTANCE,
    
    /**
     * Extended family members (cousins, aunts, uncles, etc.)
     */
    EXTENDED_FAMILY,
    
    /**
     * Neighbors
     */
    NEIGHBOR,
    
    /**
     * Teachers, mentors, or students
     */
    MENTOR_STUDENT,
    
    /**
     * Business clients or customers
     */
    CLIENT,
    
    /**
     * Boss or supervisor
     */
    BOSS
}