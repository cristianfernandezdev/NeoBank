# NeoBank — Log de errores

> Errores confirmados, su corrección y cómo detectarlos de nuevo. Última revisión autorizada: 26 Oct 2026.

## Criterio de registro

- Registrar hechos confirmados y distinguir error del código, error conceptual, error documental o error de actuación de la IA. No atribuir autoría cuando no se conoce.
- Para cada entrada: fecha, estado, evidencia, causa conocida o hipótesis, corrección y prevención/comprobación.
- Separar una corrección documental de una corrección de implementación o un aprendizaje demostrado. Una recomendación no aplicada sigue pendiente.
- No registrar secretos, cada errata ni preferencias de estilo como fallos funcionales. Priorizar errores repetibles o con impacto.
- Si no hay autorización para escribir, presentar el registro propuesto en el cierre del chat.

## ERR-001 | 26 Oct 2026 | Edición de memoria durante una petición de análisis

- **Tipo**: Actuación de la IA.
- **Estado**: Edición indebida revertida; protocolo documental corregido. Su cumplimiento deberá mantenerse en próximas sesiones.
- **Evidencia**: En esta conversación, la IA cambió la fecha y añadió un bloque de revisión a `estado.md` tras una petición de analizar el proyecto. Christian señaló que no había pedido editar. La IA revirtió sus cambios y comprobó que el archivo no tenía diferencias; conservó la modificación previa en `CreateAccountUseCase.java`.
- **Causa**: La IA aplicó la instrucción antigua de actualizar la memoria al cierre por encima del alcance de la petición. El error fue de la IA; la regla local no justificaba ampliar el encargo.
- **Corrección**: Reversión realizada en la conversación. ADR-005 y `reglas.md` separan lectura, revisión y edición, y eliminan la escritura automática al cierre.
- **Prevención/comprobación**: Identificar qué escrituras están autorizadas antes de actuar. En una revisión de solo lectura, no editar `.ai` ni ejecutar comprobaciones que generen archivos. Antes de cerrar una edición, comprobar que las diferencias corresponden al alcance y preservar las previas.

## ERR-002 | 26 Oct 2026 | Definiciones demasiado amplias tratadas como aprendizaje definitivo

- **Tipo**: Conceptual y documental; autoría inicial de las imprecisiones no determinada.
- **Estado**: Aclaraciones documentales aplicadas; reformulación y práctica de Christian pendientes.
- **Evidencia**: El glosario describía los records como inmutables sin matizar referencias mutables, la inyección por constructor como protección automática frente a null y Flyway como garantía de equivalencia entre H2 y PostgreSQL. También titulaba Factory Method a una explicación de fábrica estática. La regla anterior consideraba aprendido todo concepto ya registrado.
- **Causa observada**: Faltaban límites en las definiciones y criterios de evidencia más allá de la explicación inmediata. No se puede reconstruir con estos archivos cómo se validó originalmente cada entrada.
- **Corrección**: `glosario.md` conserva las explicaciones históricas y añade aclaraciones vigentes identificadas como texto de la IA, prácticas propuestas y criterios de evidencia. `reglas.md` permite revisar un concepto ya registrado.
- **Prevención/comprobación**: Contrastar afirmaciones dudosas con fuentes primarias y comprobarlas mediante contraejemplos. Registrar una aplicación y una recuperación posterior cuando sucedan; no dar esas actividades por hechas al escribir el documento.

## ERR-003 | 26 Oct 2026 | Confusión entre perfil de producción objetivo y configuración existente

- **Tipo**: Documental; ajuste técnico pendiente de decidir y aplicar.
- **Estado**: Documentación corregida; preparación de migraciones de producción pendiente.
- **Evidencia**: `contexto.md` indicaba Flyway activado en prod, mientras `src/main/resources/application-prod.yml` contiene `spring.flyway.enabled: false` y Hibernate `ddl-auto: validate`. Confirmado por lectura, no mediante despliegue.
- **Causa observada**: La memoria mezclaba el objetivo de arquitectura con la configuración actual; no se conoce por estos archivos por qué se desactivó Flyway en prod.
- **Corrección**: `contexto.md`, `estado.md` y ADR-001 distinguen configuración observada, objetivo y verificación pendiente. No se ha modificado la configuración de la aplicación.
- **Prevención/comprobación**: Antes de considerar preparado el despliegue, decidir dónde ejecutar las migraciones y verificar el esquema con PostgreSQL. No inferir esa validación del test de arranque en H2 con `create-drop`.
