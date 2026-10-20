-- ============================================================
-- Migración V1: Creación de la tabla accounts
-- Autor: Christian Fernández
-- Fecha: Octubre 2026
-- ============================================================
-- NOTA: Este SQL es compatible con PostgreSQL (que usaremos en AWS).
--       H2 entiende esta sintaxis también → funciona en local y en producción.
-- ============================================================

CREATE TABLE accounts (

    -- Identificador único de la cuenta.
    -- UUID en lugar de INTEGER autoincremental → más seguro en banca
    -- (no se puede adivinar el ID de otra cuenta sumando 1)
    id          UUID            NOT NULL,

    -- Quién es el dueño de esta cuenta
    user_id     UUID            NOT NULL,

    -- Saldo de la cuenta.
    -- DECIMAL(19, 4): hasta 19 dígitos en total, 4 decimales.
    -- Precisión bancaria estándar. NUNCA usar FLOAT para dinero.
    balance     DECIMAL(19, 4)  NOT NULL DEFAULT 0.0000,

    -- Moneda de la cuenta (código ISO 4217: EUR, USD, GBP...)
    currency    VARCHAR(3)      NOT NULL,

    -- Estado de la cuenta
    -- CHECK limita los valores posibles directamente en la BD → doble seguridad
    status      VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE'
                    CONSTRAINT chk_account_status
                    CHECK (status IN ('ACTIVE', 'BLOCKED', 'CLOSED')),

    -- Fecha y hora de creación (con zona horaria → estándar en banca)
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,

    -- Clave primaria
    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

-- Índice sobre user_id: cuando busques "todas las cuentas de un usuario"
-- será muy rápido. Sin índice, haría un scan completo de la tabla.
CREATE INDEX idx_accounts_user_id ON accounts (user_id);
