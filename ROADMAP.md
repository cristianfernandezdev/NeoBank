# 🚀 ROADMAP: De Junior a Senior Java/Spring Boot Developer con IA

> **Proyecto**: NeoBank — Arquitectura Hexagonal + DDD  
> **Autor**: Christian Fernández  
> **Objetivo**: Dominar Java/Spring Boot a nivel senior Y aprender a programar con agentes IA  
> **Fecha**: Octubre 2026

---

## 📋 Índice

1. [Tu punto de partida](#1-tu-punto-de-partida)
2. [Fase 1 — Fundamentos Senior que te faltan](#2-fase-1--fundamentos-senior-que-te-faltan)
3. [Fase 2 — Domina tu Dominio (DDD en serio)](#3-fase-2--domina-tu-dominio-ddd-en-serio)
4. [Fase 3 — Arquitectura Hexagonal Completa](#4-fase-3--arquitectura-hexagonal-completa)
5. [Fase 4 — Testing como un Senior](#5-fase-4--testing-como-un-senior)
6. [Fase 5 — Infraestructura y DevOps](#6-fase-5--infraestructura-y-devops)
7. [Fase 6 — Programar con IA (El multiplicador)](#7-fase-6--programar-con-ia-el-multiplicador)
8. [Fase 7 — Proyecto Final: NeoBank Completo](#8-fase-7--proyecto-final-neobank-completo)
9. [Recursos y libros recomendados](#9-recursos-y-libros-recomendados)
10. [Checklist de Senior](#10-checklist-de-senior)

---

## 1. Tu punto de partida

Tu NeoBank ya tiene una base sólida:

```
NeoBank/
├── domain/model/          ← Account.java, Money.java (Value Object)
├── application/
│   ├── port/in/           ← CreateAccountUseCase.java
│   ├── port/out/          ← SaveAccountPort.java
│   └── service/           ← AccountService.java
└── infrastructure/
    ├── adapter/in/rest/   ← AccountController.java
    └── adapter/out/persistence/ ← AccountJpaEntity, Adapter, Repository
```

**Lo que ya sabes**: Hexagonal básica, separar dominio de infra, puertos y adaptadores.  
**Lo que te falta**: Todo lo de abajo. Vamos paso a paso.

---

## 2. Fase 1 — Fundamentos Senior que te faltan

> ⏱️ **Duración estimada**: 3-4 semanas  
> 🎯 **Meta**: Escribir código que otro senior respetaría

### 2.1 Java Moderno (17+)

Aprende y practica en NeoBank:

| Concepto | Ejemplo en NeoBank | Prioridad |
|----------|-------------------|-----------|
| `record` | `CreateAccountRequest`, `CreateAccountResponse` | 🔴 Alta |
| `sealed` interfaces | Tipos de transacción: `sealed interface Transaction permits Deposit, Withdrawal` | 🟡 Media |
| Pattern Matching (`instanceof`) | Simplificar `equals()` en Value Objects | 🟡 Media |
| `Optional` bien usado | Nunca devolver `null` en un repositorio | 🔴 Alta |
| `Stream API` avanzado | Calcular balances, filtrar transacciones | 🔴 Alta |
| Text blocks `"""` | SQL queries en tests | 🟢 Baja |

**Ejercicio práctico en NeoBank:**
```java
// ❌ Así NO (junior)
public class CreateAccountRequest {
    private UUID userId;
    private String currency;
    // + getters, setters, constructor...
}

// ✅ Así SÍ (senior) — ya lo tenías parcialmente
public record CreateAccountRequest(
    UUID userId,
    String currency
) {}
```

### 2.2 Principios SOLID — de verdad

No basta con saber la teoría. Practica cada uno en NeoBank:

| Principio | Cómo aplicarlo en NeoBank |
|-----------|--------------------------|
| **S** - Single Responsibility | `AccountService` SOLO orquesta, no valida ni mapea |
| **O** - Open/Closed | Añadir `DepositUseCase` sin tocar `AccountService` |
| **L** - Liskov Substitution | Tus puertos (`SaveAccountPort`) deben ser intercambiables |
| **I** - Interface Segregation | No un `AccountPort` gigante, sino `SaveAccountPort`, `FindAccountPort`, etc. |
| **D** - Dependency Inversion | Tu dominio NUNCA importa Spring. Nunca. Jamás. |

### 2.3 Clean Code que importa

```java
// ❌ Nombre malo
public UUID create(UUID userId) { ... }

// ✅ Nombre que documenta la intención
public AccountId openNewAccount(OpenAccountCommand command) { ... }
```

**Regla senior**: Si necesitas un comentario para explicar QUÉ hace el código, el nombre está mal.  
Los comentarios son para explicar POR QUÉ, nunca QUÉ.

---

## 3. Fase 2 — Domina tu Dominio (DDD en serio)

> ⏱️ **Duración estimada**: 4-6 semanas  
> 🎯 **Meta**: Tu capa `domain/` es puro Java, sin dependencias externas, y es donde vive la lógica de negocio

### 3.1 Value Objects — Hazlos bien

Tu `Money.java` es un buen comienzo. Un Value Object senior tiene:

```java
// Lo que tiene que tener Money.java al final de esta fase:
public final class Money {  // 'final' — no se hereda
    
    // Inmutable — todos los campos final
    private final BigDecimal amount;
    private final Currency currency;
    
    // Constructor privado + factory methods
    private Money(BigDecimal amount, Currency currency) { ... }
    public static Money of(BigDecimal amount, Currency currency) { ... }
    public static Money zero(Currency currency) { ... }
    
    // Comportamiento rico (no solo getters)
    public Money add(Money other) { ... }
    public Money subtract(Money other) { ... }
    public boolean isPositive() { ... }
    public boolean isGreaterThan(Money other) { ... }
    
    // equals + hashCode basados en VALOR, no en identidad
    @Override public boolean equals(Object o) { ... }
    @Override public int hashCode() { ... }
    
    // toString legible
    @Override public String toString() { return amount + " " + currency; }
}
```

**Más Value Objects para NeoBank:**

| Value Object | Por qué | Campos |
|-------------|---------|--------|
| `AccountId` | Nunca confundir un UUID de cuenta con uno de usuario | `UUID value` |
| `UserId` | Tipado fuerte | `UUID value` |
| `IBAN` | Validación de formato | `String value` |
| `TransactionId` | Identificar transacciones | `UUID value` |

### 3.2 Aggregate Root — Account

```
Account (Aggregate Root)
├── AccountId (identidad)
├── UserId (propietario)
├── Money balance (estado)
├── AccountStatus (ACTIVE, BLOCKED, CLOSED)
├── List<Transaction> (historial — solo si es necesario)
│
├── deposit(Money) → void     ← Lógica de negocio AQUÍ
├── withdraw(Money) → void    ← Validar fondos AQUÍ
├── block() → void            ← Cambio de estado AQUÍ
└── close() → void            ← Invariante: balance == 0
```

**Regla de oro DDD**: Si la lógica es de negocio, va en el Aggregate. Si es de orquestación (llamar a varios servicios), va en el Application Service.

### 3.3 Domain Events

```java
// Evento de dominio — algo que YA pasó
public record AccountCreatedEvent(
    AccountId accountId,
    UserId userId,
    Currency currency,
    Instant createdAt
) implements DomainEvent {}

// El Aggregate los produce
public class Account {
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    public static Account open(UserId userId, Currency currency) {
        Account account = new Account(...);
        account.domainEvents.add(new AccountCreatedEvent(...));
        return account;
    }
    
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
```

### 3.4 Domain Exceptions

```java
// ❌ Genérica (junior)
throw new RuntimeException("No hay fondos");

// ✅ Específica del dominio (senior)
public class InsufficientFundsException extends DomainException {
    public InsufficientFundsException(Money balance, Money requested) {
        super("Fondos insuficientes. Balance: %s, Solicitado: %s"
            .formatted(balance, requested));
    }
}
```

### 3.5 Diagrama de tu Dominio NeoBank objetivo

```
┌─────────────────────────────────────────────────────┐
│                    DOMINIO                          │
│                                                     │
│  ┌─────────────┐    ┌──────────────┐               │
│  │   Account    │    │  Transaction │               │
│  │ (Aggregate)  │───▶│ (Entity)     │               │
│  ├─────────────┤    ├──────────────┤               │
│  │ AccountId   │    │ TransactionId│               │
│  │ UserId      │    │ Money amount │               │
│  │ Money bal.  │    │ Type         │               │
│  │ Status      │    │ Instant date │               │
│  │ IBAN        │    │ String desc. │               │
│  ├─────────────┤    └──────────────┘               │
│  │ deposit()   │                                    │
│  │ withdraw()  │    ┌──────────────┐               │
│  │ transfer()  │    │    Money     │               │
│  │ block()     │    │ (Value Obj.) │               │
│  │ close()     │    ├──────────────┤               │
│  └─────────────┘    │ add()        │               │
│                     │ subtract()   │               │
│  Eventos:           │ isPositive() │               │
│  - AccountCreated   └──────────────┘               │
│  - MoneyDeposited                                   │
│  - MoneyWithdrawn   ┌──────────────┐               │
│  - MoneyTransferred │  AccountId   │               │
│  - AccountBlocked   │  UserId      │               │
│  - AccountClosed    │ (Value Objs.)│               │
│                     └──────────────┘               │
└─────────────────────────────────────────────────────┘
```

---

## 4. Fase 3 — Arquitectura Hexagonal Completa

> ⏱️ **Duración estimada**: 3-4 semanas  
> 🎯 **Meta**: Puedes cambiar la base de datos sin tocar una línea de dominio

### 4.1 Estructura de paquetes objetivo

```
org.neobank/
├── domain/                          ← CERO dependencias externas
│   ├── model/
│   │   ├── Account.java             (Aggregate Root)
│   │   ├── Money.java               (Value Object)
│   │   ├── AccountId.java           (Value Object)
│   │   ├── UserId.java              (Value Object)
│   │   ├── Transaction.java         (Entity)
│   │   └── AccountStatus.java       (Enum)
│   ├── event/
│   │   ├── DomainEvent.java
│   │   ├── AccountCreatedEvent.java
│   │   └── MoneyDepositedEvent.java
│   ├── exception/
│   │   ├── DomainException.java
│   │   ├── InsufficientFundsException.java
│   │   └── AccountInactiveException.java
│   └── service/                     ← Domain Services (raro, solo si la lógica no cabe en un Aggregate)
│       └── TransferDomainService.java
│
├── application/                     ← Orquesta, no decide
│   ├── port/
│   │   ├── in/                      ← Lo que el mundo exterior puede pedir
│   │   │   ├── CreateAccountUseCase.java
│   │   │   ├── DepositMoneyUseCase.java
│   │   │   ├── WithdrawMoneyUseCase.java
│   │   │   └── TransferMoneyUseCase.java
│   │   └── out/                     ← Lo que la aplicación necesita del exterior
│   │       ├── SaveAccountPort.java
│   │       ├── LoadAccountPort.java
│   │       └── PublishEventPort.java
│   ├── service/
│   │   ├── AccountService.java      ← Implementa los Use Cases
│   │   └── TransferService.java
│   └── dto/                         ← Commands y Queries
│       ├── CreateAccountCommand.java
│       ├── DepositCommand.java
│       └── AccountResponse.java
│
└── infrastructure/                  ← Todo lo "sucio" vive aquí
    ├── adapter/
    │   ├── in/
    │   │   ├── rest/
    │   │   │   ├── AccountController.java
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   └── dto/ (Request/Response JSON)
    │   │   └── event/
    │   │       └── KafkaAccountEventListener.java
    │   └── out/
    │       ├── persistence/
    │       │   ├── AccountJpaEntity.java
    │       │   ├── AccountPersistenceAdapter.java
    │       │   ├── AccountMapper.java
    │       │   └── SpringDataAccountRepository.java
    │       └── messaging/
    │           └── KafkaEventPublisher.java
    └── config/
        ├── BeanConfiguration.java
        └── SecurityConfig.java
```

### 4.2 La regla de dependencias

```
          ┌──────────────────┐
          │   DOMINIO        │  ← No depende de NADA
          │   (model, event) │
          └────────▲─────────┘
                   │ depende de
          ┌────────┴─────────┐
          │   APPLICATION    │  ← Solo depende del dominio
          │   (port, service)│
          └────────▲─────────┘
                   │ depende de
          ┌────────┴─────────┐
          │  INFRASTRUCTURE  │  ← Depende de todo (Spring, JPA, Kafka...)
          │  (adapter, config)│
          └──────────────────┘
```

**Cómo verificarlo**: En tu `domain/` NUNCA debe haber un `import org.springframework.*` ni `import jakarta.*`.

### 4.3 Mappers — El puente entre capas

```java
// infrastructure/adapter/out/persistence/AccountMapper.java
public class AccountMapper {
    
    // Dominio → JPA (para guardar)
    public static AccountJpaEntity toJpaEntity(Account account) {
        return new AccountJpaEntity(
            account.getId().value(),
            account.getUserId().value(),
            account.getBalance().getAmount(),
            account.getBalance().getCurrency().getCurrencyCode(),
            account.getStatus().name()
        );
    }
    
    // JPA → Dominio (para reconstruir)
    public static Account toDomain(AccountJpaEntity entity) {
        return Account.reconstitute(
            new AccountId(entity.getId()),
            new UserId(entity.getUserId()),
            Money.of(entity.getBalance(), Currency.getInstance(entity.getCurrency())),
            AccountStatus.valueOf(entity.getStatus())
        );
    }
}
```

---

## 5. Fase 4 — Testing como un Senior

> ⏱️ **Duración estimada**: 3 semanas  
> 🎯 **Meta**: Cobertura > 80% en dominio y aplicación, con tests que documentan el negocio

### 5.1 Pirámide de Testing

```
            ╱╲
           ╱  ╲          E2E Tests (pocos, lentos)
          ╱    ╲         → Testcontainers + REST
         ╱──────╲
        ╱        ╲       Integration Tests (algunos)
       ╱          ╲      → @SpringBootTest, slices
      ╱────────────╲
     ╱              ╲    Unit Tests (MUCHOS, rápidos)
    ╱                ╲   → JUnit 5 + Mockito
   ╱──────────────────╲
```

### 5.2 Tests del Dominio — Los más importantes

```java
// domain/model/MoneyTest.java
class MoneyTest {
    
    @Test
    void should_add_same_currency() {
        Money ten = Money.of(10, Currency.getInstance("EUR"));
        Money five = Money.of(5, Currency.getInstance("EUR"));
        
        Money result = ten.add(five);
        
        assertThat(result).isEqualTo(Money.of(15, Currency.getInstance("EUR")));
    }
    
    @Test
    void should_reject_different_currencies() {
        Money euros = Money.of(10, Currency.getInstance("EUR"));
        Money dollars = Money.of(5, Currency.getInstance("USD"));
        
        assertThatThrownBy(() -> euros.add(dollars))
            .isInstanceOf(CurrencyMismatchException.class);
    }
}

// domain/model/AccountTest.java
class AccountTest {
    
    @Test
    void should_deposit_money() {
        Account account = Account.open(new UserId(UUID.randomUUID()), Currency.getInstance("EUR"));
        Money deposit = Money.of(100, Currency.getInstance("EUR"));
        
        account.deposit(deposit);
        
        assertThat(account.getBalance()).isEqualTo(deposit);
    }
    
    @Test
    void should_reject_withdrawal_exceeding_balance() {
        Account account = Account.open(new UserId(UUID.randomUUID()), Currency.getInstance("EUR"));
        account.deposit(Money.of(50, Currency.getInstance("EUR")));
        
        assertThatThrownBy(() -> account.withdraw(Money.of(100, Currency.getInstance("EUR"))))
            .isInstanceOf(InsufficientFundsException.class);
    }
    
    @Test
    void should_not_allow_operations_on_blocked_account() {
        Account account = Account.open(new UserId(UUID.randomUUID()), Currency.getInstance("EUR"));
        account.block();
        
        assertThatThrownBy(() -> account.deposit(Money.of(100, Currency.getInstance("EUR"))))
            .isInstanceOf(AccountInactiveException.class);
    }
}
```

### 5.3 Tests de Application Service

```java
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    
    @Mock SaveAccountPort saveAccountPort;
    @Mock LoadAccountPort loadAccountPort;
    @InjectMocks AccountService accountService;
    
    @Test
    void should_create_account_and_persist() {
        CreateAccountCommand command = new CreateAccountCommand(
            UUID.randomUUID(), "EUR"
        );
        
        AccountId result = accountService.create(command);
        
        assertThat(result).isNotNull();
        verify(saveAccountPort).save(any(Account.class));
    }
}
```

### 5.4 Herramientas de Testing

| Herramienta | Para qué | Prioridad |
|------------|----------|-----------|
| JUnit 5 | Framework base | 🔴 Obligatorio |
| AssertJ | Assertions fluidas: `assertThat(x).isEqualTo(y)` | 🔴 Obligatorio |
| Mockito | Mocks de puertos/adaptadores | 🔴 Obligatorio |
| Testcontainers | PostgreSQL/Docker real en tests | 🟡 Importante |
| ArchUnit | Verificar reglas de arquitectura automáticamente | 🟡 Importante |
| REST Assured | Tests de API REST | 🟢 Útil |

### 5.5 ArchUnit — Tu policía de arquitectura

```java
@AnalyzeClasses(packages = "org.neobank")
class ArchitectureTest {
    
    @ArchTest
    static final ArchRule domain_should_not_depend_on_spring =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("org.springframework..");
    
    @ArchTest
    static final ArchRule domain_should_not_depend_on_infrastructure =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("..infrastructure..");
}
```

---

## 6. Fase 5 — Infraestructura y DevOps

> ⏱️ **Duración estimada**: 4 semanas  
> 🎯 **Meta**: Tu app corre en Docker, tiene CI/CD, y tú sabes monitorearla

### 6.1 Base de datos — PostgreSQL en serio

| Concepto | Detalle |
|----------|---------|
| Flyway/Liquibase | Migraciones versionadas (nunca `ddl-auto: update` en producción) |
| Índices | Saber cuándo y por qué crear uno |
| Transacciones | `@Transactional` y sus trampas |
| Connection Pool | HikariCP, tuning básico |

### 6.2 Docker

```yaml
# docker-compose.yml mejorado
services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: neobank
      POSTGRES_USER: neobank
      POSTGRES_PASSWORD: neobank
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  neobank-app:
    build: .
    depends_on:
      - postgres
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/neobank

volumes:
  pgdata:
```

### 6.3 CI/CD — GitHub Actions

```yaml
# .github/workflows/ci.yml
name: NeoBank CI
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
      - run: ./mvnw verify
```

### 6.4 Observabilidad

| Herramienta | Para qué |
|------------|----------|
| Spring Actuator | Health checks, métricas |
| Micrometer + Prometheus | Métricas de negocio |
| Grafana | Dashboards |
| Logback + MDC | Logs estructurados con trace IDs |

---

## 7. Fase 6 — Programar con IA (El multiplicador)

> ⏱️ **Duración**: Continua — empieza en paralelo con la Fase 2  
> 🎯 **Meta**: Usar IA para ir 3x-5x más rápido SIN perder calidad

### 7.1 La mentalidad correcta

```
❌ "IA, hazme una app de banco"          → Código basura
✅ "IA, implementa el Value Object Money  → Código senior
    siguiendo estas reglas de DDD..."
```

**Regla de oro**: La IA es tu par de programación junior muy rápido. Tú eres el arquitecto. Tú decides, ella ejecuta.

### 7.2 Cómo usar Antigravity/Gemini en IntelliJ

#### Nivel 1: Asistente básico (donde estás)
- Preguntar dudas de código
- Generar boilerplate
- Explicar errores

#### Nivel 2: Par de programación (donde debes llegar)
- Dar contexto preciso antes de pedir código
- Usar `/plan` para tareas complejas
- Revisar SIEMPRE lo que genera

#### Nivel 3: Agente autónomo (nivel avanzado)
- Usar `/goal` para tareas largas y autónomas
- Definir reglas del proyecto (`.gemini/rules/`)
- Crear skills personalizados

### 7.3 Configurar reglas de proyecto para IA

Crea un archivo de reglas para que la IA siempre siga tu arquitectura:

```
NeoBank/
└── .gemini/
    └── rules/
        └── neobank-rules.md
```

Contenido del archivo de reglas:
```markdown
# Reglas del proyecto NeoBank

## Arquitectura
- Arquitectura Hexagonal estricta (Ports & Adapters)
- DDD táctico con Aggregates, Value Objects, Domain Events
- La capa domain/ NUNCA importa Spring, Jakarta, ni Lombok
- Los Value Objects son `final`, inmutables, con `equals/hashCode`

## Convenciones de código
- Java 17+ features: records, sealed, pattern matching
- Nombres en español para comentarios, en inglés para código
- Tests con JUnit 5 + AssertJ + Mockito
- Nomenclatura: UseCase para puertos in, Port para puertos out

## Estructura de paquetes
- domain.model → Aggregates y Value Objects
- domain.event → Domain Events
- domain.exception → Excepciones del dominio
- application.port.in → Use Cases (interfaces)
- application.port.out → Puertos de salida (interfaces)
- application.service → Implementación de Use Cases
- infrastructure.adapter.in.rest → Controllers REST
- infrastructure.adapter.out.persistence → JPA

## Prohibiciones
- NO usar @Autowired en campos. Usar constructor injection
- NO poner lógica de negocio en Controllers o Services de aplicación
- NO usar Lombok en domain/
- NO devolver entidades JPA fuera del adaptador de persistencia
```

### 7.4 Prompts que funcionan (recetas probadas)

#### Para crear un Value Object:
```
Crea un Value Object `AccountId` en domain/model/ siguiendo estas reglas:
- Clase final e inmutable
- Wrappea un UUID
- Factory method `generate()` para crear nuevos
- Constructor público para reconstruir desde persistencia
- equals/hashCode basados en el valor
- Sin dependencias externas (ni Spring ni Lombok)
```

#### Para crear un Use Case completo:
```
Implementa el caso de uso "Depositar dinero" en NeoBank:
1. Puerto de entrada: DepositMoneyUseCase con método deposit(DepositCommand)
2. Command: record DepositCommand(AccountId accountId, Money amount)
3. Servicio: que cargue la cuenta, llame a account.deposit(), y persista
4. Sigue la arquitectura hexagonal del proyecto
5. No toques el dominio, ya tiene Account.deposit(Money)
```

#### Para crear tests:
```
Genera tests unitarios para Account.java cubriendo:
- Crear cuenta nueva (balance cero)
- Depositar dinero válido
- Retirar dinero con fondos suficientes
- Retirar dinero sin fondos → InsufficientFundsException
- Operar en cuenta bloqueada → AccountInactiveException
- Cerrar cuenta con balance → DomainException
- Cerrar cuenta con balance cero → OK
Usa JUnit 5 + AssertJ. Naming: should_[resultado]_when_[condición]
```

### 7.5 Flujo de trabajo Senior con IA

```
1. TÚ diseñas    → "Necesito un Value Object AccountId"
2. IA genera     → Código inicial
3. TÚ revisas    → ¿Cumple mis reglas? ¿Es inmutable? ¿tiene equals?
4. TÚ refinas    → "Añade validación de no-nulo en constructor"
5. IA ajusta     → Versión mejorada
6. TÚ testeas    → "Genera tests para todos los edge cases"
7. IA genera     → Tests
8. TÚ ejecutas   → ./mvnw test
9. TÚ commiteas  → git commit -m "feat: add AccountId value object"
```

**La IA NUNCA debería**:
- Decidir la arquitectura por ti
- Hacer commits sin tu revisión
- Cambiar archivos que no le pediste
- Añadir dependencias sin que tú lo apruebes

### 7.6 Herramientas de IA para tu stack

| Herramienta | Para qué | Dónde |
|------------|----------|-------|
| Antigravity (IntelliJ) | Coding asistido en el IDE | Ya lo tienes instalado |
| `/plan` | Planificar antes de ejecutar | Comando en el chat |
| `/goal` | Tareas autónomas largas | Comando en el chat |
| GitHub Copilot | Autocompletado inline | Plugin IntelliJ |
| Gemini Web | Diseño de alto nivel, diagramas | gemini.google.com |
| Claude | Análisis profundo de arquitectura | claude.ai |

### 7.7 Crear un archivo AGENTS.md (documentación para la IA)

```markdown
# AGENTS.md — Instrucciones para agentes IA

## Contexto del proyecto
NeoBank es un proyecto bancario intermodular con arquitectura hexagonal
y DDD táctico. Stack: Java 17, Spring Boot 4.1, PostgreSQL, Docker.

## Reglas estrictas
1. El dominio es puro Java — sin anotaciones de frameworks
2. Todo cambio en domain/ requiere un test unitario
3. Los Value Objects son inmutables y tienen equals/hashCode
4. Los Use Cases son interfaces en application/port/in/
5. Los adaptadores en infrastructure/ implementan los puertos

## Flujo de trabajo
1. Antes de editar, muestra el plan
2. No edites archivos que no se te pidan
3. Después de editar, compila y ejecuta tests
4. Si algo falla, corrige antes de continuar
```

---

## 8. Fase 7 — Proyecto Final: NeoBank Completo

> ⏱️ **Duración estimada**: 6-8 semanas  
> 🎯 **Meta**: Un proyecto que puedes enseñar en una entrevista y decir "yo lo diseñé"

### 8.1 Funcionalidades objetivo

| # | Feature | Complejidad | Conceptos que aprendes |
|---|---------|------------|----------------------|
| 1 | ✅ Crear cuenta | Básica | Aggregate, Use Case, Adapter |
| 2 | Depositar dinero | Media | Cargar aggregate, domain event |
| 3 | Retirar dinero | Media | Validación de invariantes |
| 4 | Transferir entre cuentas | Alta | Domain Service, transacciones |
| 5 | Historial de transacciones | Media | Queries, paginación |
| 6 | Bloquear/desbloquear cuenta | Fácil | Estado del aggregate |
| 7 | Autenticación JWT | Media | Spring Security |
| 8 | Notificaciones de eventos | Alta | Event-driven, Kafka/RabbitMQ |
| 9 | API versionada y documentada | Media | OpenAPI/Swagger |
| 10 | Métricas de negocio | Media | Micrometer, dashboards |

### 8.2 Orden de implementación recomendado

```
Semana 1-2: Features 2-3 (Deposit/Withdraw)
  → Consolida Value Objects, Aggregate, Tests
  
Semana 3-4: Feature 4 (Transfer)
  → Domain Service, transacciones distribuidas
  
Semana 5: Features 5-6 (Historial, Bloqueo)
  → Queries, CQRS básico, paginación
  
Semana 6: Feature 7 (Auth)
  → Spring Security, JWT, roles
  
Semana 7: Features 8-9 (Eventos, API docs)
  → Event-driven architecture
  
Semana 8: Feature 10 + Pulido
  → Observabilidad, Docker final, README
```

---

## 9. Recursos y libros recomendados

### 📚 Libros (por orden de lectura)

| # | Libro | Autor | Por qué |
|---|-------|-------|---------|
| 1 | **Clean Code** | Robert C. Martin | Escribir código legible |
| 2 | **Head First Design Patterns** | Freeman & Robson | Patrones de diseño con ejemplos claros |
| 3 | **Domain-Driven Design Distilled** | Vaughn Vernon | DDD resumido y práctico |
| 4 | **Get Your Hands Dirty on Clean Architecture** | Tom Hombergs | Hexagonal con Spring Boot (ESTE ES CLAVE) |
| 5 | **Implementing Domain-Driven Design** | Vaughn Vernon | DDD en profundidad |
| 6 | **Release It!** | Michael Nygard | Patrones de resiliencia |
| 7 | **Designing Data-Intensive Applications** | Martin Kleppmann | Sistemas distribuidos |

### 🌐 Recursos online

| Recurso | URL | Para qué |
|---------|-----|----------|
| Baeldung | baeldung.com | Spring Boot recipes |
| Reflectoring | reflectoring.io | Hexagonal + Spring Boot |
| Java Brains (YouTube) | youtube.com/@JavaBrains | Spring Security, Microservicios |
| Marco Behler (YouTube) | youtube.com/@MarcoBehler | Spring internals |
| JetBrains Academy | jetbrains.com/academy | Java tracks |

### 🛠️ Repositorios de referencia

- `thombergs/buckpal` — Ejemplo canónico de hexagonal con Spring Boot
- `ddd-by-examples/library` — DDD táctico con Java
- `spring-projects/spring-petclinic` — Best practices de Spring

---

## 10. Checklist de Senior

Marca cada item cuando lo domines. Cuando tengas > 80%, puedes decir que eres senior:

### Java Core
- [ ] Records, sealed interfaces, pattern matching
- [ ] Generics avanzados (bounded wildcards, type erasure)
- [ ] Concurrencia: CompletableFuture, Virtual Threads
- [ ] Stream API: collectors personalizados, parallel streams
- [ ] Optional correctamente (nunca `get()` sin `isPresent()`)
- [ ] Manejo de excepciones (checked vs unchecked, cuándo usar cada una)

### Spring Boot
- [ ] Dependency Injection: cómo funciona por dentro
- [ ] Profiles y configuración externalizada
- [ ] Spring Security con JWT
- [ ] Spring Data JPA: queries derivadas, @Query, Specifications
- [ ] Transacciones: propagación, isolation levels
- [ ] Actuator y health checks
- [ ] Bean lifecycle y scopes

### Arquitectura
- [ ] Hexagonal / Ports & Adapters
- [ ] DDD táctico: Aggregates, Value Objects, Domain Events
- [ ] CQRS básico (separar lectura de escritura)
- [ ] Event-driven architecture
- [ ] API REST: HATEOAS, versionado, error handling
- [ ] Patrones: Builder, Strategy, Observer, Factory

### Testing
- [ ] TDD: test primero, código después
- [ ] Unit tests del dominio con JUnit 5 + AssertJ
- [ ] Mocks con Mockito
- [ ] Integration tests con @SpringBootTest
- [ ] Testcontainers para PostgreSQL
- [ ] ArchUnit para reglas de arquitectura
- [ ] Cobertura > 80% en dominio y aplicación

### DevOps & Infra
- [ ] Docker: Dockerfile multi-stage, docker-compose
- [ ] CI/CD: GitHub Actions
- [ ] PostgreSQL: migraciones, índices, EXPLAIN ANALYZE
- [ ] Observabilidad: logs estructurados, métricas, health
- [ ] Git: conventional commits, branching strategy

### IA & Productividad
- [ ] Configurar reglas de proyecto para IA (`.gemini/rules/`)
- [ ] Crear AGENTS.md para documentar contexto
- [ ] Saber cuándo usar IA y cuándo pensar tú
- [ ] Revisar SIEMPRE el código generado
- [ ] Usar /plan para tareas complejas
- [ ] Escribir prompts específicos, no genéricos

---

## 🏁 Siguiente paso

> **Empieza por la Fase 2**: Refactoriza tu `Money.java` para que sea un Value Object completo  
> con factory methods, `equals/hashCode`, y tests unitarios.  
> Hazlo tú primero. Luego pide a la IA que revise y mejore.  
> Así es como aprende un senior: **primero piensa, luego ejecuta, luego valida**.

---

*"La diferencia entre un junior y un senior no es cuánto código escriben, sino cuánto código evitan escribir."*
