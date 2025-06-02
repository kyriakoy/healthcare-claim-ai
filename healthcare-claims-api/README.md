# Healthcare Claims Adjudication API

A Spring Boot backend service that provides AI-powered healthcare claims adjudication using Large Language Models (LLMs). This API processes claims through intelligent rule validation, member history analysis, anomaly detection, and generates comprehensive adjudication notes with recommendations.

## 🚀 Quick Start

### Prerequisites
- **Java 21** or higher
- **Maven 3.6+**
- **OpenAI API Key** (for AI functionality)

### Installation & Setup

1. **Clone and navigate to the API directory:**
   ```bash
   cd healthcare-claims-api
   ```

2. **Configure OpenAI API Key:**
   
   Edit `src/main/resources/application.properties` and add your OpenAI API key:
   ```properties
   spring.ai.openai.api-key=your-api-key-here
   ```

3. **Build the project:**
   ```bash
   mvn clean package
   ```

4. **Run the application:**
   ```bash
   java -jar target/healthcare-claims-api-0.0.1-SNAPSHOT.jar
   ```
   
   Or run directly with Maven:
   ```bash
   mvn spring-boot:run
   ```

5. **Verify the service is running:**
   
   The API will be available at `http://localhost:8080`
   
   Test with a simple health check:
   ```bash
   curl http://localhost:8080/api/ai/chat -X POST \
     -H "Content-Type: application/json" \
     -d '{"prompt": "Hello, are you working?"}'
   ```

## 🏗️ Architecture Overview

The API implements a sophisticated multi-step AI workflow for claims adjudication:

### Core Components

- **Controllers**: REST endpoints for external integration
- **Services**: Business logic and AI orchestration
- **Entities**: Data models for claims, members, rules, etc.
- **Repositories**: Data access layer with JPA
- **AI Integration**: Spring AI with OpenAI for LLM processing

### AI Workflow Pipeline

1. **Rule Validation** (`ClaimValidationService`)
   - Individual LLM prompts for each adjudication rule
   - Contextual validation with claim and member data
   - Structured YES/NO responses with explanations

2. **Member History Analysis** (`MemberHistoryService`)
   - AI-powered summarization of member's claim history
   - Pattern recognition and trend analysis

3. **Anomaly Detection** (`AnomalyDetectionService`)
   - Business logic and optional AI-based anomaly detection
   - Fraud pattern identification

4. **Adjudication Note Generation** (`AdjudicationNoteService`)
   - Comprehensive AI-generated adjudication notes
   - Action recommendations (approve, deny, pend)
   - Audit trail of decision factors

## 📡 API Endpoints

### Core Adjudication Endpoints

#### Trigger Full Adjudication Workflow
```http
POST /api/claims/assist-adjudication
Content-Type: application/json

{
  "claimId": "string"
}
```

**Response:**
```json
{
  "claimId": "string",
  "validationResults": [
    {
      "ruleId": "string",
      "ruleName": "string",
      "isValid": boolean,
      "explanation": "string"
    }
  ],
  "memberHistorySummary": "string",
  "anomalyFlags": [
    {
      "type": "string",
      "description": "string",
      "severity": "string"
    }
  ],
  "suggestedAction": "string",
  "adjudicationNote": "string"
}
```

#### View AI Prompts (Transparency)
```http
GET /api/claims/{claimId}/adjudication-note-prompt
```
Returns the actual prompt sent to the LLM for the adjudication note.

```http
GET /api/claims/{claimId}/prompt
```
Returns the prompt for individual rule validation (demo endpoint).

### AI Chat Endpoint
```http
POST /api/ai/chat
Content-Type: application/json

{
  "prompt": "Your question or prompt here"
}
```

**Response:**
```json
{
  "response": "AI-generated response"
}
```

## 🗄️ Data Models

### Core Entities

- **Claim**: Healthcare claim with procedures, amounts, dates
- **Member**: Patient/member information and history
- **Provider**: Healthcare provider details
- **AdjudicationRule**: Configurable business rules for validation
- **ClaimProcessingLog**: Audit trail of processing steps

### Sample Data

The application includes sample data loaded at startup for testing:
- Pre-configured adjudication rules
- Sample claims and members
- Provider information

## ⚙️ Configuration

### Application Properties

```properties
# OpenAI Configuration (Required)
spring.ai.openai.api-key=your-api-key-here

# Database Configuration (H2 for development)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Server Configuration
server.port=8080
```

### Environment Variables

You can also configure using environment variables:
```bash
export SPRING_AI_OPENAI_API_KEY=your-api-key-here
export SERVER_PORT=8080
```

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn verify
```

### Manual Testing with Sample Data

The application loads sample data on startup. You can test with:

1. **Sample Claim ID**: Use the pre-loaded claim IDs from the console output
2. **Test Adjudication**:
   ```bash
   curl -X POST http://localhost:8080/api/claims/assist-adjudication \
     -H "Content-Type: application/json" \
     -d '{"claimId": "CLAIM001"}'
   ```

## 🔧 Development

### Project Structure
```
src/
├── main/java/com/example/healthcareclaims/
│   ├── controller/          # REST controllers
│   ├── service/            # Business logic
│   ├── entity/             # JPA entities
│   ├── repository/         # Data repositories
│   ├── dto/                # Data transfer objects
│   ├── config/             # Configuration classes
│   └── HealthcareClaimsApiApplication.java
├── main/resources/
│   ├── application.properties
│   └── data.sql            # Sample data
└── test/                   # Unit and integration tests
```

### Key Services

- **`ClaimAdjudicationService`**: Orchestrates the full adjudication workflow
- **`AIPromptService`**: Manages LLM interactions and prompt generation
- **`ClaimValidationService`**: Handles rule-based validation
- **`AnomalyDetectionService`**: Detects suspicious patterns
- **`AdjudicationNoteService`**: Generates final adjudication notes

### Adding New Adjudication Rules

1. Create rule in database or via `DataLoader`
2. Rules are automatically picked up by `ClaimValidationService`
3. Each rule generates a separate LLM prompt for validation

### Customizing AI Prompts

Modify prompt templates in:
- `AIPromptService.generateRuleValidationPrompt()`
- `AdjudicationNoteService.buildAdjudicationNotePrompt()`

## 🚀 Deployment

### Production Considerations

1. **Database**: Replace H2 with production database (PostgreSQL, MySQL)
2. **Security**: Add authentication and authorization
3. **Monitoring**: Add health checks and metrics
4. **Logging**: Configure structured logging
5. **API Rate Limiting**: Implement rate limiting for OpenAI calls

### Docker Deployment

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/healthcare-claims-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
docker build -t healthcare-claims-api .
docker run -p 8080:8080 -e SPRING_AI_OPENAI_API_KEY=your-key healthcare-claims-api
```

## 🔍 Monitoring & Debugging

### Health Checks
- Spring Boot Actuator endpoints available at `/actuator`
- Health check: `GET /actuator/health`

### Logging
- AI prompt/response logging for debugging
- Claim processing audit trail
- Error logging with stack traces

### Performance Considerations
- LLM calls are the primary performance bottleneck
- Consider caching for repeated rule validations
- Implement async processing for large claim volumes

## 🤝 Contributing

1. Follow Spring Boot best practices
2. Add unit tests for new services
3. Update API documentation for new endpoints
4. Test AI integrations thoroughly
5. Consider prompt engineering best practices

## 📚 Additional Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [OpenAI API Documentation](https://platform.openai.com/docs)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)

---

**[← Back to Main Project](../README.md)** 