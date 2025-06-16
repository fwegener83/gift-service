# Development Guide - Gift Service

## Prerequisites

- Java 17+
- Maven 3.6+
- Docker (für PostgreSQL)

## Quick Start (Empfohlene Methode)

### 1. PostgreSQL Datenbank starten

```bash
docker run --name gift-service-postgres -e POSTGRES_PASSWORD=giftservice -e POSTGRES_USER=giftservice -e POSTGRES_DB=giftservice_dev -p 5432:5432 -d postgres:15
```

### 2. Warten bis DB bereit ist (optional)

```bash
sleep 5
```

### 3. Spring Boot Service starten

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Service verfügbar unter:

- **API Base**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Health Check**: http://localhost:8080/actuator/health

## Development Features

- **Live Reload**: Application restarts automatically on code changes
- **SQL Logging**: All SQL queries logged in development mode
- **Debug Logging**: Detailed logging for application components
- **CORS Enabled**: Frontend development support

## API Testing

### Quick Tests

```bash
curl http://localhost:8080/actuator/health
```

```bash
curl http://localhost:8080/api/v1/gift-suggestions
```

### Mit api.http File

Use the included `api.http` file for comprehensive testing:
- **VS Code**: Install REST Client extension
- **IntelliJ**: Built-in HTTP client support
- **Postman**: Import the examples

## Tests ausführen

```bash
mvn clean test
```

## Service stoppen

```bash
pkill -f "GiftServiceApplication"
```

```bash
docker stop gift-service-postgres && docker rm gift-service-postgres
```

## Configuration Profiles

- **dev**: Standard development with manual PostgreSQL setup
- **test**: H2 in-memory database for testing
- **prod**: Production configuration

## Troubleshooting

### Port Conflicts

**Port 8080 belegt:**
```bash
lsof -ti:8080 | xargs kill -9
```

**Port 5432 belegt:**
```bash
docker stop gift-service-postgres && docker rm gift-service-postgres
```

**Alternative Port für Service:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dspring-boot.run.jvmArguments="-Dserver.port=8081"
```

### Docker Issues

**Docker Status prüfen:**
```bash
docker ps
```

**Container Logs prüfen:**
```bash
docker logs gift-service-postgres
```

**PostgreSQL Image aktualisieren:**
```bash
docker pull postgres:15
```

**Alte Container aufräumen:**
```bash
docker rm -f gift-service-postgres
```

### Database Connection Issues

**Container neu starten:**
```bash
docker restart gift-service-postgres
```

**Testcontainers zurücksetzen:**
```bash
rm -rf ~/.testcontainers
```

## Nützliche Commands

### Build ohne Tests

```bash
mvn clean compile
```

### Nur Tests für bestimmte Klasse

```bash
mvn test -Dtest=GiftSuggestionControllerTest
```

### Package erstellen

```bash
mvn clean package
```

### Jar direkt ausführen

```bash
java -jar target/gift-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

## Development Workflow

1. **PostgreSQL starten** (einmalig pro Session)
2. **Service starten** mit Maven
3. **API testen** mit Swagger UI oder api.http
4. **Tests ausführen** vor Commits
5. **Service stoppen** wenn fertig