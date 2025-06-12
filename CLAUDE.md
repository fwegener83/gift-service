# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.2.x REST API service for managing gift suggestions, built with Java 17+, PostgreSQL, and Maven. The service follows a 3-layer architecture (Controller → Service → Repository) and implements a comprehensive gift suggestion system with detailed categorization.

## Core Architecture

### Domain Model
- **GiftSuggestion**: Main entity with UUID, name, description, price range, and multiple enum attributes (AgeGroup, Gender, Interest, Occasion, Relationship, PersonalityType)
- **ConcreteGift**: Specific gift implementations linked to suggestions with exact prices and vendor information
- One-to-Many relationship: GiftSuggestion → ConcreteGift

### Package Structure
```
com.giftservice/
├── config/          # Spring configuration classes
├── controller/      # REST endpoints (v1 API)
├── service/         # Business logic layer
├── repository/      # Data access layer with Spring Data JPA
├── entity/          # JPA entities (GiftSuggestion, ConcreteGift)
├── dto/             # Request/Response DTOs
├── enums/           # Domain enums (AgeGroup, Gender, Interest, etc.)
├── exception/       # Global exception handling
└── validation/      # Custom validators
```

## Development Commands

### Build and Run
```bash
mvn clean install                    # Build project
mvn spring-boot:run                  # Run application
mvn test                            # Run all tests
mvn test -Dtest=ClassName           # Run specific test class
```

### Database
- Uses PostgreSQL with TestContainers for integration tests
- Database migrations with Liquibase/Flyway in `src/main/resources/db/migration/`
- Profiles: dev, test, prod configured in application.yml files

### Testing Strategy
- Unit tests for service layer business logic
- Integration tests with TestContainers (PostgreSQL)
- Controller tests with MockMvc
- Use @Sql annotations or TestData Builder pattern for test data

## API Endpoints

### Gift Suggestions
- `GET /api/v1/gift-suggestions` - List with filtering/pagination
- `GET /api/v1/gift-suggestions/search` - Advanced search with attribute combinations
- Full CRUD operations on `/api/v1/gift-suggestions/{id}`

### Concrete Gifts  
- Full CRUD operations on `/api/v1/concrete-gifts/{id}`
- `GET /api/v1/gift-suggestions/{suggestionId}/concrete-gifts` - Related gifts

## Key Implementation Details

### Validation
- Bean Validation annotations throughout DTOs
- Custom validators for business rules (e.g., price range validation)
- Global exception handler for consistent error responses

### Configuration
- OpenAPI/Swagger documentation via springdoc-openapi-starter-webmvc-ui
- CORS configuration for frontend integration
- Actuator endpoints for monitoring
- Separate application.yml per environment

### Enums
Critical domain enums define the gift categorization system:
- AgeGroup, Gender, Interest, Occasion, Relationship, PersonalityType
- These drive the filtering and search functionality