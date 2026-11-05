# NeoBank — Estado actual

> Última actualización: 5 Nov 2026 — cierre de sesión y punto de reanudación guardados a petición de Christian.

## Fase actual: 1 — Dominio DDD (En progreso)

### Progreso técnico registrado

Se conservan los hitos del estado del 21 Oct. Un hito de implementación no acredita dominio del concepto ni pruebas de todos sus comportamientos.

- [x] **Fase 0** — Setup registrado como completado (perfiles, Flyway, H2, compilación verde). Ver límites de verificación debajo.
- [x] **Fase 1 Lote 1** — AccountStatus (enum rico), AccountId y UserId (records tipados) implementados.
- [x] **Fase 1 Lote 2** — Money implementado (4 decimales, inmutable, fábricas estáticas, operaciones); reglas de negocio aún sin tests específicos.
- [x] **Fase 1 Lote 2** — DomainException y cuatro excepciones concretas implementadas.
- [x] **26 Oct** — Protocolo de colaboración y memoria revisados con autorización. ADR-005 sustituye ADR-004. Su eficacia pedagógica queda pendiente de práctica.
- [x] **26 Oct, continuación** — Creado `AGENTS.md` con instrucciones y prompt de inicio; perfil y plan por fases consolidados en `contexto.md`. Retirados los antiguos `GEMINI.md` y `ROADMAP.md` tras conservar su contenido útil. Ver ADR-006.
- [x] **26 Oct, preferencia de trabajo** — La IA prepara bloques guiados completos para que Christian trabaje entre revisiones sin preguntar cada paso. Se mantiene la ayuda gradual y se concreta el formato en `reglas.md`.

- [x] **5 Nov — Lote 3:** observados `DomainEvent` sealed y los cinco records de eventos, con 18 tests de datos y rechazo de nulos. Christian comunica que ha mergeado la rama de eventos y creado otra.
- [ ] **5 Nov — Lote 4 en curso:** rama local `feature/account-aggregate`. `Account.java` tiene cambios sin commit: IDs tipados, estado, fecha `createdAt`, constructor privado común, fábricas `open`/`reconstitute` y getters. `open` genera identidad y fecha, saldo cero y estado activo; `reconstitute` conserva los datos recibidos.
- **Autoría y ayuda:** Christian implementó la reconstrucción de la clase y retiró el constructor duplicado. A petición explícita, la IA renombró `accountid`/`ocurredOn`, añadió los cinco `Objects.requireNonNull` al constructor y los getters `getStatus()`/`getCreatedAt()`.

### Evidencias y límites de verificación

- **26 Oct 2026, revisión técnica anterior de esta conversación:** `JAVA_HOME=/home/chrferlop/.jdks/ms-21.0.12.1 ./mvnw -o test` terminó en `BUILD SUCCESS`: 1 test, 0 fallos, 0 errores. La compilación fue con Java 21.
- En aquella ejecución, el único test era `NeoBankApplicationTests.contextLoads`, con perfil `test`. No demuestra las invariantes bancarias, las migraciones ni compatibilidad con PostgreSQL.
- El estado anterior registraba Flyway probado en H2/dev. No se repitió esa prueba en esta revisión documental. Los perfiles observados y el objetivo de producción se distinguen en `contexto.md`.
- En la revisión del 26 Oct, la copia local analizada correspondía al commit `d40443f`, con una modificación previa de Christian en `CreateAccountUseCase.java`. No se pudo comprobar la versión remota de GitHub por falta de conexión.
- La primera revisión documental de esta conversación modificó los seis Markdown de `.ai`. La consolidación posterior actualiza la documentación, crea `AGENTS.md` y retira los dos documentos anteriores. Ninguna de las dos implementa funcionalidades ni vuelve a ejecutar pruebas de la aplicación.

- **5 Nov:** revisión por lectura; no se ejecutaron compilación ni tests. El informe existente consultado solo acredita un test de arranque anterior, no los 18 tests de eventos. La consulta a GitHub falló por DNS; el merge lo comunica Christian.
- **Estado transitorio:** `AccountService` y `AccountPersistenceAdapter` aún usan el contrato anterior; la migración de `Account` no está integrada y se esperan errores de compilación hasta adaptar esos consumidores. No se han añadido tests de `Account`. Las ediciones de esta sesión pasaron `git diff --check`.

### Progreso de aprendizaje

