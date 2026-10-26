# NeoBank — Contexto del proyecto

## Qué es

Backend bancario (API REST) que simula operaciones financieras. Proyecto intermodular de DAM de Christian Fernández. Portfolio profesional. Entrega: Mar/Abr 2027.

## Perfil de aprendizaje y dedicación

- Perfil inicial registrado en octubre de 2026: Java básico-intermedio, algo de JavaFX y Kotlin/Android; principiante en Spring Boot. DDD avanzado, testing, Docker, Kafka y Redis requieren enseñanza gradual. Ajustar la ayuda con evidencias posteriores, sin asumir que el perfil inicial sigue siendo definitivo.
- Aprende construyendo paso a paso y entendiendo el porqué. Prefiere recibir un bloque completo con explicación, pasos y resultados esperados, trabajar por su cuenta y volver para la revisión.
- Dedicación prevista: 5–10 horas por semana, aproximadamente 1–2 horas por sesión. Horizonte inicial de unas 24 semanas, 120–240 horas entre octubre de 2026 y marzo/abril de 2027.
- La IA prepara el siguiente bloque; Christian no tiene que organizar el temario ni preguntar cada paso. No introducir varios conceptos nuevos a la vez si se pueden trabajar de forma gradual.

## Objetivos formativos

- Aprender a descubrir invariantes, repartir responsabilidades y justificar decisiones de arquitectura.
- Implementar y comprobar el comportamiento con autonomía creciente; poder defender el proyecto y sus costes técnicos.
- Usar la IA como tutora y revisora con ayuda gradual. El protocolo vigente es `reglas.md` (ADR-005); la IA no diseña siempre por adelantado.

## Alcance y restricciones acordados

- Backend REST de cuentas, depósitos, retiradas y transferencias; seguridad y despliegue previstos para fases posteriores.
- Desarrollo individual y gradual. Kafka y Redis son ampliaciones opcionales; no condicionan la validez del núcleo.
- Mantener Java 21, Spring Boot, Maven y arquitectura hexagonal con DDD táctico como decisiones actuales del proyecto. Revisar las decisiones con argumentos y acuerdo, no sustituirlas automáticamente.
- Fuera de la entrega actual: frontend Android/Kotlin o JavaFX, integración con Odoo, Kubernetes y ELK. Pueden estudiarse después; no añadirlos como requisitos del bloque actual.

## Plan por fases

Plan consolidado el 26 Oct 2026 a partir de la planificación anterior. Las horas son estimaciones orientativas, no un calendario rígido. El progreso real y el siguiente bloque se mantienen solo en `estado.md`. Las fases orientan a la IA; Christian recibe una tarea manejable cada vez.

| Fase | Resultado previsto | Estimación inicial |
|---|---|---|
| 0 — Entorno | Java 21/Maven, perfiles, H2, entidad y adaptador iniciales, migración de cuentas y arranque comprobado. | 15 h |
| 1 — Dominio | Money, IDs, estados y excepciones; eventos, Account con invariantes y ciclo de vida, y servicio de transferencia. | 30 h |
| 2 — Aplicación | Casos de uso de apertura, depósito, retirada, transferencia, consulta y bloqueo; commands/resultados y puertos de guardar, cargar y publicar eventos. | 22 h |
| 3 — Adaptadores | Mapeo JPA bidireccional, REST y DTOs, traducción de errores, configuración de beans, publicador inicial con Spring Events y migraciones necesarias. Flujo crear → depositar → retirar → consultar comprobado. | 22 h |
| 4 — Consolidación de pruebas | Pruebas de Money, Account, IDs, casos de uso y transferencias; reglas de dependencias con ArchUnit. Completar casos límite e integración pendientes. | 22 h |
| 5 — Seguridad | Registro y login, usuarios y migración, JWT, roles USER/ADMIN y protección de las operaciones de cuentas con pruebas. Rutas previstas: `/api/v1/auth/register`, `/api/v1/auth/login` y `/api/v1/accounts/**`. | 15 h |
| 6 — Despliegue | Dockerfile con etapas de build/runtime, `.dockerignore`, Compose con aplicación y PostgreSQL; arranque con `docker compose up --build`, migraciones y preparación del despliegue en AWS. | 15 h |
| 7 — Kafka, opcional | Adaptador publicador tras el puerto de eventos, topics y consumidor de ejemplo. Evaluar entrega y fallos sin introducir dependencias de Kafka en las capas internas. | 18 h |
| 8 — Redis, opcional | Caché de consultas con adaptador/decorador, invalidación al modificar y pruebas de aciertos/fallos de caché. | 12 h |
| Pulido y defensa | README con ejecución y arquitectura, diagrama, documentación OpenAPI/Swagger, limpieza de código/comentarios y presentación de decisiones y demostración funcional. | 10 h |

