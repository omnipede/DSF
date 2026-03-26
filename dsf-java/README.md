# DSF Java Port

**DSF (Det Sentrale Folketrygdsystemet)** - Java port of the Norwegian National Insurance System.

Original system operated 1967-2018, written in PL/I for IBM mainframe with CICS.

## Overview

This project ports the historic Norwegian national insurance system to Java, providing a terminal-based interface that mimics the original CICS screen flow.

### Original System

- **Operational Period:** 1967-2018 (51 years)
- **Owner:** NAV (Norwegian Labour and Welfare Administration)
- **Original Language:** PL/I with CICS
- **Platform:** IBM Mainframe

### Java Port Features

- Terminal-based UI using JLine 3 (CICS-style screens)
- Menu-driven navigation (same as original R0010101-R0010410 flow)
- Person lookup by FNR (Norwegian national ID)
- Pension registration (AP - Age Pension, UP - Disability Pension)
- Transaction processing with rollback support (R0012001)
- User authentication and role-based access control (ACF2 simulation)
- JSON-based persistence layer
- In-memory database with demo data
- FNR validation with checksum algorithm

## Building

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Build Commands

```bash
cd dsf-java

# Compile
mvn compile

# Package
mvn package

# Run tests
mvn test
```

## Running

### Using Maven

```bash
mvn exec:java -Dexec.mainClass="com.nav.dsf.DSFApplication"
```

### Using Java

```bash
# After building
java -jar target/dsf-java-1.0.0.jar
```

### Using IDE

Run the `com.nav.dsf.DSFApplication` class from your IDE.

## Usage

### Demo Credentials

Use any of these demo user IDs:

| User ID | Name | Roles |
|---------|------|-------|
| `DEMO` | Demo Bruker | All roles |
| `JDA2970` | Jon Demo Andersen | AP, UP, FB saksbehandler |
| `SPA7339` | Siri Pettersen Andersen | Senior saksbehandler |
| `TSB2970` | Tone Solberg Berg | AP, UP saksbehandler |

### Navigation

1. **Login Screen:** Enter your user ID
2. **Role Selection:** Choose a role (1-16)
3. **Main Menu:** Select function code:
   - `A` - Administration
   - `F` - Inquiry (Person lookup)
   - `R` - Registration
   - `I` - Income adjustment
   - `V` - Waiting transactions
   - `X` - Exit

### Demo Person Records

Pre-loaded demo FNRs for testing (all pass checksum validation):

| FNR | Name | Birth Date | Gender | Pension Type |
|-----|------|------------|--------|--------------|
| 01010100131 | Ola Nordmann | 1901-01-01 | Male | AP |
| 01010100050 | Kari Nordmann | 1901-01-01 | Female | AP |
| 13050300182 | Per Hansen | 2003-05-13 | Male | UP |
| 13050300263 | Anne Larsen | 2003-05-13 | Female | EP |

## Project Structure

```
dsf-java/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/nav/dsf/
        │       ├── DSFApplication.java           # Main entry point
        │       ├── application/                   # Application layer
        │       │   ├── AppState.java
        │       │   └── DSFApplicationController.java
        │       ├── domain/                        # Domain layer
        │       │   ├── enums/                     # Enumerations
        │       │   │   ├── FamilyRelationshipType.java
        │       │   │   ├── PensionStatusCode.java
        │       │   │   └── PensionType.java
        │       │   ├── model/                     # Entity classes
        │       │   │   ├── AgePension.java
        │       │   │   ├── EarlyRetirementSettlement.java
        │       │   │   ├── FamilyMember.java
        │       │   │   ├── IncomeRecord.java
        │       │   │   ├── PensionStatus.java
        │       │   │   ├── Person.java
        │       │   │   └── SupportSupplement.java
        │       │   ├── repository/                # Data access
        │       │   │   └── MemoryDatabase.java
        │       │   └── service/                   # Business logic
        │       │       ├── PersonService.java
        │       │       └── ValidationService.java
        │       ├── infrastructure/                # Infrastructure layer
        │       │   ├── security/                  # Authentication/Authorization
        │       │   │   ├── AccessControl.java
        │       │   │   ├── Role.java
        │       │   │   ├── User.java
        │       │   │   └── UserService.java
        │       │   └── terminal/                  # Terminal UI
        │       │       ├── InputHandler.java
        │       │       ├── ScreenRenderer.java
        │       │       └── TerminalUI.java
        │       └── common/                        # Common utilities
        │           ├── constants/
        │           │   └── DSFConstants.java
        │           └── util/
        │               ├── DateUtil.java
        │               └── FNRValidator.java
        └── resources/
```