- **Explicaciones registradas previamente:** Hexagonal, Port, Adapter, Regla de dependencias, DDD, Value Object, Domain Exception, Inmutabilidad, Fábrica estática (antes titulada Factory Method), Record, Enum rico, Objeto autovalidante, Inyección por constructor, ISP, SRP y Flyway.
- No hay evidencia registrada por concepto de aplicación autónoma ni recuperación posterior. Esto indica falta de registro, no demuestra que Christian no lo sepa.
- Las aclaraciones técnicas de la IA en `glosario.md` están pendientes de reformulación o práctica por Christian; no contarlas como aprendizajes demostrados.
- **Conceptos pendientes de lo ya programado:** Primitive Obsession e IDs tipados, Invariante de dominio, Precisión monetaria, DIP, Entidad Dominio vs Entidad JPA y BeanConfiguration.
- **5 Nov, explicación con ayuda:** por qué `AccountId` se genera al abrir y `UserId` se recibe del usuario existente; reconstruir conserva ambas identidades. Explicado que `Money` valida su contenido y `Account` debe rechazar una referencia de saldo nula, ya que esta no ejecuta el constructor de `Money`. No consta todavía comprobación autónoma de estos conceptos.
- **Lote actual:** Aggregate Root (Lote 4).

### Punto exacto de reanudación: adaptar AccountService

**Fase 1 — Lote 4.** Los getters ya están hechos. Al recibir «seguimos», preparar solo un paso pequeño: adaptar `AccountService.create` para convertir el UUID de entrada con `UserId.of`, llamar a `Account.open` y devolver el UUID con `getId().value()`. Mantener por ahora el contrato de `CreateAccountUseCase`. Explicar la conversión antes de pedirla; Christian implementa salvo delegación explícita.

**Preferencia explícita del 5 Nov:** Christian pidió no recibir tantas tareas de golpe. Trabajar en pasos pequeños, uno cada vez; no volver a entregar todo el refactor como bloque inicial. Delegó cambios mecánicos concretos (nombres, validaciones y getters), lo cual no autoriza implementar automáticamente los siguientes pasos.

Después de revisar el servicio, abordar por separado el adaptador: extraer IDs con `.value()` y persistir `getStatus().name()`/`getCreatedAt()` en lugar de valores inventados. Más adelante, pruebas de apertura/reconstrucción, eventos del agregado, invariantes de operaciones y ciclo de vida. No pedir realizar todo junto.

### Pendiente en Fase 1

- Completar Lote 4: integración de Account, pruebas, eventos, protección de depósitos/retiradas y block/unblock/close/pullDomainEvents.
- Lote 5: TransferDomainService.

### Deuda técnica conocida

- Account ya usa AccountId/UserId en los cambios locales; falta adaptar sus consumidores. Quedan imports y comentarios antiguos por limpiar.
- AccountService.java usa @Service y Lombok (debe pasar a @Bean en BeanConfiguration, Fase 3)
- AccountPersistenceAdapter tiene status hardcodeado "ACTIVE" y createdAt como Instant.now() (se arregla cuando Account esté completo)
- La revisión estática del 26 Oct identificó que Account no protege importes positivos ni fondos suficientes; completar esas invariantes y sus pruebas en el refactor del dominio.
- Antes del despliegue: resolver la configuración de migraciones en producción y comprobar PostgreSQL. Las credenciales incrustadas detectadas en el perfil prod requieren configuración externa y rotación si son reales; no copiar sus valores a la memoria.

### Dudas abiertas y alcance siguiente

- **Lectura pendiente solicitada por Christian:** [Diseñar un modelo de dominio de microservicio — Microsoft Learn](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/microservice-domain-model). Guardado al cerrar la sesión para leer al día siguiente. Relacionado con el repaso de una cuenta mutable que sustituye su saldo por nuevos objetos `Money` inmutables; empezar por la sección «The Domain Entity pattern». Lectura aún no realizada.
- Ajustar el apoyo según los intentos y la revisión del bloque, sin exigir un examen previo ni una propuesta arquitectónica desde cero para un concepto nuevo.
- Las convenciones técnicas se mantienen; las recomendaciones nuevas no se consideran decisiones aceptadas hasta acordarlas.
- Una próxima tutoría o revisión no autoriza actualizar archivos automáticamente. Entregar el cierre en el chat salvo que Christian autorice guardar los avances.

### Ficheros que existen

```
src/main/java/org/neobank/
├── domain/event/        → DomainEvent y los cinco records de eventos
├── domain/model/        → Account.java, Money.java, AccountId.java, UserId.java, AccountStatus.java
├── domain/exception/    → DomainException.java, AccountNotFoundException, AccountNotActiveException, CurrencyMismatchException, InsufficientFundsException
├── application/port/in/ → CreateAccountUseCase.java
├── application/port/out/→ SaveAccountPort.java
├── application/service/ → AccountService.java
└── infrastructure/      → AccountController.java (placeholder), AccountJpaEntity.java, AccountPersistenceAdapter.java, SpringDataAccountRepository.java
```
