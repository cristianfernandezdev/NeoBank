# 🏦 ROADMAP — NeoBank

---

## 📌 BLOQUE DE CONTEXTO DEL PROYECTO

> **OBLIGATORIO**: Todo agente IA que trabaje en NeoBank DEBE leer este bloque completo
> antes de generar código, dar consejos o tomar decisiones. Este bloque es la ÚNICA fuente
> de verdad sobre el proyecto.

### Qué es NeoBank

NeoBank es un **backend bancario** que simula operaciones financieras (cuentas, depósitos,
retiradas, transferencias). Es un **proyecto intermodular individual** del ciclo de **DAM**
(Desarrollo de Aplicaciones Multiplataforma) de Christian Fernández.

No es un trabajo cualquiera del ciclo — Christian quiere usarlo como **proyecto de portfolio
profesional**, por lo que la calidad, arquitectura y buenas prácticas deben ser superiores
a lo exigido por el ciclo. Los requisitos del ciclo son vagos; la exigencia real viene del
objetivo personal de aprender bien y tener un portfolio sólido.

**Entrega**: Marzo-Abril 2027.

### Perfil del estudiante

| Aspecto | Detalle |
|---------|---------|
| **Nivel** | Básico en Spring Boot — está aprendiendo |
| **Conocimientos previos** | Java básico-intermedio, algo de JavaFX, algo de Kotlin/Android |
| **Aún no sabe** | DDD profundo, Hexagonal avanzada, SOLID estricto, testing, Docker, Kafka, Redis |
| **Cómo aprende** | Construyendo paso a paso — necesita explicaciones mientras implementa |
| **Dedicación** | 5-10 horas/semana (~1-2h/día) |
| **Tiempo total** | ~24 semanas (Oct 2026 → Mar/Abr 2027), ~120-240 horas |

**Importante para el agente**: Christian está aprendiendo. Al generar código o dar
instrucciones, explica el POR QUÉ de cada decisión. No asumas conocimientos avanzados.
Los conceptos nuevos deben introducirse uno a uno, no todos a la vez.

### 🤖 Modelo de colaboración con IA — "Arquitecto / Revisor + Memory Bank"

> **Principio**: La IA NO genera código de implementación. Christian escribe TODO.
> La IA diseña, especifica contratos, y destruye el código cuando Christian se lo trae.

#### Memory Bank y Handoffs

La carpeta `.ai/` contiene el Memory Bank del proyecto:
- `.ai/reglas.md` — Cómo colaborar (la IA lo lee SIEMPRE al inicio)
- `.ai/estado.md` — Estado actual y punto de reanudación (se actualiza cada sesión)
- `.ai/contexto.md` — Resumen compacto del proyecto (la IA lo lee solo la primera vez)
- `.ai/errores.md` — Log de errores encontrados y cómo se resolvieron
- `.ai/decisiones.md` — Decisiones arquitectónicas (ADRs)

La IA **NO lee el repositorio automáticamente**. Christian trae el código que quiere revisar.

#### Flujo de trabajo por cada tarea

| Fase | Quién | Qué hace |
|------|-------|----------|
| **1. Diseño** | IA | Explica el concepto teórico. Define el contrato: interfaces, firmas de métodos, responsabilidades, invariantes. NO genera implementación |
| **2. Implementación** | Christian | Escribe el código siguiendo la especificación. Se come los efectos cascada de sus propios cambios |
| **3. "Destroy my code"** | IA | Christian trae su código y la IA lo destroza: ¿dónde rompe DDD? ¿ha acoplado infraestructura en el dominio? ¿viola SOLID? |
| **4. Iteración** | Christian | Corrige según el feedback. Vuelve a traer el código. Repetir hasta que esté limpio |
| **5. Handoff** | Ambos | Se actualiza `.ai/estado.md` con el estado exacto para la siguiente sesión |

> Ver reglas completas en `.ai/reglas.md` y `GEMINI.md`.

---

### Alcance actual del roadmap

| Incluido | NO incluido (futuro) |
|----------|---------------------|
| Backend completo (API REST) | Frontend móvil (Android/Kotlin) |
| Arquitectura Hexagonal + DDD + SOLID | Frontend escritorio (JavaFX) |
| PostgreSQL + Flyway | Integración con Odoo (ERP) |
| Docker + Docker Compose | ELK Stack |
| Kafka + Redis (avanzado) | Kubernetes (post-entrega) |
| Testing (JUnit, ArchUnit) | |
| Seguridad JWT | |

