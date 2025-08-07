# MRPConfigPrototype

A Java 17 Swing application for MRP (Material Requirements Planning) configuration.

## Overview

This application provides a graphical user interface for managing MRP configurations with the following features:

- **Scenario Management**: Complete scenario lifecycle with dropdown selection and creation functionality
- **Scenario Creation**: User-friendly dialog for creating new scenarios with validation
- **Configuration Saving**: Save technical configurations via REST API with status confirmation
- **Dynamic Configuration UI**: Automatically generated form fields based on configuration items from API
- **Configuration Areas**: Two vertically scrollable and resizable areas for technical and operational configurations
- **REST Integration**: Full CRUD operations for scenarios and configurations via mock APIs
- **Fallback Mechanism**: Graceful handling when APIs are unavailable with example data
- **Modern UI**: Icons, intuitive dialogs, and professional user experience

## Key Features in Detail

### Scenario Management
- **Scenario Selection**: Dropdown menu with all available scenarios loaded via REST API
- **Scenario Creation**: Professional dialog for creating new scenarios
  - Single unified dialog with both Szenario-ID and Szenario-Name input fields
  - Input validation with user-friendly error messages
  - Automatic scenario list refresh after successful creation
  - HTTP 201 status code validation for successful scenario creation
  - Graceful handling of user cancellation and empty inputs

### Configuration Management  
- **Technical Configuration Saving**: Complete save functionality for technical settings
  - Collects all modified values from UI components automatically
  - Supports multiple field types: string, password, and boolean
  - Sends changes as JSON array to PUT /config/technical endpoint
  - Comprehensive success/error feedback with detailed user messages
  - Password field security with memory cleanup after use

## Technical Details

- **Java Version**: 17
- **Build Tool**: Maven
- **GUI Framework**: Swing
- **REST Client**: RESTEasy (JAX-RS Client Implementation)
- **JSON Processing**: Jackson (automatically detected)
- **Logging**: SLF4J with Logback implementation
- **Testing**: JUnit 5 + Mockito
- **Architecture**: Clean service-oriented architecture with dependency injection

## Project Structure

```
src/main/java/com/apag/p2plus/management/plugins/mrp/
├── MRPConfigApplication.java         # Main application class
├── model/
│   ├── Scenario.java                # Data model for scenarios
│   └── ConfigItem.java              # Data model for configuration items
├── service/
│   ├── MRPConfigClient.java         # JAX-RS Client Interface for REST API calls
│   ├── BaseConfigService.java       # Abstract base service with RESTEasy client
│   ├── ServiceFactory.java          # Factory for creating service instances
│   ├── ScenarioService.java         # Service for scenario management
│   ├── TechnicalConfigService.java  # Service for technical configuration
│   └── OperationalConfigService.java# Service for operational configuration
└── ui/
    └── MRPConfigPanel.java          # Main UI panel with dynamic form generation

src/test/java/                       # Unit tests with JUnit 5 and Mockito
src/main/resources/
└── logback.xml                      # Logging configuration
```

## Installation and Startup

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Key Dependencies

- **RESTEasy Client**: JAX-RS client implementation
- **Jackson**: JSON serialization/deserialization (auto-detected)
- **Jakarta WS-RS API**: JAX-RS standard annotations
- **Logback**: Logging framework with advanced configuration
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework for tests
- **Swing**: GUI framework (part of JDK)

### Build Project

```bash
mvn clean compile
```

### Run Tests

```bash
mvn test
```

### Start Application

```bash
mvn exec:java
```

Or alternatively:

```bash
mvn exec:java -Dexec.mainClass="com.apag.p2plus.management.plugins.mrp.MRPConfigApplication"
```

Or using the provided batch file:
```bash
run.bat
```

## UI Layout

The application is divided into three main areas:

1. **Toolbar (top)**: Contains the scenario selection dropdown and action buttons:
   - ➕ **Create Scenario**: Create new scenarios via intuitive dialog (Szenario-ID and Szenario-Name)
   - 💾 **Save**: Save current technical configuration changes
2. **Technical Config (middle)**: Dynamically generated form fields for technical configuration
3. **Operational Config (bottom)**: Dynamically generated form fields for operational configuration (scenario-specific)

The bottom two areas are separated by a resizable splitter and automatically generate appropriate UI components based on the configuration item types (text fields, password fields, checkboxes).

## REST API Integration

The application integrates with four REST APIs:

### 1. Scenarios API

**Get all scenarios:**
```
GET https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io/config/scenarios
```

**Create new scenario:**
```
POST https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io/config/scenarios
```

