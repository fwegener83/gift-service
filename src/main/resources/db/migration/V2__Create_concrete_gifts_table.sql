-- Create concrete_gifts table
-- This table stores specific gift implementations linked to gift suggestions

CREATE TABLE concrete_gifts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    exact_price DECIMAL(10,2) NOT NULL CHECK (exact_price > 0),
    vendor_name VARCHAR(100) NOT NULL,
    product_url VARCHAR(500),
    product_sku VARCHAR(50),
    available BOOLEAN NOT NULL DEFAULT true,
    gift_suggestion_id UUID NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP WITH TIME ZONE,
    
    -- Foreign key constraint to gift_suggestions table
    CONSTRAINT fk_concrete_gifts_gift_suggestion 
        FOREIGN KEY (gift_suggestion_id) 
        REFERENCES gift_suggestions(id) 
        ON DELETE CASCADE
);

-- Create indexes for efficient querying
CREATE INDEX idx_concrete_gifts_gift_suggestion_id ON concrete_gifts(gift_suggestion_id);
CREATE INDEX idx_concrete_gifts_vendor_name ON concrete_gifts(vendor_name);
CREATE INDEX idx_concrete_gifts_available ON concrete_gifts(available);
CREATE INDEX idx_concrete_gifts_exact_price ON concrete_gifts(exact_price);
CREATE INDEX idx_concrete_gifts_created_date ON concrete_gifts(created_date);

-- Composite index for filtering available gifts by price
CREATE INDEX idx_concrete_gifts_available_price ON concrete_gifts(available, exact_price);

-- Unique index on product_sku per vendor to avoid duplicates
CREATE UNIQUE INDEX idx_concrete_gifts_vendor_sku ON concrete_gifts(vendor_name, product_sku) 
WHERE product_sku IS NOT NULL;