### Decisiones arquitectónicas INAMOVIBLES

| Decisión | Por qué |
|----------|---------|
| **Arquitectura Hexagonal (Ports & Adapters)** | Desacoplar dominio de infraestructura. El dominio no depende de nada externo |
| **DDD Táctico** | Aggregates, Value Objects, Domain Events, Domain Services. La lógica de negocio vive en el dominio |
| **SOLID estricto** | Los 5 principios aplicados rigurosamente. Interface Segregation en puertos, Constructor Injection siempre |
| **Java 21 LTS** | Última LTS. Virtual Threads, Pattern Matching completo, Records, Sealed interfaces |
| **Spring Boot 4.1.1** | Framework principal del ecosistema Java enterprise |
| **PostgreSQL 16** | Base de datos relacional robusta, estándar en banca — se usará en AWS al final del curso |
| **Flyway** | Migraciones versionadas. NUNCA `ddl-auto: update/create` en desarrollo real |
| **Maven** | Build tool. Ya configurado en el proyecto |

### ⚠️ Decisión sobre base de datos (Octubre 2026)

> **H2 durante todo el desarrollo local. PostgreSQL en AWS al final del curso.**
>
> - Durante las Fases 0–8 se usa **H2 en memoria** para no necesitar Docker/PostgreSQL instalado localmente.
> - Flyway se activa y las migraciones SQL se escriben desde el principio (compatibles con PostgreSQL).
> - Al final del curso se despliega en **AWS** con PostgreSQL 16 real — el cambio será mínimo gracias a la arquitectura hexagonal (solo cambia la configuración, no el código).
> - El `docker-compose.yml` con PostgreSQL queda como referencia pero **no se usa en desarrollo local**.

### Stack tecnológico

```
CORE (obligatorio):
├── Java 21 LTS
├── Spring Boot 4.1.1
├── Spring Data JPA + Hibernate
├── PostgreSQL 16
├── Flyway (migraciones de BBDD)
├── Docker + Docker Compose
├── JUnit 5 + AssertJ + Mockito
└── ArchUnit (validación de arquitectura)

AVANZADO (si da tiempo):
├── Apache Kafka (eventos asíncronos)
└── Redis (caché)

ASPIRACIONAL (post-entrega):
├── Kubernetes
├── ELK Stack
└── Odoo (integración ERP)
```

### Estado actual del proyecto (Octubre 2026)

```
NeoBank/
├── src/main/java/org/neobank/
│   ├── NeoBankApplication.java
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Account.java                          ← Aggregate básico (pendiente refactorizar a Lote 4)
│   │   │   ├── Money.java                            ← Value Object completo (final, escala 4, equals/hashCode, of/zero)
│   │   │   ├── AccountId.java                        ← Value Object record tipado (UUID)
│   │   │   ├── UserId.java                           ← Value Object record tipado (UUID)
│   │   │   └── AccountStatus.java                    ← Enum rico con allowsOperations() y canBeReactivated()
│   │   └── exception/
│   │       ├── DomainException.java                  ← Excepción base abstracta (unchecked)
│   │       ├── AccountNotFoundException.java         ← Mapea a HTTP 404
│   │       ├── AccountNotActiveException.java        ← Mapea a HTTP 409
│   │       ├── CurrencyMismatchException.java        ← Mapea a HTTP 400
│   │       └── InsufficientFundsException.java       ← Mapea a HTTP 422
│   ├── application/
│   │   ├── port/in/CreateAccountUseCase.java          ← Puerto de entrada
│   │   ├── port/out/SaveAccountPort.java              ← Puerto de salida
│   │   └── service/AccountService.java                ← Servicio de aplicación
│   └── infrastructure/
│       ├── adapter/in/rest/
│       │   ├── AccountController.java                 ← Placeholder
│       │   └── CreateAccountResponse.java             ← Placeholder
│       └── adapter/out/persistence/
│           ├── AccountJpaEntity.java                  ← Entidad JPA
│           ├── AccountPersistenceAdapter.java         ← Adaptador JPA
│           └── SpringDataAccountRepository.java       ← Repositorio Spring Data
├── src/main/resources/
│   ├── application.yml                                ← Base (puerto 8080, neobank-core)
│   ├── application-dev.yml                            ← Perfil DEV (H2 en memoria, Flyway ON)
│   ├── application-test.yml                           ← Perfil TEST (H2 en memoria, ddl create-drop)
│   ├── application-prod.yml                           ← Perfil PROD (PostgreSQL AWS RDS, ddl validate)
│   └── db/migration/
│       └── V1__create_accounts_table.sql              ← Migración inicial de tabla accounts
├── docker-compose.yml                                 ← PostgreSQL 16 (referencia AWS)
├── pom.xml                                            ← Java 21 LTS, Spring Boot 4.1.1 (con Flyway y Hikari configurados)
├── GEMINI.md                                          ← Reglas de pairing: consultar antes de modificar código y explicar decisiones
└── ROADMAP.md
```

