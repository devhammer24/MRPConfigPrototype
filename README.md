# MRPConfigPrototype

A Java 17 Swing application for MRP (Material Requirements Planning) configuration.

## Overview

This application provides a graphical user interface for managing MRP configurations with the following features:

- **Scenario Selection**: Dropdown menu with scenarios loaded via REST API
- **Dynamic Configuration UI**: Automatically generated form fields based on configuration items from API
- **Configuration Areas**: Two vertically scrollable and resizable areas for technical and operational configurations
- **REST Integration**: Automatic loading of scenarios, technical and operational configurations from mock APIs
- **Fallback Mechanism**: Graceful handling when APIs are unavailable with example data
- **Modern UI**: Icons and intuitive button design for better user experience

## Technical Details

- **Java Version**: 17
- **Build Tool**: Maven
- **GUI Framework**: Swing
- **REST Client**: RESTEasy (JAX-RS Client Implementation)
- **JSON Processing**: Jackson (automatically detected)
- **Logging**: SLF4J with Simple implementation
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
│   ├── ScenarioService.java         # Service for scenario management
│   ├── TechnicalConfigService.java  # Service for technical configuration
│   └── OperationalConfigService.java# Service for operational configuration
└── ui/
    └── MRPConfigPanel.java          # Main UI panel with dynamic form generation

src/test/java/                       # Unit tests with JUnit 5 and Mockito
```

## Installation and Startup

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Key Dependencies

- **RESTEasy Client**: JAX-RS client implementation
- **Jackson**: JSON serialization/deserialization (auto-detected)
- **Jakarta WS-RS API**: JAX-RS standard annotations
- **SLF4J**: Logging framework
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
   - ➕ **Create Scenario**: Create new scenarios
   - 💾 **Save**: Save current configuration
2. **Technical Config (middle)**: Dynamically generated form fields for technical configuration
3. **Operational Config (bottom)**: Dynamically generated form fields for operational configuration (scenario-specific)

The bottom two areas are separated by a resizable splitter and automatically generate appropriate UI components based on the configuration item types (text fields, password fields, checkboxes).

## REST API Integration

The application integrates with three REST APIs:

### 1. Scenarios API
```
GET https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io/config/scenarios
```

**Expected JSON format:**
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

### 2. Technical Configuration API
```
GET https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io/config/technical
```

### 3. Operational Configuration API
```
GET https://80ab19d6-cfd5-42b4-889e-714dd9f0d184.mock.pstmn.io/config/operational/{scenarioId}
```

**Expected JSON format for configuration APIs:**
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

- **Dependency Injection**: Services are injected via constructor
- **Single Responsibility**: Each service has one clear purpose
- **Separation of Concerns**: UI, Service, and Model layers are clearly separated
- **Async Operations**: Non-blocking UI with CompletableFuture
- **Thread Safety**: All UI updates happen on the Event Dispatch Thread
- **Resource Management**: Proper cleanup of REST clients

### Service Layer Architecture

- **BaseConfigService<T>**: Abstract base class providing RESTEasy client infrastructure
- **ScenarioService**: Handles scenario loading with fallback
- **TechnicalConfigService**: Manages technical configuration
- **OperationalConfigService**: Manages scenario-specific operational configuration
- **MRPConfigClient**: JAX-RS interface defining all REST API endpoints

### Benefits

- **Testability**: Services can be easily mocked for unit testing
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
- **Minimal logging**: Only essential warnings and errors
- **Modern Java**: Leveraging Java 17 features
- **Clean imports**: No unused dependencies

### Error Handling

- **Graceful degradation**: Fallback when APIs are unavailable
- **User-friendly messages**: Clear error dialogs without technical details
- **Proper logging**: Structured logging for debugging

## Future Extensions

- **Configuration Persistence**: Save and load configurations to/from file
- **Validation**: Real-time validation of configuration values
- **Configuration Comparison**: Compare different scenarios
- **Export/Import**: JSON/XML export and import functionality
- **User Management**: Authentication and user-specific configurations
- **Internationalization**: Multi-language support
- **Configuration Templates**: Pre-defined configuration templates
- **Advanced UI**: More sophisticated form controls and validation

## License

This project is a prototype for demonstration purposes. 