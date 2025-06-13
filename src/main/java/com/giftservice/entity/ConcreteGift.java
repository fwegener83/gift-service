package com.giftservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing specific gift implementations linked to gift suggestions.
 * This entity handles the Many-to-One relationship with GiftSuggestion
 * and contains vendor-specific information for actual gift purchases.
 */
@Entity
@Table(name = "concrete_gifts")
@EntityListeners(AuditingEntityListener.class)
public class ConcreteGift {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(max = 150, message = "Name must not exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Exact price is required")
    @Positive(message = "Price must be positive")
    @Column(name = "exact_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal exactPrice;

    @NotBlank(message = "Vendor name is required")
    @Size(max = 100, message = "Vendor name must not exceed 100 characters")
    @Column(name = "vendor_name", nullable = false, length = 100)
    private String vendorName;

    @URL(message = "Product URL must be a valid URL")
    @Size(max = 500, message = "Product URL must not exceed 500 characters")
    @Column(name = "product_url", length = 500)
    private String productUrl;

    @Size(max = 50, message = "Product SKU must not exceed 50 characters")
    @Column(name = "product_sku", length = 50)
    private String productSku;

    @NotNull(message = "Availability status is required")
    @Column(nullable = false)
    private Boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "gift_suggestion_id", nullable = false)
    @NotNull(message = "Gift suggestion is required")
    private GiftSuggestion giftSuggestion;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    /**
     * Default constructor for JPA
     */
    public ConcreteGift() {
    }

    /**
     * Constructor with essential fields
     */
    public ConcreteGift(String name, String description, BigDecimal exactPrice, 
                       String vendorName, GiftSuggestion giftSuggestion) {
        this.name = name;
        this.description = description;
        this.exactPrice = exactPrice;
        this.vendorName = vendorName;
        this.giftSuggestion = giftSuggestion;
        this.available = true;
    }

    /**
     * Constructor with all optional fields
     */
    public ConcreteGift(String name, String description, BigDecimal exactPrice, 
                       String vendorName, String productUrl, String productSku,
                       Boolean available, GiftSuggestion giftSuggestion) {
        this.name = name;
        this.description = description;
        this.exactPrice = exactPrice;
        this.vendorName = vendorName;
        this.productUrl = productUrl;
        this.productSku = productSku;
        this.available = available != null ? available : true;
        this.giftSuggestion = giftSuggestion;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getExactPrice() {
        return exactPrice;
    }

    public void setExactPrice(BigDecimal exactPrice) {
        this.exactPrice = exactPrice;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public GiftSuggestion getGiftSuggestion() {
        return giftSuggestion;
    }

    public void setGiftSuggestion(GiftSuggestion giftSuggestion) {
        this.giftSuggestion = giftSuggestion;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcreteGift that = (ConcreteGift) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ConcreteGift{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", exactPrice=" + exactPrice +
                ", vendorName='" + vendorName + '\'' +
                ", productUrl='" + productUrl + '\'' +
                ", productSku='" + productSku + '\'' +
                ", available=" + available +
                ", giftSuggestionId=" + (giftSuggestion != null ? giftSuggestion.getId() : null) +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}