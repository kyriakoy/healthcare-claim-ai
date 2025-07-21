# Healthcare Claims Adjudication System

A comprehensive AI-powered healthcare claims adjudication system that leverages Large Language Models (LLMs) to automate and assist in the claims processing workflow. The system provides intelligent rule validation, anomaly detection, and adjudication recommendations to streamline healthcare claims processing.

## 🏗️ System Architecture

This project consists of two main components:

### 🔧 Backend API (`healthcare-claims-api/`)
A Spring Boot application that provides the core adjudication logic and AI-powered processing capabilities.

**Key Features:**
- **AI-Powered Rule Validation**: Uses LLMs to validate claims against configurable adjudication rules
- **Member History Analysis**: Intelligent summarization of member claim history
- **Anomaly Detection**: Automated detection of suspicious patterns in claims
- **Adjudication Note Generation**: AI-generated comprehensive adjudication notes with recommendations
- **RESTful API**: Clean API endpoints for integration with external systems

**Technology Stack:**
- Java 21
- Spring Boot 3.2.4
- Spring AI with OpenAI integration
- Spring Data JPA
- H2 Database (for development)
- Maven

### 🎨 Frontend UI (`adjudication-explorer/`)
A modern React-based web application that provides an intuitive interface for claims adjudication workflows.

**Key Features:**
- **Claims Dashboard**: Overview of claims requiring adjudication
- **Interactive Rule Validation**: Visual representation of rule validation results
- **AI Prompt Inspection**: View the actual prompts sent to LLMs for transparency
- **Adjudication Workflow**: Guided interface for claims processing
- **Real-time Updates**: Live updates on claim processing status

**Technology Stack:**
- React 19
- Vite (build tool)
- Modern ES6+ JavaScript
- Responsive design

## 🚀 Quick Start

### Prerequisites
- **Java 21** (for backend)
- **Node.js 18+** (for frontend)
- **Maven 3.6+** (for backend build)
- **OpenAI API Key** (for AI functionality)

### 1. Backend Setup
```bash
cd healthcare-claims-api
# Copy the example properties file and edit it with your settings
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Configure your OpenAI API key and other settings in src/main/resources/application.properties
mvn clean package
java -jar target/healthcare-claims-api-0.0.1-SNAPSHOT.jar
```
The API will be available at `http://localhost:8080`

### 2. Frontend Setup
```bash
cd adjudication-explorer
npm install
npm run dev
```
The UI will be available at `http://localhost:5173`

## 📚 Documentation

- **[API Documentation](healthcare-claims-api/README.md)** - Detailed backend setup, configuration, and API reference
- **[UI Documentation](adjudication-explorer/README.md)** - Frontend setup, development guide, and component overview

## 🔄 AI Workflow

The system implements a sophisticated multi-step AI workflow:

1. **Rule Validation**: Each adjudication rule is validated individually using targeted LLM prompts
2. **History Analysis**: Member claim history is summarized using AI for context
3. **Anomaly Detection**: Suspicious patterns are identified through business logic and AI analysis
4. **Final Adjudication**: All findings are synthesized into a comprehensive AI-generated note with recommendations

## 🛠️ Development

### Project Structure
```
healthcare-claim-ai/
├── healthcare-claims-api/     # Spring Boot backend
│   ├── src/main/java/        # Java source code
│   ├── src/main/resources/   # Configuration files
│   └── README.md            # API documentation
├── adjudication-explorer/    # React frontend
│   ├── src/                 # React components and logic
│   ├── public/              # Static assets
│   └── README.md           # UI documentation
└── README.md               # This file
```

### Key API Endpoints
- `POST /api/claims/assist-adjudication` - Trigger full adjudication workflow
- `GET /api/claims/{claimId}/adjudication-note-prompt` - View AI prompts for transparency
- `GET /api/claims/{claimId}/prompt` - View individual rule validation prompts

## 🔧 Configuration

### Backend Configuration
- Edit your configuration in `healthcare-claims-api/src/main/resources/application.properties` (not committed to git)
- Use `application.properties.example` as a template for new environments or developers

Configure the following in your `application.properties` file:
```properties
spring.ai.openai.api-key=your-openai-api-key-here
```

### Frontend Configuration
The frontend automatically connects to the backend API running on `localhost:8080`.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---