**Estado técnico**:
- **Compilación & Tests**: En **VERDE** con Java 21 LTS (`JAVA_HOME=/home/chrferlop/.jdks/ms-21.0.12.1 ./mvnw test` pasa al 100%).
- **Flyway**: Probado y validado en tiempo real en arranque local con H2 (ejecuta `V1`, crea `flyway_schema_history` y valida con Hibernate).
- **Perfiles Spring**: Estructura enterprise completa (`application.yml`, `application-dev.yml`, `application-test.yml`, `application-prod.yml`).
- **Dominio**: Lote 1 (Status, IDs, Excepciones) y Lote 2 (Money refactorizado y documentado) completados. CERO imports de Spring/Jakarta/Lombok en `domain/`.
- **Pendiente inmediato para siguiente sesión**: **Fase 1 - Lote 3: Domain Events** (`DomainEvent` sealed interface + records de eventos en `org.neobank.domain.event`).

### Reglas de código estrictas

#### Capa `domain/` — Puro Java, CERO dependencias externas

```
✅ PERMITIDO en domain/:
   - java.util.*, java.math.*, java.time.*
   - Clases propias del dominio

❌ PROHIBIDO en domain/:
   - org.springframework.*
   - jakarta.*
   - lombok.*
   - com.fasterxml.jackson.*
   - Cualquier anotación de framework (@Entity, @Service, @Component, @Autowired...)
```

#### Convenciones generales

- **Inyección**: Constructor injection SIEMPRE. Nunca `@Autowired` en campos
- **Value Objects**: `final`, inmutables, con `equals`/`hashCode`, factory methods, auto-validantes
- **Aggregate Root**: Encapsula TODA la lógica de negocio de sus invariantes
- **Use Cases**: Una interfaz = una acción (Interface Segregation Principle)
- **Idioma**: Código en INGLÉS, comentarios/documentación en ESPAÑOL
- **DTOs y Commands**: Usar `record` de Java 21
- **Sealed interfaces**: Donde tenga sentido semántico (Domain Events, tipos de transacción)
- **Optional**: Para retornos que pueden no existir. Nunca devolver `null`
- **Excepciones**: Excepciones de dominio propias que extienden `DomainException`. Nunca `RuntimeException` genérica
- **Lombok**: SOLO en `infrastructure/` (entidades JPA, DTOs REST). Prohibido en `domain/` y `application/`
- **Naming de tests**: `should_[resultado]_when_[condición]`
- **Servicios de dominio y aplicación**: NO llevan `@Service`. Se registran como `@Bean` en `BeanConfiguration`

#### Estructura de paquetes canónica

