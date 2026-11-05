# NeoBank — Glosario de Christian

> Explicaciones de Christian y evidencias de aprendizaje. Poder explicar es una evidencia; aplicar y recuperar después requieren comprobaciones distintas.

## Cómo leer y actualizar este glosario

- **Texto registrado** conserva las palabras previas de Christian. Su presencia no certifica dominio ni exactitud completa.
- **Aclaración vigente** identifica una corrección o un límite redactado por la IA el 26 Oct 2026. Cuando existe, corrige o precisa el texto registrado; no leer la frase histórica como definición suficiente. Está pendiente de reformulación o práctica por Christian.
- **Práctica pendiente** es una propuesta, no una actividad realizada ni evidencia de aprendizaje. Elegir las prácticas relevantes para la tarea; no completar toda la lista como requisito para seguir con el proyecto.
- **Evidencia inicial para todas las entradas:** explicación registrada antes de esta revisión; sin registro individual del ejercicio, fecha o ayuda. Aplicación y recuperación posterior no documentadas. Esto no equivale a desconocimiento.
- En futuras actualizaciones autorizadas, añadir a la entrada correspondiente: fecha, ejercicio, ayuda utilizada y resultado observado. Distinguir explicación propia, aplicación con ayuda, aplicación sin ayuda y recuperación posterior. No marcar un concepto como aprendido para siempre.
- Conservar la autoría: una explicación escrita por la IA no se convierte en palabras de Christian. Al reformular él una entrada, registrar esa revisión como suya y conservar el historial en Git.

## Arquitectura

### Arquitectura Hexagonal

**Texto registrado:** Dividir la app en capas (domain, application, infrastructure) conectadas por interfaces, para que no dependan unas de otras y puedas cambiar piezas sin romper el resto.

**Aclaración vigente:** El objetivo es aislar la lógica de la aplicación de los detalles externos mediante puertos y adaptadores. Siguen existiendo dependencias: se controla su dirección. Las tres carpetas son una organización elegida para NeoBank, no una prueba de aislamiento ni un requisito universal del patrón. Cambiar un adaptador exige respetar el contrato y comprobar el comportamiento.

**Práctica pendiente:** Explicar cómo ejecutar un caso de uso con un adaptador de persistencia en memoria y qué dependencias impedirían hacerlo.

### Port

**Texto registrado:** Interfaces que definen qué ofrece la app (port in = casos de uso) y qué necesita (port out = guardar datos, etc). Están en application/.

**Aclaración vigente:** `application/` es la ubicación elegida en NeoBank. Un puerto expresa una interacción con propósito; su contrato incluye comportamiento y condiciones, además de firmas.

**Práctica pendiente:** Proponer qué necesita conocer un caso de uso para guardar una cuenta sin depender de JPA.

### Adapter

**Texto registrado:** Las implementaciones concretas que conectan con el mundo real: controllers (adapter in) y persistencia (adapter out). Están en infrastructure/.

**Aclaración vigente:** Un adaptador de entrada puede invocar un puerto de entrada sin implementar su interfaz Java. Un adaptador de salida puede implementar el puerto que la aplicación necesita. La dirección de la llamada no debe confundirse con la dependencia del código.

**Práctica pendiente:** Seguir una petición de creación de cuenta y explicar qué hace el controller y qué hace el adaptador de persistencia.

### Regla de dependencias

**Texto registrado:** Domain no importa nada. Application solo importa domain. Infrastructure importa todo. Las flechas siempre apuntan hacia dentro.

**Aclaración vigente:** El dominio puede usar Java estándar y tipos propios del dominio. Aplicación puede usar su propio código, Java estándar y dominio. Infraestructura puede depender de las capas internas y frameworks, pero solo debe importar lo necesario. La prohibición relevante es que las capas internas dependan de detalles externos. Estas son las fronteras acordadas para NeoBank.

**Práctica pendiente:** Clasificar dependencias de Java, dominio, Spring y JPA según la capa que las utiliza y justificar la clasificación.

## DDD Táctico

### Domain-Driven Design (DDD)

**Texto registrado:** Pensar primero en qué hace el negocio y cómo es el dominio, modelarlo y meterle su lógica, en vez de crear primero las tablas de la base de datos y programar alrededor de ellas.

**Aclaración vigente:** Esta es una aproximación inicial útil. También requiere aclarar el significado del lenguaje del negocio y revisar el modelo según se aprende; disponer de clases llamadas Aggregate o Value Object no demuestra por sí solo que el modelo represente bien el problema.

