# MRPConfigPrototype

A Java 17 Swing application for MRP (Material Requirements Planning) configuration.

## Overview

This application provides a graphical user interface for managing MRP configurations with the following features:

- **Scenario Selection**: Dropdown menu with scenarios loaded via REST API
- **Dynamic Configuration UI**: Automatically generated form fields based on configuration items from API
- **Configuration Areas**: Two vertically scrollable and resizable areas for technical and operational configurations
- **REST Integration**: Automatic loading of scenarios, technical and operational configurations from mock APIs
- **Fallback Mechanism**: Graceful handling when APIs are unavailable with example data

## Technical Details

- **Java Version**: 17
- **Build Tool**: Maven
- **GUI Framework**: Swing
- **HTTP Client**: Apache HttpClient 5
- **JSON Processing**: Jackson
- **Architecture**: Service-oriented with Base Class inheritance

## Project Structure

```
src/main/java/com/apag/p2plus/management/plugins/mrp/
├── MRPConfigApplication.java         # Main application class
├── model/
│   ├── Scenario.java                # Data model for scenarios
│   └── ConfigItem.java              # Data model for configuration items
├── service/
│   ├── BaseConfigService.java       # Abstract base service with common HTTP functionality
│   ├── ScenarioService.java         # Service for scenario management
│   ├── TechnicalConfigService.java  # Service for technical configuration
│   └── OperationalConfigService.java# Service for operational configuration
└── ui/
    └── MRPConfigPanel.java          # Main UI panel with dynamic form generation
```

## Installation and Startup

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Build Project

```bash
mvn clean compile
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

1. **Toolbar (top)**: Contains the scenario selection dropdown and "Create scenario" button
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

### Service Layer Architecture

The application uses a clean service-oriented architecture:

- **BaseConfigService<T>**: Abstract base class providing common HTTP functionality and generic configuration loading
- **ScenarioService**: Extends BaseConfigService for scenario management
- **TechnicalConfigService**: Extends BaseConfigService for technical configuration
- **OperationalConfigService**: Extends BaseConfigService for operational configuration

### Benefits

- **Separation of Concerns**: Each service has a single responsibility
- **DRY Principle**: Common functionality in base class
- **Type Safety**: Generic typing for better compile-time checks
- **Extensibility**: Easy to add new configuration types
- **Maintainability**: Clean, modular architecture

## Development

### Clean Code Principles

- Separation of Concerns (Model, Service, UI)
- Single Responsibility Principle
- Inheritance for code reuse
- Asynchronous API calls with CompletableFuture
- Proper Resource Management (HTTP client cleanup)
- Generic programming for type safety

### Error Handling

- Graceful fallback when APIs are unavailable
- User-friendly error messages
- Asynchronous loading with proper exception handling

## Future Extensions

- **Configuration Persistence**: Save and load configurations to/from file
- **Validation**: Real-time validation of configuration values
- **Configuration Comparison**: Compare different scenarios
- **Export/Import**: JSON/XML export and import functionality
- **User Management**: Authentication and user-specific configurations
- **Internationalization**: Multi-language support
- **Configuration Templates**: Pre-defined configuration templates

## License

This project is a prototype for demonstration purposes. 