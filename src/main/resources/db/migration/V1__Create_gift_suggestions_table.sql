-- Create gift_suggestions table
-- This table stores the main gift suggestion entities with all categorization attributes

CREATE TABLE gift_suggestions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    min_price DECIMAL(10,2) NOT NULL CHECK (min_price > 0),
    max_price DECIMAL(10,2) NOT NULL CHECK (max_price > 0),
    age_group VARCHAR(50) NOT NULL CHECK (age_group IN ('BABY', 'TODDLER', 'CHILD', 'TEEN', 'YOUNG_ADULT', 'ADULT', 'SENIOR')),
    gender VARCHAR(50) NOT NULL CHECK (gender IN ('MALE', 'FEMALE', 'UNISEX', 'NON_BINARY')),
    interest VARCHAR(50) NOT NULL CHECK (interest IN ('SPORTS', 'MUSIC', 'READING', 'COOKING', 'PHOTOGRAPHY', 'GARDENING', 'TECHNOLOGY', 'TRAVEL', 'ART', 'FASHION', 'FITNESS', 'GAMING', 'MOVIES', 'CRAFTS', 'SCIENCE', 'OUTDOORS', 'COLLECTING', 'BEAUTY')),
    occasion VARCHAR(50) NOT NULL CHECK (occasion IN ('BIRTHDAY', 'WEDDING', 'ANNIVERSARY', 'GRADUATION', 'CHRISTMAS', 'VALENTINES_DAY', 'MOTHERS_DAY', 'FATHERS_DAY', 'EASTER', 'NEW_YEAR', 'THANKSGIVING', 'BABY_SHOWER', 'BRIDAL_SHOWER', 'HOUSEWARMING', 'RETIREMENT', 'GET_WELL', 'THANK_YOU', 'JUST_BECAUSE')),
    relationship VARCHAR(50) NOT NULL CHECK (relationship IN ('FAMILY', 'FRIEND', 'COLLEAGUE', 'ROMANTIC_PARTNER', 'ACQUAINTANCE', 'EXTENDED_FAMILY', 'NEIGHBOR', 'MENTOR_STUDENT', 'CLIENT', 'BOSS')),
    personality_type VARCHAR(50) NOT NULL CHECK (personality_type IN ('EXTROVERT', 'INTROVERT', 'ADVENTUROUS', 'CREATIVE', 'ANALYTICAL', 'PRACTICAL', 'NURTURING', 'COMPETITIVE', 'RELAXED', 'INTELLECTUAL', 'PLAYFUL', 'SOPHISTICATED', 'MINIMALIST', 'TRADITIONAL', 'MODERN')),
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP WITH TIME ZONE,
    
    -- Ensure max_price is greater than min_price
    CONSTRAINT check_price_range CHECK (max_price >= min_price)
);

-- Create indexes for efficient querying on filtering attributes
CREATE INDEX idx_gift_suggestions_age_group ON gift_suggestions(age_group);
CREATE INDEX idx_gift_suggestions_gender ON gift_suggestions(gender);
CREATE INDEX idx_gift_suggestions_interest ON gift_suggestions(interest);
CREATE INDEX idx_gift_suggestions_occasion ON gift_suggestions(occasion);
CREATE INDEX idx_gift_suggestions_relationship ON gift_suggestions(relationship);
CREATE INDEX idx_gift_suggestions_personality_type ON gift_suggestions(personality_type);
CREATE INDEX idx_gift_suggestions_price_range ON gift_suggestions(min_price, max_price);
CREATE INDEX idx_gift_suggestions_created_date ON gift_suggestions(created_date);

-- Composite indexes for common filtering combinations
CREATE INDEX idx_gift_suggestions_age_gender ON gift_suggestions(age_group, gender);
CREATE INDEX idx_gift_suggestions_occasion_relationship ON gift_suggestions(occasion, relationship);
CREATE INDEX idx_gift_suggestions_interest_personality ON gift_suggestions(interest, personality_type);