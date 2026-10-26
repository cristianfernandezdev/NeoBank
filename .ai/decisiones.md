# NeoBank — Decisiones arquitectónicas

> Registro de decisiones aceptadas y su historia. Revisión documental autorizada: 26 Oct 2026.

## Cómo mantener este registro

- Cada ADR identifica fecha, estado, contexto, decisión, alternativas, consecuencias y condición de revisión.
- Estados: propuesto, aceptado o sustituido. Una recomendación de la IA no se registra como aceptada sin acuerdo.
- Conservar la decisión original y fechar las aclaraciones. Para sustituirla, crear otro ADR y enlazarlos; no reescribir la historia como si la nueva decisión siempre hubiera existido.
- Un ADR aceptado no demuestra implementación, pruebas ni comprensión: las evidencias van en `estado.md` y `glosario.md`.
- Vigentes: ADR-001, ADR-002, ADR-003, ADR-005 y ADR-006. ADR-004 se conserva como antecedente sustituido.

## ADR-001 | Oct 2026 | H2 en desarrollo, PostgreSQL solo en AWS

- **Estado**: Aceptado. Se conserva la elección de motores.
- **Contexto**: No necesitar Docker/PostgreSQL instalado localmente durante el desarrollo
- **Decisión**: H2 en memoria para dev/test. PostgreSQL solo en producción (AWS RDS)
- **Alternativas descartadas**: PostgreSQL local con Docker (complejidad innecesaria en esta fase)
- **Consecuencia**: Flyway activo desde el inicio. Migraciones SQL compatibles con ambos

**Aclaración del 26 Oct 2026:** la consecuencia anterior expresaba una intención, no una compatibilidad demostrada. Flyway está activado en dev, desactivado en test y actualmente desactivado en prod. Ver la configuración observada en `contexto.md`.

- **Beneficio**: Menor preparación del entorno local durante las primeras fases.
- **Coste aceptado**: H2 no demuestra el comportamiento de PostgreSQL; diferencias de SQL, tipos, restricciones y concurrencia pueden aparecer más tarde.
- **Condición de revisión**: Antes de considerar preparado el despliegue, ejecutar las migraciones y pruebas de persistencia relevantes contra PostgreSQL y resolver cómo se aplicarán las migraciones de producción. Si aparece una incompatibilidad antes, revisar la decisión en ese momento. El mecanismo concreto de esa comprobación queda por acordar.

## ADR-002 | Oct 2026 | Money como Value Object en vez de BigDecimal directo

- **Estado**: Aceptado.
- **Contexto**: Representar dinero de forma segura. BigDecimal solo no captura la moneda
- **Decisión**: Value Object `Money` (amount + currency), final, inmutable, escala 4
- **Alternativas descartadas**: BigDecimal + String currency por separado (pierde cohesión), Joda-Money (dependencia externa en domain/)

**Consecuencias y revisión añadidas el 26 Oct 2026:**

- **Beneficio**: Cantidad y moneda se mantienen juntas, con igualdad por valor y operaciones controladas.
- **Coste aceptado**: Hay que definir, mantener y probar la política de precisión, redondeo y validación. El tipo Money no sustituye las invariantes propias de Account.
- **Límite**: Escala 4 es una decisión de NeoBank, no una garantía universal de precisión para cualquier operación financiera.
- **Condición de revisión**: Al incorporar intereses, conversiones u operaciones con nuevas exigencias de precisión. Acordar ejemplos esperados antes de cambiar escala o redondeo.

## ADR-003 | Oct 2026 | IDs tipados (AccountId, UserId) en vez de UUID directo

- **Estado**: Aceptado; integración en Account pendiente según `estado.md`.
- **Contexto**: Evitar confundir IDs de distintas entidades. Un UUID es un UUID
- **Decisión**: Records que envuelven UUID con validación
- **Alternativas descartadas**: UUID directo (sin seguridad de tipos), Long autoincremental (predecible en banca)

**Consecuencias y revisión añadidas el 26 Oct 2026:**

- **Beneficio**: El compilador distingue IDs de cuenta y usuario en los contratos que usan esos tipos.
- **Coste aceptado**: Tipos y conversiones adicionales en las fronteras REST y de persistencia.
- **Límite**: La seguridad de tipos es distinta de la autorización. Un UUID no demuestra titularidad ni sustituye controles de acceso; la impredecibilidad del ID no basta para proteger una cuenta.
- **Condición de revisión**: Al integrar los IDs en dominio, casos de uso y adaptadores, comprobar que no se pierda la distinción de tipos dentro de la aplicación. Revisar el formato solo ante un requisito concreto de integración o identidad.

## ADR-004 | Oct 2026 | Modelo de colaboración IA: Arquitecto/Revisor + Memory Bank

- **Estado**: Sustituido por ADR-005 el 26 Oct 2026. El texto siguiente es histórico y no debe aplicarse como protocolo vigente.
- **Contexto**: Maximizar aprendizaje real. Evitar que la IA genere código que Christian no entiende
- **Decisión**: La IA solo da contratos y destroza código. Christian escribe TODO
- **Alternativas descartadas**: IA genera código + Christian lo estudia (aprendizaje pasivo)

