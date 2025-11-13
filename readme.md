# Shopping Cart Calculation Service

Enterprise-grade Spring Boot application for calculating shopping cart totals with client-specific pricing strategies.

## 🏗️ Architecture & Design Patterns

This application demonstrates **14 years of enterprise Java development experience** through:

### Design Patterns Implemented
- **Strategy Pattern**: Dynamic pricing calculation based on client type
- **Factory Pattern**: PricingStrategyFactory for strategy selection
- **Repository Pattern**: Data access abstraction with Spring Data JPA
- **DTO Pattern**: Clean separation between API and domain models
- **Aggregate Pattern**: ShoppingCart as domain aggregate root
- **Builder Pattern**: Fluent object construction with Lombok

### SOLID Principles
- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Extensible pricing strategies without modifying existing code
- **Liskov Substitution**: Client polymorphism with proper inheritance
- **Interface Segregation**: Focused interfaces for specific behaviors
- **Dependency Inversion**: Dependencies on abstractions, not concretions

### Domain-Driven Design (DDD)
- Clear separation of domain entities, value objects, and aggregates
- Rich domain models with business logic encapsulation
- Ubiquitous language throughout the codebase

## 📋 Requirements Implemented

### Client Types
1. **Individual Clients**
   - Client ID, First Name, Last Name
   - Standard pricing: High-end phone €1500, Mid-range €800, Laptop €1200

2. **Professional Clients**
   - Client ID, Company Name, VAT Number (optional), Business Registration Number, Annual Revenue
   - **High Revenue (>€10M)**: High-end €1000, Mid-range €550, Laptop €900
   - **Low Revenue (<€10M)**: High-end €1150, Mid-range €600, Laptop €1000

### Features
- ✅ Calculate cart totals with multiple product quantities
- ✅ Client-specific pricing strategies
- ✅ Complete CRUD operations for clients
- ✅ RESTful API with comprehensive error handling
- ✅ Input validation with Jakarta Bean Validation
- ✅ Swagger/OpenAPI documentation
- ✅ H2 in-memory database for demo
- ✅ Sample data preloaded
- ✅ Comprehensive unit tests

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

### Access Points
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **H2 Console**: http://localhost:8080/api/h2-console
  - JDBC URL: `jdbc:h2:mem:shoppingcartdb`
  - Username: `sa`
  - Password: (empty)

## 📡 API Endpoints

### Client Management

#### Create Individual Client
```http
POST /api/v1/clients/individual
Content-Type: application/json

{
  "clientId": "IND003",
  "firstName": "Alice",
  "lastName": "Johnson"
}
```

#### Create Professional Client
```http
POST /api/v1/clients/professional
Content-Type: application/json

{
  "clientId": "PRO004",
  "companyName": "Enterprise Solutions Inc",
  "vatNumber": "US123456789",
  "businessRegistrationNumber": "EIN-12-3456789",
  "annualRevenue": 25000000.00
}
```

#### Get All Clients
```http
GET /api/v1/clients
```

#### Get Client by ID
```http
GET /api/v1/clients/{clientId}
```

### Shopping Cart Calculation

#### Calculate Cart Total
```http
POST /api/v1/cart/calculate
Content-Type: application/json

{
  "clientId": "IND001",
  "items": [
    {
      "productType": "HIGH_END_PHONE",
      "quantity": 2
    },
    {
      "productType": "LAPTOP",
      "quantity": 1
    }
  ]
}
```

**Response:**
```json
{
  "success": true,
  "message": "Cart total calculated successfully",
  "data": {
    "clientId": "IND001",
    "clientType": "INDIVIDUAL",
    "clientName": "John Doe",
    "totalAmount": 4200.00,
    "currency": "EUR",
    "totalItems": 3,
    "lineItems": [
      {
        "productType": "HIGH_END_PHONE",
        "productName": "High-End Phone",
        "quantity": 2,
        "unitPrice": 1500.00,
        "lineTotal": 3000.00
      },
      {
        "productType": "LAPTOP",
        "productName": "Laptop",
        "quantity": 1,
        "unitPrice": 1200.00,
        "lineTotal": 1200.00
      }
    ],
    "calculatedAt": "2024-11-10T14:30:00"
  },
  "timestamp": "2024-11-10T14:30:00"
}
```

## 🧪 Test Scenarios (Preloaded Data)

### Scenario 1: Individual Client
```bash
curl -X POST http://localhost:8080/api/v1/cart/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "IND001",
    "items": [
      {"productType": "HIGH_END_PHONE", "quantity": 1},
      {"productType": "MID_RANGE_PHONE", "quantity": 1},
      {"productType": "LAPTOP", "quantity": 1}
    ]
  }'
```
**Expected Total**: €3500 (1500 + 800 + 1200)

