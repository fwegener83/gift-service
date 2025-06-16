package com.giftservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for concrete gift response.
 */
@Schema(description = "Concrete gift response")
public class ConcreteGiftResponse {

    @Schema(description = "Unique identifier of the concrete gift", example = "456e7890-e89b-12d3-a456-426614174001")
    private UUID id;

    @Schema(description = "Name of the concrete gift", example = "Sony WH-1000XM4 Headphones")
    private String name;

    @Schema(description = "Detailed description of the concrete gift", 
            example = "Industry-leading noise canceling headphones with 30-hour battery life")
    private String description;

    @Schema(description = "Exact price of the concrete gift", example = "349.99")
    private BigDecimal exactPrice;

    @Schema(description = "Name of the vendor/store selling the gift", example = "Amazon")
    private String vendorName;

    @Schema(description = "URL to the product page", example = "https://amazon.com/sony-wh1000xm4-headphones")
    private String productUrl;

    @Schema(description = "Stock keeping unit for the product", example = "SONY-WH1000XM4-BLK")
    private String productSku;

    @Schema(description = "Whether the product is currently available", example = "true")
    private Boolean available;

    @Schema(description = "Date and time when the concrete gift was created")
    private LocalDateTime createdDate;

    @Schema(description = "Date and time when the concrete gift was last modified")
    private LocalDateTime lastModifiedDate;

    @Schema(description = "ID of the associated gift suggestion", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID giftSuggestionId;

    @Schema(description = "Name of the associated gift suggestion", example = "Wireless Bluetooth Headphones")
    private String giftSuggestionName;

    // Default constructor
    public ConcreteGiftResponse() {}

    // Constructor with all fields
    public ConcreteGiftResponse(UUID id, String name, String description, BigDecimal exactPrice, String vendorName,
                              String productUrl, String productSku, Boolean available,
                              LocalDateTime createdDate, LocalDateTime lastModifiedDate,
                              UUID giftSuggestionId, String giftSuggestionName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exactPrice = exactPrice;
        this.vendorName = vendorName;
        this.productUrl = productUrl;
        this.productSku = productSku;
        this.available = available;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.giftSuggestionId = giftSuggestionId;
        this.giftSuggestionName = giftSuggestionName;
    }

    // Getters and setters
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

    public UUID getGiftSuggestionId() {
        return giftSuggestionId;
    }

    public void setGiftSuggestionId(UUID giftSuggestionId) {
        this.giftSuggestionId = giftSuggestionId;
    }

    public String getGiftSuggestionName() {
        return giftSuggestionName;
    }

    public void setGiftSuggestionName(String giftSuggestionName) {
        this.giftSuggestionName = giftSuggestionName;
    }
}