- Núcleo de la entrega: fases 0–6 y pulido. Kafka y Redis se recortan o posponen si comprometen el núcleo o el aprendizaje.
- Las pruebas relevantes acompañan cada incremento desde el principio. La fase 4 consolida la verificación; no es el momento de empezar a comprobar por primera vez.
- Se conserva la meta orientativa de cobertura superior al 80 % en dominio/aplicación, como señal secundaria: no sustituye probar invariantes y escenarios de fallo.
- Antes de transferencias completas, definir y comprobar atomicidad y operaciones simultáneas. Antes de producción, verificar migraciones y persistencia contra PostgreSQL y resolver la configuración externa.
- Los nombres previstos de clases o migraciones se concretan en cada bloque según el código existente; evitar fijar un número de migración que pueda estar ocupado. El publicador de eventos no garantiza por sí solo entrega fiable.
- Historial de commits descriptivos; se conserva Conventional Commits como guía. Una revisión o sesión de tutoría no autoriza crear commits.

## Referencias de estudio conservadas

Recursos opcionales de la planificación anterior, para consultar cuando ayuden al bloque actual: *Get Your Hands Dirty on Clean Architecture* (Tom Hombergs), *Domain-Driven Design Distilled* (Vaughn Vernon), el repositorio `thombergs/buckpal` y artículos de Reflectoring/Baeldung. Contrastar detalles de APIs y versiones con documentación oficial. No exigir leerlos completos antes de avanzar.

## Stack y herramientas previstas

- Java 21 LTS, Spring Boot 4.1.1, Spring Data JPA + Hibernate
- H2 en memoria (dev/test) → PostgreSQL 16 en AWS (prod)
- Flyway para migraciones versionadas; en dev, Hibernate valida el esquema. Las excepciones del perfil test se detallan debajo.
- Maven, Lombok limitado a infraestructura, pruebas con JUnit/AssertJ/Mockito; comprobaciones de arquitectura con ArchUnit previstas.
- Seguridad JWT y despliegue con Docker previstos; Kafka y Redis opcionales.

Este listado expresa elecciones y objetivos, no certifica que todo esté implementado o configurado. El avance y sus evidencias están en `estado.md`.

## Arquitectura objetivo y convenciones locales

- Arquitectura hexagonal: aislar el dominio y los casos de uso de los detalles externos mediante puertos y adaptadores. Las carpetas por sí solas no demuestran el aislamiento.
- Dependencias permitidas: `domain/` usa Java estándar y dominio propio; `application/` puede depender de `domain/`; `infrastructure/` conecta las capas internas con frameworks. El código interior no depende de infraestructura.
- `domain/`: sin Spring, Jakarta, Lombok ni otros frameworks. Se permiten los tipos de Java estándar apropiados al problema.
- Value Objects: en este proyecto se usan tipos finales o records, igualdad por valor, validación e inmutabilidad efectiva. Las fábricas estáticas se emplean cuando aportan claridad o control de creación.
- Puertos de entrada: una interfaz por acción es la convención elegida; ISP exige evitar dependencias innecesarias del cliente, no un número fijo de métodos.
- Inyección por constructor; validar dependencias obligatorias cuando corresponda. `final` no implica inmutabilidad del objeto referenciado.
- Servicios de dominio/aplicación sin `@Service`, registrados con `@Bean` en infraestructura. Es la convención de independencia de frameworks acordada aquí.
- Idioma: código en inglés, comentarios/docs en español
- DTOs y Commands: preferir records; proteger componentes mutables cuando existan.
- Fallos de reglas de negocio: excepciones propias que extienden `DomainException`. Distinguirlos de errores técnicos y de uso del API; no transformar automáticamente toda excepción de Java en una de negocio.
- Lombok: solo en `infrastructure/`
- Naming tests: should_[resultado]_when_[condición]

Estas convenciones son acuerdos de NeoBank. Las revisiones deben explicar qué objetivo protegen y diferenciar su incumplimiento de un fallo funcional. Los motivos y condiciones de revisión se registran en `decisiones.md`.

## Estructura de paquetes objetivo

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

Este modelo conserva la planificación actual. Antes de cada implementación, Christian debe justificar las responsabilidades y los datos con escenarios del negocio, con la ayuda necesaria.

## Perfiles: configuración observada y objetivo

Lectura de `src/main/resources/application-*.yml` del 26 Oct 2026; no prueba de despliegue.

| Perfil | Configuración observada | Límite o trabajo pendiente |
|---|---|---|
| dev | H2 en memoria, Flyway activado, Hibernate `validate` | Las migraciones deben comprobarse también en PostgreSQL. |
| test | H2 en memoria, Flyway desactivado, Hibernate `create-drop` | El test de arranque no valida migraciones ni compatibilidad con PostgreSQL. |
| prod | PostgreSQL, Flyway desactivado, Hibernate `validate` | El objetivo documentado era gestionar migraciones con Flyway. Resolver cuándo y dónde se ejecutan antes de un despliegue sobre una base vacía. |

No confundir el objetivo de producción con una capacidad ya verificada. H2 se mantiene en desarrollo según ADR-001; la comprobación contra PostgreSQL debe realizarse antes de considerar preparado el despliegue.