### Scenario 2: Professional High Revenue
```bash
curl -X POST http://localhost:8080/api/v1/cart/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "PRO001",
    "items": [
      {"productType": "HIGH_END_PHONE", "quantity": 1},
      {"productType": "MID_RANGE_PHONE", "quantity": 1},
      {"productType": "LAPTOP", "quantity": 1}
    ]
  }'
```
**Expected Total**: €2450 (1000 + 550 + 900)

### Scenario 3: Professional Low Revenue
```bash
curl -X POST http://localhost:8080/api/v1/cart/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "PRO002",
    "items": [
      {"productType": "HIGH_END_PHONE", "quantity": 1},
      {"productType": "MID_RANGE_PHONE", "quantity": 1},
      {"productType": "LAPTOP", "quantity": 1}
    ]
  }'
```
**Expected Total**: €2750 (1150 + 600 + 1000)

### Scenario 4: Bulk Order
```bash
curl -X POST http://localhost:8080/api/v1/cart/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "PRO001",
    "items": [
      {"productType": "HIGH_END_PHONE", "quantity": 10},
      {"productType": "LAPTOP", "quantity": 5}
    ]
  }'
```
**Expected Total**: €14500 (10×1000 + 5×900)

## 🏛️ Project Structure

```
src/main/java/com/ecommerce/shoppingcart/
├── config/
│   ├── PricingConfiguration.java          # Externalized pricing config
│   └── DataLoader.java                    # Sample data initialization
├── controller/
│   ├── CartController.java                # Cart calculation endpoints
│   └── ClientController.java              # Client management endpoints
├── domain/
│   ├── aggregate/
│   │   └── ShoppingCart.java              # Aggregate root
│   ├── entity/
│   │   ├── Client.java                    # Abstract base entity
│   │   ├── IndividualClient.java          # Concrete entity
│   │   └── ProfessionalClient.java        # Concrete entity
│   ├── enums/
│   │   └── ProductType.java               # Product enumeration
│   └── valueobject/
│       ├── CartItem.java                  # Value object
│       └── CartTotalResult.java           # Result VO
├── dto/
│   ├── request/
│   │   ├── CartCalculationRequest.java    # Request DTOs
│   │   └── ClientRequest.java
│   └── response/
│       └── Response.java                   # Response DTOs
├── exception/
│   ├── [Custom exceptions]
│   └── GlobalExceptionHandler.java        # Centralized error handling
├── repository/
│   └── ClientRepository.java              # Data access layer
├── service/
│   ├── CartCalculationService.java        # Core business logic
│   ├── ClientService.java                 # Client management
│   ├── factory/
│   │   └── PricingStrategyFactory.java    # Strategy factory
│   └── strategy/
│       ├── PricingStrategy.java           # Strategy interface
│       ├── IndividualClientPricingStrategy.java
│       ├── ProfessionalHighRevenuePricingStrategy.java
│       └── ProfessionalLowRevenuePricingStrategy.java
└── ShoppingCartApplication.java           # Main application
```

## 🔧 Configuration

Pricing can be configured in `application.yml`:

```yaml
pricing:
  individual:
    high-end-phone: 1500.00
    mid-range-phone: 800.00
    laptop: 1200.00
  professional:
    high-revenue-threshold: 10000000.00
    high-revenue:
      high-end-phone: 1000.00
      mid-range-phone: 550.00
      laptop: 900.00
    low-revenue:
      high-end-phone: 1150.00
      mid-range-phone: 600.00
      laptop: 1000.00
```

## 🧪 Running Tests

```bash
mvn test
```

Tests include:
- Unit tests for business logic
- Pricing strategy tests
- Client classification tests
- Cart calculation scenarios

## 📊 Key Features for Client Demo

1. **Interactive API Documentation**: Use Swagger UI for live testing
2. **Preloaded Sample Data**: Ready-to-use clients for immediate demo
3. **Clear Price Differentiation**: Shows all three pricing tiers
4. **Comprehensive Error Handling**: Professional error messages
5. **Detailed Response Format**: Line-by-line breakdown with totals
6. **Validation**: Input validation with clear error messages
7. **Database Console**: View data in H2 console

## 🎯 Business Rules Validated

✅ Individual clients always get standard pricing  
✅ Professional clients with revenue >€10M get premium pricing  
✅ Professional clients with revenue <€10M get standard professional pricing  
✅ Multiple quantities of same product are properly calculated  
✅ Cart totals include detailed line-item breakdown  
✅ All monetary calculations use BigDecimal for precision  

## 📈 Production Considerations

For production deployment, consider:
- Replace H2 with production database (PostgreSQL, MySQL)
- Add Spring Security for authentication/authorization
- Implement caching with Redis
- Add monitoring with Actuator
- Set up CI/CD pipeline
- Containerize with Docker
- Configure externalized secrets management
- Add rate limiting
- Implement audit logging

## 👨‍💻 Author

Senior Java Developer with 14 years of enterprise application development experience.

## 📝 License

Proprietary - For client demonstration purposes.