```
org.neobank/
├── domain/                              ← CERO dependencias externas
│   ├── model/                           ← Aggregates, Entities, Value Objects, Enums
│   ├── event/                           ← Domain Events (sealed interface + records)
│   ├── exception/                       ← Excepciones del dominio
│   └── service/                         ← Domain Services (solo si la lógica no cabe en un Aggregate)
│
├── application/                         ← Solo depende de domain/
│   ├── port/
│   │   ├── in/                          ← Use Cases (interfaces) + Commands (records)
│   │   └── out/                         ← Puertos de salida (interfaces)
│   └── service/                         ← Implementación de los Use Cases (orquestación pura)
│
└── infrastructure/                      ← Depende de todo (Spring, JPA, Kafka...)
    ├── adapter/
    │   ├── in/                          ← REST controllers, Kafka listeners
    │   │   └── rest/                    ← Controllers + DTOs de request/response
    │   └── out/                         ← JPA adapters, Kafka publishers, Redis cache
    │       ├── persistence/             ← JPA entities, mappers, repositories, adapters
    │       ├── messaging/               ← Kafka publisher (futuro)
    │       └── cache/                   ← Redis adapter (futuro)
    └── config/                          ← BeanConfiguration, SecurityConfig
```

#### Regla de dependencias — NUNCA violar

```
domain/          → NO importa NADA externo. Cero frameworks.
application/     → Solo importa domain/
infrastructure/  → Importa application/ y domain/ (e implementa sus interfaces)
```

#### Modelo de dominio objetivo

```
Account (Aggregate Root)
├── AccountId          (Value Object — wrapper de UUID)
├── UserId             (Value Object — wrapper de UUID)
├── Money balance      (Value Object — BigDecimal + Currency, con add/subtract)
├── AccountStatus      (Enum — ACTIVE, BLOCKED, CLOSED, con allowsOperations())
├── Currency           (java.util.Currency)
├── Instant createdAt
│
├── open()             → Factory method, crea cuenta con balance cero
├── reconstitute()     → Reconstruir desde persistencia (sin eventos)
├── deposit(Money)     → Valida cuenta activa, moneda, importe positivo
├── withdraw(Money)    → Valida fondos suficientes
├── block(reason)      → Cambia estado
├── unblock()          → Reactiva desde BLOCKED
├── close()            → Solo si balance == 0
└── pullDomainEvents() → Devuelve y limpia los eventos acumulados

Domain Events (sealed interface):
├── AccountOpenedEvent
├── MoneyDepositedEvent
├── MoneyWithdrawnEvent
├── AccountBlockedEvent
└── AccountClosedEvent

Domain Exceptions (extienden DomainException):
├── InsufficientFundsException
├── AccountNotActiveException
├── AccountNotFoundException
└── CurrencyMismatchException

Domain Services:
└── TransferDomainService.transfer(source, target, amount)

Use Cases (interfaces en application/port/in/):
├── OpenAccountUseCase
├── DepositMoneyUseCase
├── WithdrawMoneyUseCase
├── TransferMoneyUseCase
├── GetAccountUseCase
└── BlockAccountUseCase

Puertos de salida (interfaces en application/port/out/):
├── SaveAccountPort
├── LoadAccountPort
└── PublishDomainEventPort
```

### Perfiles de Spring

| Perfil | Base de datos | Flyway | Hibernate DDL | Uso |
|--------|--------------|--------|---------------|-----|
| `dev` | H2 en memoria | ✅ Activo | `validate` | Desarrollo local rápido y seguro |
| `test` | H2 en memoria | ❌ Desactivado | `create-drop` | Tests automáticos aislados |
| `prod` | PostgreSQL (AWS RDS) | ✅ Activo | `validate` | Despliegue en la nube (datos reales seguros) |

---

## 📋 Fases del roadmap

### Fase 0 — Cimientos: Setup profesional
> ⏱️ **Semanas 1-2** (~15h) · 🔴 Obligatorio

Montar el entorno de desarrollo real: conectar base de datos, activar Flyway con la primera migración, configurar perfiles Spring (`dev`/`test`), y verificar que todo está listo.

- [x] Java 21 LTS verificado y Maven en verde (`./mvnw test` ejecutándose sin fallos)
- [x] `application.yml` base configurado (nombre y puerto)
- [x] Entidad JPA sincronizada (`AccountJpaEntity` con currency, status, createdAt)
- [x] Adaptador de persistencia (`AccountPersistenceAdapter`) sincronizado con `Money` y `currency`
- [x] Crear perfiles: `application-dev.yml` (H2 + Flyway) y `application-test.yml` (H2 tests)
- [x] `ddl-auto: validate` en dev, `create-drop` solo en test
- [x] Primera migración Flyway: `V1__create_accounts_table.sql`
- [x] La app compila con Java 21 (`JAVA_HOME=/home/chrferlop/.jdks/ms-21.0.12.1 ./mvnw compile`)

