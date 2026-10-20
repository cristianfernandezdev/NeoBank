# NeoBank — Contexto del proyecto

## Qué es
Backend bancario (API REST) que simula operaciones financieras. Proyecto intermodular de DAM de Christian Fernández. Portfolio profesional. Entrega: Mar/Abr 2027.

## Stack
- Java 21 LTS, Spring Boot 4.1.1, Spring Data JPA + Hibernate
- H2 en memoria (dev/test) → PostgreSQL 16 en AWS (prod)
- Flyway (migraciones versionadas, NUNCA ddl-auto en dev real)
- Maven, Lombok (solo en infrastructure/), JUnit 5 + AssertJ + Mockito, ArchUnit
- Futuro: Kafka, Redis, Docker, JWT

## Arquitectura
- **Hexagonal (Ports & Adapters)** + **DDD Táctico** + **SOLID estricto**
- Regla de dependencias: `domain/ → NADA` | `application/ → solo domain/` | `infrastructure/ → todo`

## Reglas de código
- `domain/`: CERO imports de Spring/Jakarta/Lombok. Solo java.util.*, java.math.*, java.time.*
- Value Objects: final, inmutables, equals/hashCode por valor, factory methods, auto-validantes
- Use Cases: una interfaz = una acción (ISP)
- Inyección: constructor injection SIEMPRE
- Servicios de dominio/aplicación: NO llevan @Service → se registran como @Bean en BeanConfiguration
- Idioma: código en inglés, comentarios/docs en español
- DTOs y Commands: usar record de Java 21
- Excepciones: propias extendiendo DomainException, nunca RuntimeException genérica
- Lombok: SOLO en infrastructure/
- Naming tests: should_[resultado]_when_[condición]

## Estructura de paquetes
```
org.neobank/
├── domain/          → model/, event/, exception/, service/
├── application/     → port/in/, port/out/, service/
└── infrastructure/  → adapter/in/rest/, adapter/out/persistence/, config/
```

## Modelo de dominio objetivo
- Account (Aggregate Root): AccountId, UserId, Money, AccountStatus, Currency, createdAt
- Métodos: open(), reconstitute(), deposit(), withdraw(), block(), unblock(), close(), pullDomainEvents()
- Domain Events: sealed interface → AccountOpened, MoneyDeposited, MoneyWithdrawn, AccountBlocked, AccountClosed
- Domain Service: TransferDomainService.transfer(source, target, amount)
- Excepciones: InsufficientFunds, AccountNotActive, AccountNotFound, CurrencyMismatch

## Perfiles Spring
| dev | H2 mem, Flyway ON, ddl validate | test | H2 mem, Flyway OFF, ddl create-drop | prod | PostgreSQL AWS, Flyway ON, ddl validate |