## Architecture Mapping

| Original (PL/I + CICS) | Java Equivalent |
|------------------------|-----------------|
| `EXEC CICS SEND MAP` | `ScreenRenderer.render*()` |
| `EXEC CICS RECEIVE MAP` | `InputHandler.read*()` |
| `EXEC CICS LINK/XCTL` | Method calls / Spring beans |
| `EXEC CICS RETURN TRANSID` | State transition in controller |
| `%INCLUDE copybook` | Java `import` statements |
| `COMMAREA` | Java objects (DTOs) |
| ACF2 security | `AccessControl`, `UserService` |
| database.txt records | Java POJOs |

## Key Components

### Terminal UI (`TerminalUI.java`)
Provides console I/O similar to CICS terminal handling using JLine 3.

### FNR Validator (`FNRValidator.java`)
Implements the Norwegian national ID checksum algorithm (modulo 11).

### Access Control (`AccessControl.java`)
Simulates ACF2 security checks for user authentication and authorization.

### Memory Database (`MemoryDatabase.java`)
In-memory storage for person records, simulating the mainframe database.

## Implemented Features (v1.0.0)

### Core Navigation ✅
- R0010101 - Login/Start
- R0010201 - User ID Validation
- R0010301 - Function Selection
- R0010401 - Registration Menu
- R0010410 - Inquiry Main Program

### Registration ✅
- AP (Alderspensjon) - Age Pension Registration (R0010501)
- UP (Uførepensjon) - Disability Pension Registration (R0010601)
- Transaction Processing (R0012001)

### Inquiry ✅
- Person lookup by FNR
- Display pension information
- Display age, gender, pension type

### Security ✅
- User authentication
- Role-based access control (16 roles)
- ACF2 simulation

### Utilities ✅
- FNR validation with checksum (R0019904)
- Date validation (R0019901)
- Age calculation (R0019905)
- Gender extraction (R0019902)

### Data Persistence ✅
- JSON file repository
- Automatic backup on exit
- Import/Export functionality

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ClassName
```

## Limitations

This is a demonstration port with the following limitations:

1. **Partial pension types:** AP and UP only (EP, FB, BP, FT, YK, AF not implemented)
2. **Simplified calculations:** Pension calculations use simplified formulas
3. **No mainframe integration:** Standalone Java application
4. **Terminal UI only:** No web interface

## Future Enhancements

- [ ] Complete remaining pension types (EP, FB, BP, FT, YK, AF)
- [ ] Full pension calculation logic (G-run, trygdetid, special supplements)
- [ ] Database persistence (H2/PostgreSQL)
- [ ] EEA/EU pro-rata calculations
- [ ] Report generation
- [ ] Export to JSON/CSV
- [ ] Web UI option
- [ ] Batch processing
- [ ] Statistics and analytics

## License

MIT License - See LICENSE.md in the parent project.

## Credits

Original DSF system developed and maintained by NAV (Norwegian Labour and Welfare Administration) 1967-2018.

Java port created as a demonstration of legacy system modernization.

## References

- [Original DSF Repository](https://github.com/navikt/dsf)
- [PL/I Documentation](https://en.wikipedia.org/wiki/PL/I)
- [CICS Documentation](https://www.ibm.com/docs/en/cics-transaction-server)
- [Norwegian FNR Algorithm](https://www.skatteetaten.no/en/person/national-registry/birth-and-name-selection/children-born-in-norway/national-id-number/)