> 💡 **Contexto al retomar en la siguiente sesión (en nuevo chat)**:
> - **Fase 0** cerrada al 100%.
> - **Fase 1 (En progreso)**:
>   - ✅ Creados: `AccountStatus.java` (Enum rico), `AccountId.java` y `UserId.java` (Records tipados).
>   - ✅ Creada jerarquía de excepciones de dominio: `DomainException.java`, `AccountNotFoundException.java`, `AccountNotActiveException.java`, `CurrencyMismatchException.java`, `InsufficientFundsException.java`.
>   - ✅ Refactorizado: `Money.java` (Value Object completo con 4 decimales, equals/hashCode por valor, of/zero, operaciones inmutables y validaciones).
>   - 🔴 **Punto exacto de reanudación**: **Fase 1 - Lote 3: Domain Events** (`DomainEvent.java` como `sealed interface` y los records de eventos: `AccountOpenedEvent`, `MoneyDepositedEvent`, `MoneyWithdrawnEvent`, `AccountBlockedEvent`, `AccountClosedEvent`).
> - **Modelo de colaboración**: Patrón "Arquitecto / Revisor + Memory Bank" — la IA diseña contratos y destruye código; Christian escribe TODA la implementación y se come los efectos cascada. Cero lectura automática del repo (ver `.ai/reglas.md` y `GEMINI.md`).

---

### Fase 1 — Dominio: DDD desde las bases
> ⏱️ **Semanas 3-6** (~30h) · 🔴 Obligatorio

Construir toda la capa `domain/` con Java puro: Value Objects ricos (Money, AccountId,
UserId), Aggregate Root (Account) con toda la lógica de negocio, Domain Events como sealed
interface, excepciones de dominio propias, y el Domain Service de transferencias.

- [x] `Money.java` — Value Object completo: final, inmutable, factory methods, add/subtract, equals por valor, BigDecimal con escala fija
- [x] `AccountId.java` y `UserId.java` — records con validación de no-nulo y factory methods
- [x] `AccountStatus.java` — enum con `allowsOperations()` y `canBeReactivated()`
- [x] `DomainException.java` — clase base abstracta (extends RuntimeException)
- [x] `InsufficientFundsException`, `AccountNotActiveException`, `CurrencyMismatchException`, `AccountNotFoundException`
- [ ] `DomainEvent` — sealed interface con `occurredOn()`
- [ ] `AccountOpenedEvent`, `MoneyDepositedEvent`, `MoneyWithdrawnEvent`, `AccountBlockedEvent`, `AccountClosedEvent` — records
- [ ] `Account.java` — Aggregate Root con `open()`, `reconstitute()`, `deposit()`, `withdraw()`, `block()`, `unblock()`, `close()`, `pullDomainEvents()`
- [ ] `TransferDomainService.java` — lógica de transferencia entre dos cuentas
- [ ] **Verificar: CERO imports de Spring/Jakarta/Lombok en `domain/`**

---

### Fase 2 — Aplicación: Use Cases y SOLID
> ⏱️ **Semanas 7-9** (~22h) · 🔴 Obligatorio

Construir la capa `application/`: Use Cases como interfaces segregadas (una por acción),
Commands como records con validación, puertos de salida, y Application Services que solo
orquestan (cargar → delegar al dominio → persistir → publicar eventos).

- [ ] Puertos de entrada: `OpenAccountUseCase`, `DepositMoneyUseCase`, `WithdrawMoneyUseCase`, `TransferMoneyUseCase`, `GetAccountUseCase`, `BlockAccountUseCase`
- [ ] Commands: `OpenAccountCommand`, `DepositMoneyCommand`, `WithdrawMoneyCommand`, `TransferMoneyCommand`, `BlockAccountCommand` — todos como records con validación
- [ ] Response: `AccountResponse` record con factory method `from(Account)`
- [ ] Puertos de salida: `SaveAccountPort`, `LoadAccountPort`, `PublishDomainEventPort`
- [ ] `AccountService` — implementa los Use Cases de cuenta (solo orquestación, cero lógica de negocio)
- [ ] `TransferService` — usa `TransferDomainService` para transferencias
- [ ] **Verificar: ningún servicio contiene if/else de lógica de negocio**
- [ ] **Verificar: constructor injection en todos los servicios**

