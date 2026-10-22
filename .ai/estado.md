# NeoBank — Estado actual

> Última actualización: 21 Oct 2026

## Fase actual: 1 — Dominio DDD (En progreso)

### Completado
- [x] **Fase 0** — Setup completo (perfiles, Flyway, H2, compilación verde)
- [x] **Fase 1 Lote 1** — AccountStatus (enum rico), AccountId y UserId (records tipados)
- [x] **Fase 1 Lote 2** — Money (Value Object completo: 4 decimales, inmutable, factory methods, operaciones)
- [x] **Fase 1 Lote 2** — Excepciones de dominio (DomainException, 4 excepciones concretas)

### Punto exacto de reanudación
**Repaso de conceptos / Glosario (en progreso):**
- Glosario completado: Arquitectura (Hexagonal, Port, Adapter, Regla dependencias), DDD Táctico (DDD, Value Object, Domain Exception), Java/Patrones (Inmutabilidad, Factory Method, Record, Enum rico, Objeto auto-validante, Inyección por constructor), SOLID (ISP, SRP), Persistencia (Flyway).
- Pendientes de lo ya programado: Primitive Obsession e IDs tipados, Invariante de dominio, Precisión monetaria (BigDecimal vs float/double), DIP, Entidad Dominio vs Entidad JPA, BeanConfiguration.
- Pendientes para próximos lotes: Aggregate Root (Lote 4), Domain Events (Lote 3).

**Fase 1 — Lote 3: Domain Events (código pendiente)**
- Crear `DomainEvent` como sealed interface con `occurredOn()` en `domain/event/`
- Crear 5 records: AccountOpenedEvent, MoneyDepositedEvent, MoneyWithdrawnEvent, AccountBlockedEvent, AccountClosedEvent

### Pendiente en Fase 1 después del Lote 3
- Lote 4: Refactorizar Account.java (Aggregate Root completo con AccountId, UserId, AccountStatus, eventos, open/reconstitute/block/unblock/close/pullDomainEvents)
- Lote 5: TransferDomainService

### Deuda técnica conocida
- Account.java usa UUID directamente (pendiente migrar a AccountId/UserId en Lote 4)
- AccountService.java usa @Service y Lombok (debe pasar a @Bean en BeanConfiguration, Fase 3)
- AccountPersistenceAdapter tiene status hardcodeado "ACTIVE" y createdAt como Instant.now() (se arregla cuando Account esté completo)

### Ficheros que existen
```
src/main/java/org/neobank/
├── domain/model/        → Account.java, Money.java, AccountId.java, UserId.java, AccountStatus.java
├── domain/exception/    → DomainException.java, AccountNotFoundException, AccountNotActiveException, CurrencyMismatchException, InsufficientFundsException
├── application/port/in/ → CreateAccountUseCase.java
├── application/port/out/→ SaveAccountPort.java
├── application/service/ → AccountService.java
└── infrastructure/      → AccountController.java (placeholder), AccountJpaEntity.java, AccountPersistenceAdapter.java, SpringDataAccountRepository.java
```