**Motivo de sustitución:** entregar siempre los contratos ya diseñados limita la práctica de arquitectura de Christian. La prohibición indiscriminada de ejemplos, la validación definitiva del glosario y la actualización automática de memoria tampoco distinguen nivel de ayuda, evidencia de aprendizaje y autorización para editar. Leer un ejemplo no equivale necesariamente a aprendizaje pasivo: importa la actividad que se realiza con él.

## ADR-005 | 26 Oct 2026 | Tutoría con autonomía gradual, revisión con evidencias y memoria autorizada

- **Estado**: Aceptado por Christian en esta conversación; sustituye ADR-004. La aplicación del método en una sesión de aprendizaje está pendiente.
- **Contexto**: Aprender a diseñar y defender NeoBank, además de escribir su implementación; conservar continuidad sin convertir notas o recomendaciones en hechos demostrados.
- **Decisión**: Christian propone progresivamente escenarios, responsabilidades, invariantes y contratos. La IA enseña lo que falta, da pistas y revisa con contraejemplos y evidencias. Christian implementa por defecto. El protocolo operativo está en `reglas.md`.
- **Ayuda**: Ejemplo análogo para conceptos nuevos, diseño acompañado en primeras aplicaciones y propuesta autónoma cuando haya práctica suficiente. Se permite volver a un nivel de ayuda mayor si hace falta.
- **Validación**: Diferenciar explicación registrada, aplicación con ayuda, aplicación sin ayuda y recuperación posterior. Las correcciones escritas por la IA no acreditan comprensión de Christian.
- **Alcance**: Leer el contexto necesario para la tarea; editar solo dentro de lo autorizado. El cierre de sesión se entrega en el chat y no autoriza guardar cambios automáticamente.
- **Alternativas descartadas**: Mantener a la IA como diseñadora permanente; generar la solución de la tarea antes del intento de Christian; prohibir todos los ejemplos didácticos; añadir una infraestructura de memoria más compleja sin una necesidad demostrada.
- **Beneficio esperado**: Practicar criterio arquitectónico y disponer de revisiones comprobables, ajustando la ayuda al nivel real.
- **Coste aceptado**: Algunas tareas requerirán más tiempo de explicación, intentos y comprobación. Hay que evitar tanto entregar respuestas prematuramente como prolongar bloqueos sin enseñar.
- **Condición de revisión**: Tras practicar un incremento y una variante posterior, revisar si Christian puede justificar más decisiones con menos ayuda. Si hay bloqueo persistente, falta de avance o exceso de carga documental, ajustar el protocolo; no dar por demostrada su eficacia por haberlo escrito.

**Fundamento y límites:** el aprendizaje con modelado, práctica guiada y retirada de apoyo está descrito por [Collins, Brown y Holum](https://www.aft.org/ae/winter1991/collins_brown_holum). La recuperación de memoria tiene apoyo experimental en [Roediger y Karpicke](https://pubmed.ncbi.nlm.nih.gov/16507066/). Estas fuentes orientan el método, pero no prueban que este protocolo concreto sea óptimo para NeoBank; su resultado se evaluará con la práctica de Christian.

**Aclaración posterior del 26 Oct 2026 — bloques guiados:** Christian pide una experiencia sencilla, con guía suficiente para trabajar sin consultar a la IA cada paso. La IA prepara un bloque completo con objetivo, explicación, archivos relevantes, pasos, ejemplos y criterio de finalización. Christian implementa a su ritmo y vuelve para revisar o pedir una pista si se bloquea. La autonomía aumenta dentro de esa guía, sin exigir descubrir conceptos desconocidos ni organizar el temario. Esta aclaración concreta ADR-005; no elimina la práctica propia ni autoriza escrituras automáticas.

## ADR-006 | 26 Oct 2026 | Entrada común en AGENTS.md y planificación consolidada

- **Estado**: Aceptado en el encargo de Christian de crear una entrada para cualquier IA y simplificar la documentación.
- **Contexto**: `GEMINI.md` dirigía a la memoria, mientras `ROADMAP.md` duplicaba reglas y estado y conservaba un modelo de colaboración antiguo. Parte del perfil y de las fases futuras solo estaba en ese roadmap.
- **Decisión**: Crear `AGENTS.md` en la raíz con instrucciones de entrada y un prompt reutilizable. Mantener el detalle en los seis archivos de `.ai`. Integrar en `contexto.md` el perfil, la dedicación, el alcance y un plan compacto con entregables antes de retirar `GEMINI.md` y `ROADMAP.md`.
- **Alternativas descartadas**: Mantener instrucciones distintas por proveedor; conservar el roadmap completo duplicando reglas y estado; borrar la planificación sin trasladar la información útil; añadir otro archivo de planificación cuando cabe de forma compacta en el contexto.
- **Beneficio**: Un punto de inicio común y una ubicación definida para cada tipo de información. Christian puede iniciar una sesión con una petición breve.
- **Coste y límite**: No todos los asistentes leen automáticamente un archivo del repositorio. El prompt debe indicar que abran `AGENTS.md`; si no tienen acceso, necesitan recibir su contenido y los documentos relevantes. La síntesis conserva objetivos y entregables, no cada frase del documento anterior; su versión original permanece en el historial de Git.
- **Condición de revisión**: Si la planificación crece hasta dificultar la lectura del contexto, separar únicamente ese plan sin duplicar las reglas o el progreso. No añadir documentación por proveedor sin una necesidad concreta.