---

### Fase 3 — Infraestructura: Adaptadores
> ⏱️ **Semanas 10-12** (~22h) · 🔴 Obligatorio

Conectar el dominio con el mundo real: adaptador de persistencia JPA (entidad + mapper +
adapter), publicador de eventos con Spring Application Events, controller REST con endpoints,
manejo global de errores, y `BeanConfiguration` para registrar beans sin anotaciones.

- [ ] `AccountJpaEntity` — entidad JPA con Lombok (aquí SÍ se permite)
- [ ] `AccountMapper` — traducción bidireccional entre dominio y JPA
- [ ] `AccountPersistenceAdapter` — implementa `SaveAccountPort` y `LoadAccountPort`
- [ ] `SpringEventPublisher` — implementa `PublishDomainEventPort` con Spring Application Events
- [ ] `AccountController` — endpoints: POST crear, POST depositar, POST retirar, GET consultar
- [ ] DTOs REST: `CreateAccountRestRequest`, `CreateAccountRestResponse`, `MoneyOperationRestRequest`
- [ ] `GlobalExceptionHandler` — mapea excepciones de dominio a HTTP (404, 400, 409, 422)
- [ ] `BeanConfiguration` — registra servicios de dominio y aplicación como `@Bean`
- [ ] Probar con curl/Postman: flujo completo crear → depositar → retirar → consultar
- [ ] Nueva migración Flyway si la tabla cambió

---

### Fase 4 — Testing: Verificar todo
> ⏱️ **Semanas 13-15** (~22h) · 🔴 Obligatorio

Tests unitarios del dominio (los más importantes, rápidos, sin Spring), tests de Application
Service con mocks de Mockito, y ArchUnit para validar automáticamente las reglas de arquitectura.

- [ ] `MoneyTest` — crear, add, subtract, equals por valor, diferentes monedas, nulls
- [ ] `AccountTest` — open, deposit, withdraw, block, close, eventos, edge cases
- [ ] `AccountIdTest` — validación, generate, equals
- [ ] `AccountServiceTest` — con mocks de `SaveAccountPort`, `LoadAccountPort`, `PublishDomainEventPort`
- [ ] `TransferServiceTest` — transfer entre cuentas, cuenta no encontrada
- [ ] `ArchitectureTest` — ArchUnit: domain no depende de Spring/Jakarta/infrastructure, application no depende de infrastructure
- [ ] AssertJ y ArchUnit como dependencias en `pom.xml`
- [ ] `./mvnw test` pasa sin errores
- [ ] Cobertura > 80% en `domain/` y `application/`

---

### Fase 5 — Seguridad: JWT y Spring Security
> ⏱️ **Semanas 16-17** (~15h) · 🟡 Importante

Autenticación con JWT: registro de usuario, login que devuelve token, y protección de
todos los endpoints `/api/v1/accounts/**`. Roles USER y ADMIN.

- [ ] Migración Flyway `V2__create_users_table.sql`
- [ ] Use Cases: `RegisterUserUseCase`, `LoginUseCase`
- [ ] `SecurityConfig` con filtro JWT
- [ ] `JwtTokenProvider` — generación y validación de tokens
- [ ] `AuthController` — `/api/v1/auth/register` y `/api/v1/auth/login`
- [ ] Endpoints de cuentas protegidos (requieren JWT)
- [ ] Tests de autenticación

---

### Fase 6 — Docker y DevOps
> ⏱️ **Semanas 18-19** (~15h) · 🔴 Obligatorio

Dockerizar la aplicación completa: Dockerfile multi-stage (build + runtime), Docker Compose
con PostgreSQL + app, y verificar que todo funciona con un solo `docker compose up --build`.

- [ ] `Dockerfile` multi-stage (JDK para build, JRE para runtime)
- [ ] `.dockerignore` (ignorar `.git`, `target/`, `.idea/`)
- [ ] `docker-compose.yml` con PostgreSQL + neobank-app
- [ ] `docker compose up --build` levanta todo y la app responde
- [ ] Flyway se ejecuta automáticamente dentro de Docker

---