**Práctica pendiente:** Describir una regla de retirada con ejemplos antes de decidir qué clases intervienen.

### Value Object

**Texto registrado:** Objeto sin identidad (no tiene ID) e inmutable (si cambia el valor, se crea una instancia nueva). Se auto-valida al crearse y se define exclusivamente por sus datos internos (dos objetos con los mismos valores son idénticos).

**Aclaración vigente:** La igualdad es por valor y significado, no por identidad de referencia. Dos instancias distintas pueden ser iguales. La validación protege las reglas propias del valor; otras reglas pueden pertenecer a la cuenta, como impedir un saldo negativo.

**Práctica pendiente:** Justificar qué condiciones pertenecen a Money y cuáles debe proteger Account.

### Domain Exception

**Texto registrado:** Excepción propia que representa la violación de una regla del negocio (no un fallo técnico). Permite identificar claramente que el error viene del dominio y tratarlo de forma específica.

**Práctica pendiente:** Distinguir una retirada sin fondos de un fallo de conexión a la base de datos y explicar quién debería traducirlos a una respuesta HTTP.

## Java y Patrones

### Inmutabilidad

**Texto registrado:** Propiedad de un objeto cuyo estado no puede cambiar una vez creado (campos final, sin setters). Si se necesita un valor distinto se crea una nueva instancia, evitando efectos secundarios inesperados.

**Aclaración vigente:** Campos finales y ausencia de setters no bastan si el objeto contiene o expone referencias a datos mutables. Debe protegerse el estado observable, incluyendo copias defensivas u otras medidas cuando sean necesarias.

**Práctica pendiente:** Analizar un objeto con un campo final que referencia una lista modificable y proponer cómo proteger su estado.

### Fábrica estática (antes titulada Factory Method)

**Texto registrado:** Método estático que se encarga de instanciar y devolver un objeto en lugar de usar `new` directamente. Permite ocultar el constructor, validar parámetros y usar nombres descriptivos que explican cómo o para qué se crea la instancia (ej. `Money.zero()`).

**Aclaración vigente:** La explicación corresponde a un método fábrica estático. No es el patrón Factory Method de GoF, que permite variar la creación mediante redefinición en subclases. Una fábrica estática puede crear una instancia o devolver una existente; tampoco necesita ocultar siempre el constructor.

**Práctica pendiente:** Justificar qué aporta una fábrica como Money.zero frente a un constructor. Estudiar el patrón GoF aparte cuando exista una necesidad, sin marcarlo como ya aprendido.

### Record (Java 21)

**Texto registrado:** Clase inmutable nativa de Java diseñada para transportar datos sin código repetitivo (genera constructor, accesores, equals, hashCode y toString). Puede contener lógica (validaciones en constructor, factory methods, métodos de cálculo), pero no puede mutar sus campos ni añadir estado extra.

**Aclaración vigente:** Un record es superficialmente inmutable: sus campos de componentes son finales, pero los objetos referenciados pueden cambiar. No admite campos de instancia adicionales; sí puede declarar miembros estáticos. Puede validar datos y proteger componentes mutables cuando el diseño lo requiera.

**Práctica pendiente:** Explicar qué puede cambiar en un record que contiene una lista y qué debe proteger su constructor o su API.

### Enum rico

**Texto registrado:** Enumerado que no solo lista constantes o etiquetas, sino que contiene lógica de negocio y comportamiento (métodos, validaciones de transición de estado, comprobaciones) relacionado con esos valores, evitando llenar el código externo de if/else repetitivos.

**Aclaración vigente:** Encapsular comportamiento en el enum no garantiza que las operaciones de la cuenta lo consulten. El agregado debe hacer cumplir sus invariantes.

**Práctica pendiente:** Explicar qué falta para que AccountStatus impida realmente operar sobre una cuenta bloqueada.

### Objeto auto-validante

**Texto registrado:** Objeto que comprueba sus propias reglas e invariantes en el momento de su construcción (ej. con Objects.requireNonNull o validaciones de negocio). Si los datos son erróneos falla de inmediato (Fail Fast), garantizando que cualquier instancia que exista en el sistema sea siempre 100% válida.

**Aclaración vigente:** La construcción puede garantizar un estado inicial válido respecto de las reglas comprobadas. Mantenerlo exige proteger también las operaciones posteriores y las referencias mutables expuestas. Una validación inicial no demuestra validez permanente ni cubre reglas externas al objeto.

