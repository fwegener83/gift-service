package com.giftservice.dto.request;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for creating a new concrete gift.
 */
@Schema(description = "Request to create a new concrete gift")
public class CreateConcreteGiftRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 150, message = "Name must not exceed 150 characters")
    @Schema(description = "Name of the concrete gift", example = "Sony WH-1000XM4 Headphones", required = true)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Schema(description = "Detailed description of the concrete gift", 
            example = "Industry-leading noise canceling headphones with 30-hour battery life", required = true)
    private String description;

    @NotNull(message = "Exact price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Exact price must be positive")
    @Schema(description = "Exact price of the concrete gift", example = "349.99", required = true)
    private BigDecimal exactPrice;

    @NotBlank(message = "Vendor name is required")
    @Size(max = 100, message = "Vendor name must not exceed 100 characters")
    @Schema(description = "Name of the vendor/store selling the gift", example = "Amazon", required = true)
    private String vendorName;

    @Size(max = 500, message = "Product URL must not exceed 500 characters")
    @Schema(description = "URL to the product page", example = "https://amazon.com/sony-wh1000xm4-headphones")
    private String productUrl;

    @Size(max = 50, message = "Product SKU must not exceed 50 characters")
    @Schema(description = "Stock keeping unit for the product", example = "SONY-WH1000XM4-BLK")
    private String productSku;

    @Schema(description = "Whether the product is currently available", example = "true", defaultValue = "true")
    private Boolean available = true;

    @NotNull(message = "Gift suggestion ID is required")
    @Schema(description = "ID of the associated gift suggestion", required = true)
    private UUID giftSuggestionId;

    // Default constructor
    public CreateConcreteGiftRequest() {}

    // Constructor with required fields
    public CreateConcreteGiftRequest(String name, String description, BigDecimal exactPrice, 
                                   String vendorName, UUID giftSuggestionId) {
        this.name = name;
        this.description = description;
        this.exactPrice = exactPrice;
        this.vendorName = vendorName;
        this.giftSuggestionId = giftSuggestionId;
    }

    // Constructor with all fields
    public CreateConcreteGiftRequest(String name, String description, BigDecimal exactPrice, String vendorName,
                                   String productUrl, String productSku, Boolean available, UUID giftSuggestionId) {
        this.name = name;
        this.description = description;
        this.exactPrice = exactPrice;
        this.vendorName = vendorName;
        this.productUrl = productUrl;
        this.productSku = productSku;
        this.available = available;
        this.giftSuggestionId = giftSuggestionId;
    }

    // Getters and setters
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

    public UUID getGiftSuggestionId() {
        return giftSuggestionId;
    }

    public void setGiftSuggestionId(UUID giftSuggestionId) {
        this.giftSuggestionId = giftSuggestionId;
    }
}