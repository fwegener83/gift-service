# Gift Suggestion Service - Spring Boot REST API

Erstelle einen vollständigen Spring Boot REST API Service für die Verwaltung von Geschenkvorschlägen mit folgenden Anforderungen:

## Technische Basis
- Spring Boot 3.2.x mit Java 17+
- PostgreSQL als Datenbank
- TestContainers für Integration Tests
- Maven als Build-Tool
- OpenAPI/Swagger Dokumentation (springdoc-openapi-starter-webmvc-ui)
- Bean Validation für Eingabevalidierung
- 3-Layer-Architektur (Controller → Service → Repository)

## Domain Model

### Geschenkvorschlag (GiftSuggestion)
- ID (UUID)
- Name/Titel
- Beschreibung
- Preisrange (minPrice, maxPrice in Euro)
- Kategorien und Attribute als ENUMs:
  - **AgeGroup**: TEENS_13_17, YOUNG_ADULTS_18_25, ADULTS_26_35, MIDDLE_AGED_36_50, SENIORS_50_PLUS
  - **Gender**: MALE, FEMALE, UNISEX
  - **Interest**: TECHNOLOGY, SPORTS, WELLNESS, CULINARY, CULTURE, HOBBY, FASHION, BOOKS, MUSIC, OUTDOOR, INDOOR, CRAFTS
  - **Occasion**: BIRTHDAY, CHRISTMAS, WEDDING, ANNIVERSARY, VALENTINES_DAY, MOTHERS_DAY, FATHERS_DAY, GRADUATION, GENERAL
  - **Relationship**: PARTNER, FAMILY, FRIENDS, COLLEAGUES, ACQUAINTANCES
  - **PersonalityType**: PRACTICAL, CREATIVE, SPORTY, RELAXED, ADVENTUROUS, INTELLECTUAL, SOCIAL
- Erstellungs- und Aktualisierungszeitpunkt
- One-to-Many Beziehung zu konkreten Geschenken

### Konkretes Geschenk (ConcreteGift)
- ID (UUID)
- Produktname
- Beschreibung
- Exakter Preis (in Euro)
- Anbieter/Shop Name
- Produkt-URL
- Erstellungs- und Aktualisierungszeitpunkt
- Many-to-One Beziehung zu Geschenkvorschlag

## REST API Endpoints

### GiftSuggestion Controller
- `GET /api/v1/gift-suggestions` - Alle Geschenkvorschläge abrufen mit optionalen Query-Parametern für Filterung
- `GET /api/v1/gift-suggestions/{id}` - Einzelnen Geschenkvorschlag abrufen
- `POST /api/v1/gift-suggestions` - Neuen Geschenkvorschlag erstellen
- `PUT /api/v1/gift-suggestions/{id}` - Geschenkvorschlag aktualisieren
- `DELETE /api/v1/gift-suggestions/{id}` - Geschenkvorschlag löschen
- `GET /api/v1/gift-suggestions/search` - Erweiterte Suche mit Kombinationen von Attributen

### ConcreteGift Controller
- `GET /api/v1/concrete-gifts` - Alle konkreten Geschenke abrufen
- `GET /api/v1/concrete-gifts/{id}` - Einzelnes konkretes Geschenk abrufen
- `POST /api/v1/concrete-gifts` - Neues konkretes Geschenk erstellen
- `PUT /api/v1/concrete-gifts/{id}` - Konkretes Geschenk aktualisieren
- `DELETE /api/v1/concrete-gifts/{id}` - Konkretes Geschenk löschen
- `GET /api/v1/gift-suggestions/{suggestionId}/concrete-gifts` - Alle konkreten Geschenke zu einem Vorschlag

## Implementierungsdetails

### Validierung
- Umfassende Bean Validation Annotationen
- Custom Validatoren wo sinnvoll (z.B. Preisrange-Validierung)
- Globale Exception Handler für saubere Fehlerbehandlung

### DTOs
- Separate Request- und Response-DTOs
- Mapping zwischen Entities und DTOs (MapStruct wäre nice, aber manuell ist auch OK)

### Datenbank
- JPA Entities mit Hibernate
- Liquibase oder Flyway für Database Migrations
- Repository Pattern mit Spring Data JPA
- Custom Queries für komplexe Suchen

### Testing
- Unit Tests für Services
- Integration Tests mit TestContainers (PostgreSQL)
- Controller Tests mit MockMvc
- Testdaten mit @Sql oder TestData Builder Pattern

### Configuration
- application.yml für verschiedene Profile (dev, test, prod)
- PostgreSQL Configuration
- OpenAPI Configuration
- Logging Configuration

## Zusätzliche Features
- CORS Configuration für Frontend-Integration
- Actuator für Health Checks
- Pagination für Listen-Endpoints
- Sorting-Möglichkeiten für Ergebnisse

## Projektstruktur
```
src/main/java/com/giftservice/
├── GiftServiceApplication.java
├── config/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── enums/
├── exception/
└── validation/

src/main/resources/
├── application.yml
├── application-dev.yml
├── application-test.yml
└── db/migration/

src/test/java/
├── integration/
├── controller/
└── service/
```

Erstelle einen vollständig funktionsfähigen Service mit allen genannten Features. Achte auf Clean Code, ausführliche Kommentare und eine professionelle Struktur. Der Service soll sofort lauffähig sein und als solide Basis für Erweiterungen dienen.