**Práctica pendiente:** Mostrar cómo una cuenta creada correctamente podría quedar inválida tras una retirada si la operación no protege sus reglas.

### Inyección por constructor (Constructor Injection)

**Texto registrado:** Pasar las dependencias obligatoriamente a través del constructor de la clase en lugar de inyectarlas con `@Autowired` en atributos privados. Permite declarar los campos como `final` (inmutables), evita que el objeto se cree a medias o con dependencias nulas (Fail-Fast), y facilita los tests unitarios rápidos ya que podemos instanciar la clase pasando mocks directamente con `new` sin depender de Spring ni de trucos de reflexión.

**Aclaración vigente:** El constructor hace explícitas las dependencias, pero Java permite pasar null: hay que validarlas si se requiere impedirlo. Un campo final impide reasignar la referencia, no modificar el objeto referenciado. En pruebas pueden utilizarse implementaciones reales pequeñas, fakes o mocks según el objetivo.

**Práctica pendiente:** Explicar qué ocurre al construir un servicio con una dependencia nula y cómo probarlo sin arrancar Spring.

## Principios de diseño (SOLID)

### SRP (Principio de Responsabilidad Única)

**Texto registrado:** Una clase debe ocuparse de una sola cosa y tener una única razón para cambiar. Por ejemplo, una entidad tiene sus reglas de negocio pero no debe saber cómo guardarse en la base de datos ni gestionar transferencias ajenas a ella; así, si cambia la base de datos, no rompemos ni modificamos el modelo de negocio.

**Aclaración vigente:** Una sola responsabilidad no equivale a un solo método. Importa qué cambios del negocio o de sus responsables obligan a modificar el módulo. El lugar de una operación entre cuentas debe justificarse por sus invariantes y coordinación, no decidirse solo por esta frase.

**Práctica pendiente:** Identificar motivos de cambio distintos si una clase contiene reglas de saldo y detalles de persistencia.

### ISP (Principio de Segregación de Interfaces)

**Texto registrado:** Una clase no debe verse obligada a depender de métodos o interfaces que no utiliza. En lugar de crear interfaces gigantes con muchas operaciones, se crean interfaces pequeñas y específicas para cada necesidad (ej. un caso de uso = un método como CreateAccountUseCase).

**Aclaración vigente:** El principio se refiere a las dependencias que necesita cada cliente. No exige interfaces de un solo método. Una interfaz por caso de uso es una convención de NeoBank; la agrupación debe justificarse por las necesidades de sus clientes y la cohesión del contrato.

**Práctica pendiente:** Comparar una interfaz de dos operaciones usadas por el mismo cliente con otra que fuerza dependencias innecesarias y argumentar si hace falta separarlas.

## Persistencia e Infraestructura

### Flyway (Migraciones de base de datos)

**Texto registrado:** Herramienta que actúa como un "control de versiones" para la base de datos mediante scripts SQL versionados y ordenados (`V1__...`, `V2__...`). Evita el peligro de `ddl-auto: update` de Hibernate (que puede borrar columnas o corromper datos al intentar adivinar los cambios) y garantiza que el esquema de la base de datos sea exactamente el mismo y reproducible en desarrollo (H2) y en producción (PostgreSQL).

**Aclaración vigente:** Flyway registra y aplica migraciones de forma controlada, pero su contenido puede ser incorrecto o destructivo. No garantiza equivalencia entre motores ni corrección funcional. Hibernate `update` tampoco debe describirse como una herramienta que necesariamente borra columnas: el motivo para preferir migraciones aquí es disponer de cambios explícitos, revisables y versionados. La compatibilidad entre H2 y PostgreSQL requiere comprobación sobre ambos motores.

**Práctica pendiente:** Explicar qué demuestra un test que usa Hibernate `create-drop` y qué falta para comprobar una migración de producción.

## Referencias para contrastar las aclaraciones

- [Arquitectura hexagonal: artículo original de Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture).
- [Records: documentación de Java 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Record.html).
- [Métodos fábrica estáticos: extracto de Effective Java, Joshua Bloch](https://ptgmedia.pearsoncmg.com/imprint_downloads/informit/images/2009_JavaOne_PDF.pdf).
- [ISP: artículo de Robert C. Martin](https://objectmentor.com/resources/articles/isp.pdf).