**Expected JSON format for GET response:**
```json
[
    {
        "scenarioId": "Standard_LDL_M1000",
        "description": "Production run Client 1000"
    },
    {
        "scenarioId": "Test_Mandant_3000", 
        "description": "Test scenario for Client 3000"
    }
]
```

**Expected JSON format for POST request:**
```json
{
    "scenarioId": "New_Scenario",
    "description": "This is the new scenario"
}
```

The POST request returns HTTP status code 201 on successful creation.

### 2. Technical Configuration API

**Get technical configuration:**
```
GET https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io/config/technical
```

**Save technical configuration:**
```
PUT https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io/config/technical
```

### 3. Operational Configuration API
```
GET https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io/config/operational/{scenarioId}
```

**Expected JSON format for GET configuration APIs:**
```json
[
    {
        "name": "datasourceUrl",
        "type": "string",
        "value": "jdbc:mssql://localhost:5432/mydb",
        "description": "Datasource URL"
    },
    {
        "name": "enableLogging",
        "type": "boolean", 
        "value": true,
        "description": "Enable logging"
    },
    {
        "name": "password",
        "type": "password",
        "value": null,
        "description": "Database password"
    }
]
```

**Expected JSON format for PUT technical configuration (save):**
```json
[
    {
        "name": "datasourceUrl",
        "type": "string",
        "value": "jdbc:mssql://localhost:5432/mydb-new"
    },
    {
        "name": "datasourceDriver",
        "type": "string",
        "value": "mssql-new"
    },
    {
        "name": "datasourceUsername",
        "type": "string",
        "value": "admin-new"
    },
    {
        "name": "datasourcePassword",
        "type": "password",
        "value": "password-new"
    },
    {
        "name": "datasourceDebug",
        "type": "boolean",
        "value": false
    }
]
```

## Dynamic UI Generation

The application automatically generates appropriate UI components based on configuration item types:

- **string**: Text field
- **password**: Password field (masked input)
- **boolean**: Checkbox
- **unknown types**: Default to text field

## Fallback Behavior

If any REST API is not available, example configurations are automatically loaded:

- **Scenarios**: 2 example scenarios
- **Technical Config**: 5 example database configuration items
- **Operational Config**: 3 example processing configuration items

## Architecture

### Clean Architecture Principles

The application follows modern clean code principles:

- **Factory Pattern**: Services are managed via ServiceFactory singleton
- **Single Responsibility**: Each service has one clear purpose
- **Separation of Concerns**: UI, Service, and Model layers are clearly separated
- **Async Operations**: Non-blocking UI with CompletableFuture
- **Thread Safety**: All UI updates happen on the Event Dispatch Thread
- **Resource Management**: Proper cleanup of REST clients

### Service Layer Architecture

- **ServiceFactory**: Singleton factory for managing service instances
- **BaseConfigService<T>**: Abstract base class providing RESTEasy client infrastructure
- **ScenarioService**: Handles scenario loading and creation with fallback
- **TechnicalConfigService**: Manages technical configuration loading and saving
- **OperationalConfigService**: Manages scenario-specific operational configuration
- **MRPConfigClient**: JAX-RS interface defining all REST API endpoints (GET, POST, PUT)

### Benefits

- **Testability**: Services can be easily mocked for unit testing via factory
- **Maintainability**: Clean separation enables easy modification
- **Extensibility**: New configuration types can be added easily
- **Performance**: Asynchronous loading keeps UI responsive
- **Reliability**: Fallback configurations ensure application always works

## Testing

The application includes comprehensive unit tests:

- **Service Testing**: All services are tested with mocked dependencies
- **Error Handling**: API failures and fallback scenarios are tested
- **Async Operations**: CompletableFuture-based operations are properly tested

Run tests with:
```bash
mvn test
```

## Development

### Code Quality

- **2-space indentation**: Following .editorconfig standards
- **Consistent naming**: English language throughout
- **Structured logging**: Logback with console and file appenders
- **Modern Java**: Leveraging Java 17 features
- **Clean imports**: No unused dependencies

### Logging Configuration

The application uses Logback for structured logging:

- **Console output**: For development and debugging
- **File logging**: Logs stored in `logs/mrp-config.log`
- **Rolling policy**: Daily rotation with size limits (10MB per file)
- **Log levels**: Configurable per package (default INFO for application)

### Error Handling

- **Graceful degradation**: Fallback when APIs are unavailable
- **User-friendly messages**: Clear error dialogs without technical details
- **Proper logging**: Structured logging for debugging

## License

This project is a prototype for demonstration purposes. 