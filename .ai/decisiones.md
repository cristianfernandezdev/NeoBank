# NeoBank — Decisiones arquitectónicas

> Formato: ADR-{nº} | fecha | decisión | contexto | alternativas descartadas

## ADR-001 | Oct 2026 | H2 en desarrollo, PostgreSQL solo en AWS
- **Contexto**: No necesitar Docker/PostgreSQL instalado localmente durante el desarrollo
- **Decisión**: H2 en memoria para dev/test. PostgreSQL solo en producción (AWS RDS)
- **Alternativas descartadas**: PostgreSQL local con Docker (complejidad innecesaria en esta fase)
- **Consecuencia**: Flyway activo desde el inicio. Migraciones SQL compatibles con ambos

## ADR-002 | Oct 2026 | Money como Value Object en vez de BigDecimal directo
- **Contexto**: Representar dinero de forma segura. BigDecimal solo no captura la moneda
- **Decisión**: Value Object `Money` (amount + currency), final, inmutable, escala 4
- **Alternativas descartadas**: BigDecimal + String currency por separado (pierde cohesión), Joda-Money (dependencia externa en domain/)

## ADR-003 | Oct 2026 | IDs tipados (AccountId, UserId) en vez de UUID directo
- **Contexto**: Evitar confundir IDs de distintas entidades. Un UUID es un UUID
- **Decisión**: Records que envuelven UUID con validación
- **Alternativas descartadas**: UUID directo (sin seguridad de tipos), Long autoincremental (predecible en banca)

## ADR-004 | Oct 2026 | Modelo de colaboración IA: Arquitecto/Revisor + Memory Bank
- **Contexto**: Maximizar aprendizaje real. Evitar que la IA genere código que Christian no entiende
- **Decisión**: La IA solo da contratos y destroza código. Christian escribe TODO
- **Alternativas descartadas**: IA genera código + Christian lo estudia (aprendizaje pasivo)
