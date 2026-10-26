# NeoBank — Estado actual

> Última actualización: 26 Oct 2026 — entrada común y bloques guiados, autorizados por Christian.

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

### Evidencias y límites de verificación

- **26 Oct 2026, revisión técnica anterior de esta conversación:** `JAVA_HOME=/home/chrferlop/.jdks/ms-21.0.12.1 ./mvnw -o test` terminó en `BUILD SUCCESS`: 1 test, 0 fallos, 0 errores. La compilación fue con Java 21.
- El único test es `NeoBankApplicationTests.contextLoads`, con perfil `test`. No demuestra las invariantes bancarias, las migraciones ni compatibilidad con PostgreSQL.
- El estado anterior registraba Flyway probado en H2/dev. No se repitió esa prueba en esta revisión documental. Los perfiles observados y el objetivo de producción se distinguen en `contexto.md`.
- La copia local analizada corresponde al commit `d40443f`, con una modificación previa de Christian en `CreateAccountUseCase.java`. No se pudo comprobar la versión remota de GitHub por falta de conexión.
- La primera revisión documental de esta conversación modificó los seis Markdown de `.ai`. La consolidación posterior actualiza la documentación, crea `AGENTS.md` y retira los dos documentos anteriores. Ninguna de las dos implementa funcionalidades ni vuelve a ejecutar pruebas de la aplicación.

### Progreso de aprendizaje

- **Explicaciones registradas previamente:** Hexagonal, Port, Adapter, Regla de dependencias, DDD, Value Object, Domain Exception, Inmutabilidad, Fábrica estática (antes titulada Factory Method), Record, Enum rico, Objeto autovalidante, Inyección por constructor, ISP, SRP y Flyway.
- No hay evidencia registrada por concepto de aplicación autónoma ni recuperación posterior. Esto indica falta de registro, no demuestra que Christian no lo sepa.
- Las aclaraciones técnicas de la IA en `glosario.md` están pendientes de reformulación o práctica por Christian; no contarlas como aprendizajes demostrados.
- **Conceptos pendientes de lo ya programado:** Primitive Obsession e IDs tipados, Invariante de dominio, Precisión monetaria, DIP, Entidad Dominio vs Entidad JPA y BeanConfiguration.
- **Próximos lotes:** Domain Events (Lote 3) y Aggregate Root (Lote 4).

### Punto exacto de reanudación: primer bloque guiado

Se mantiene **Fase 1 — Lote 3: Domain Events**. Al recibir «seguimos», la IA debe preparar un único bloque completo sobre el hecho de abrir una cuenta, con el formato de `reglas.md`; no iniciar una cadena de preguntas para que Christian descubra solo qué hacer.

- **Objetivo del bloque:** comprender un evento de dominio y representar una apertura de cuenta con datos justificados.
- **Preparación de la IA:** explicar el concepto con un ejemplo sencillo, aportar un escenario de apertura satisfactoria y otro de rechazo, señalar los archivos relevantes y dar pasos con resultados esperados.
- **Trabajo de Christian:** usar esa guía para proponer los datos del hecho, justificar su significado e implementar el primer contrato con una comprobación adecuada a lo aprendido. La IA proporciona la ayuda necesaria si no conoce aún alguna construcción de Java.
- **Límite del bloque:** representar un hecho; no implementar todavía Kafka ni una infraestructura de publicación. Aclarar brevemente que crear un evento no garantiza su persistencia ni su entrega.
- **Revisión:** Christian puede decir «terminado, revisa». Consultar los cambios relevantes y dar una corrección concreta o preparar la continuación; no pedir que copie el repositorio si ya se tiene acceso.

**Plan técnico conservado:** `DomainEvent` como sealed interface con `occurredOn()` en `domain/event/`, y los records AccountOpenedEvent, MoneyDepositedEvent, MoneyWithdrawnEvent, AccountBlockedEvent y AccountClosedEvent. Es una previsión que se debe comprender y justificar; aún no está implementada.

**Criterio de aprendizaje del ejercicio:** explicar por qué se representa ese hecho, justificar sus datos y razonar qué sucede si la operación se rechaza. En otra sesión, aplicar la idea a una operación distinta y registrar la ayuda necesaria cuando esté autorizada la actualización documental.

### Pendiente en Fase 1 después del Lote 3

- Lote 4: Refactorizar Account.java (Aggregate Root completo con AccountId, UserId, AccountStatus, eventos, open/reconstitute/block/unblock/close/pullDomainEvents)
- Lote 5: TransferDomainService

### Deuda técnica conocida

- Account.java usa UUID directamente (pendiente migrar a AccountId/UserId en Lote 4)
- AccountService.java usa @Service y Lombok (debe pasar a @Bean en BeanConfiguration, Fase 3)
- AccountPersistenceAdapter tiene status hardcodeado "ACTIVE" y createdAt como Instant.now() (se arregla cuando Account esté completo)
- La revisión estática del 26 Oct identificó que Account no protege importes positivos ni fondos suficientes; completar esas invariantes y sus pruebas en el refactor del dominio.
- Antes del despliegue: resolver la configuración de migraciones en producción y comprobar PostgreSQL. Las credenciales incrustadas detectadas en el perfil prod requieren configuración externa y rotación si son reales; no copiar sus valores a la memoria.

### Dudas abiertas y alcance siguiente

- Ajustar el apoyo según los intentos y la revisión del bloque, sin exigir un examen previo ni una propuesta arquitectónica desde cero para un concepto nuevo.
- Las convenciones técnicas se mantienen; las recomendaciones nuevas no se consideran decisiones aceptadas hasta acordarlas.
- Una próxima tutoría o revisión no autoriza actualizar archivos automáticamente. Entregar el cierre en el chat salvo que Christian autorice guardar los avances.

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
