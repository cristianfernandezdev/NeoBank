# NeoBank — Glosario de Christian

> Conceptos aprendidos, explicados con MIS palabras.
> Si no puedo explicarlo simple, no lo he entendido de verdad.

## Arquitectura

### Arquitectura Hexagonal
Dividir la app en capas (domain, application, infrastructure) conectadas por interfaces, para que no dependan unas de otras y puedas cambiar piezas sin romper el resto.

### Port
Interfaces que definen qué ofrece la app (port in = casos de uso) y qué necesita (port out = guardar datos, etc). Están en application/.

### Adapter
Las implementaciones concretas que conectan con el mundo real: controllers (adapter in) y persistencia (adapter out). Están en infrastructure/.

### Regla de dependencias
Domain no importa nada. Application solo importa domain. Infrastructure importa todo. Las flechas siempre apuntan hacia dentro.

## DDD Táctico

### Domain-Driven Design (DDD)
Pensar primero en qué hace el negocio y cómo es el dominio, modelarlo y meterle su lógica, en vez de crear primero las tablas de la base de datos y programar alrededor de ellas.

### Value Object
Objeto sin identidad (no tiene ID) e inmutable (si cambia el valor, se crea una instancia nueva). Se auto-valida al crearse y se define exclusivamente por sus datos internos (dos objetos con los mismos valores son idénticos).

### Domain Exception
Excepción propia que representa la violación de una regla del negocio (no un fallo técnico). Permite identificar claramente que el error viene del dominio y tratarlo de forma específica.

## Java y Patrones

### Inmutabilidad
Propiedad de un objeto cuyo estado no puede cambiar una vez creado (campos final, sin setters). Si se necesita un valor distinto se crea una nueva instancia, evitando efectos secundarios inesperados.

### Factory Method
Método estático que se encarga de instanciar y devolver un objeto en lugar de usar `new` directamente. Permite ocultar el constructor, validar parámetros y usar nombres descriptivos que explican cómo o para qué se crea la instancia (ej. `Money.zero()`).

### Record (Java 21)
Clase inmutable nativa de Java diseñada para transportar datos sin código repetitivo (genera constructor, accesores, equals, hashCode y toString). Puede contener lógica (validaciones en constructor, factory methods, métodos de cálculo), pero no puede mutar sus campos ni añadir estado extra.

### Enum rico
Enumerado que no solo lista constantes o etiquetas, sino que contiene lógica de negocio y comportamiento (métodos, validaciones de transición de estado, comprobaciones) relacionado con esos valores, evitando llenar el código externo de if/else repetitivos.

### Objeto auto-validante
Objeto que comprueba sus propias reglas e invariantes en el momento de su construcción (ej. con Objects.requireNonNull o validaciones de negocio). Si los datos son erróneos falla de inmediato (Fail Fast), garantizando que cualquier instancia que exista en el sistema sea siempre 100% válida.

## Principios de diseño (SOLID)

### ISP (Principio de Segregación de Interfaces)
Una clase no debe verse obligada a depender de métodos o interfaces que no utiliza. En lugar de crear interfaces gigantes con muchas operaciones, se crean interfaces pequeñas y específicas para cada necesidad (ej. un caso de uso = un método como CreateAccountUseCase).