### Fase 7 — Kafka: Eventos asíncronos
> ⏱️ **Semanas 20-22** (~18h) · 🟡 Avanzado

Reemplazar `SpringEventPublisher` por `KafkaEventPublisher` sin tocar `domain/` ni
`application/`. Los Domain Events se publican en topics de Kafka. Consumer de ejemplo.

- [ ] Kafka en `docker-compose.yml`
- [ ] `spring-kafka` en `pom.xml`
- [ ] `KafkaEventPublisher` implementando `PublishDomainEventPort`
- [ ] Topics por tipo de evento (usando pattern matching con sealed interface)
- [ ] Consumer de ejemplo (log de eventos)
- [ ] **Verificar: `domain/` y `application/` sin cambios (git diff)**

---

### Fase 8 — Redis: Caché
> ⏱️ **Semanas 23-24** (~12h) · 🟡 Avanzado

Cachear consultas frecuentes de cuentas con Redis. Implementar como decorador del
adaptador de persistencia (Open/Closed Principle).

- [ ] Redis en `docker-compose.yml`
- [ ] `spring-boot-starter-data-redis` en `pom.xml`
- [ ] `CachedAccountAdapter` — patrón Decorator sobre el adaptador JPA
- [ ] Invalidación de caché al modificar una cuenta
- [ ] Tests de caché hit/miss

---

### Pulido y documentación final
> ⏱️ **Semana 24+** (~10h) · 🔴 Obligatorio

- [ ] `README.md` profesional (descripción, arquitectura, cómo ejecutar, API docs)
- [ ] Diagrama de arquitectura
- [ ] OpenAPI/Swagger para documentar la API
- [ ] Revisión de código: eliminar código muerto, TODOs, comentarios obsoletos
- [ ] Conventional commits en el historial de git
- [ ] Presentación para la defensa del intermodular

---

## 📅 Calendario visual

```
Oct 2026                                                         Mar/Abr 2027
  │                                                                   │
  ▼                                                                   ▼
  ┌──────┬──────────┬────────┬────────┬────────┬──────┬──────┬──────┬──────┬──────┐
  │ F0   │   F1     │  F2    │  F3    │  F4    │ F5   │ F6   │ F7   │ F8   │  📦  │
  │Setup │ Dominio  │UseCases│Adapters│Testing │ JWT  │Docker│Kafka │Redis │Polish│
  │ 2sem │  4 sem   │ 3 sem  │ 3 sem  │ 3 sem  │2 sem │2 sem │3 sem │2 sem │ 1+   │
  └──────┴──────────┴────────┴────────┴────────┴──────┴──────┴──────┴──────┴──────┘
  🔴 CORE OBLIGATORIO ─────────────────────────────────────────  🟡 AVANZADO ────

  Si vas corto de tiempo, las fases 7 y 8 se pueden recortar o posponer.
  Las fases 0-6 son el MÍNIMO para un proyecto de portfolio sólido.
```

---

## 📚 Recursos de referencia

| Recurso | Para qué |
|---------|----------|
| **Get Your Hands Dirty on Clean Architecture** (Tom Hombergs) | Hexagonal + Spring Boot — lectura principal |
| **Domain-Driven Design Distilled** (Vaughn Vernon) | DDD resumido y práctico |
| `thombergs/buckpal` (GitHub) | Repo de referencia de hexagonal con Spring Boot |
| baeldung.com | Recetas de Spring Boot |
| reflectoring.io | Artículos de hexagonal + Spring |

---

## ✅ Progreso general

- [x] **Fase 0** — Setup (Perfiles, Flyway, Base de datos en memoria lista)
- [ ] **Fase 1** — Dominio DDD (En progreso: Lotes 1 y 2 completados, pendiente Lote 3 Domain Events)
- [ ] **Fase 2** — Aplicación (Use Cases, Commands, Services)
- [ ] **Fase 3** — Infraestructura (REST, JPA, Events, Config)
- [ ] **Fase 4** — Testing (dominio, aplicación, ArchUnit)
- [ ] **Fase 5** — Seguridad JWT
- [ ] **Fase 6** — Docker
- [ ] **Fase 7** — Kafka
- [ ] **Fase 8** — Redis
- [ ] **Pulido** — README, docs, presentación
