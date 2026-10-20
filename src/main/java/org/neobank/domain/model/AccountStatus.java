package org.neobank.domain.model;

/**
 * Estados posibles de una cuenta bancaria.
 *
 * Usamos un enum "rico" (con métodos) en vez de un enum básico o un String.
 * ¿Por qué? Principio "Tell, Don't Ask": el enum SABE qué permite cada estado.
 * En vez de comprobar 'if (status == ACTIVE)' en 15 sitios distintos,
 * preguntamos 'status.allowsOperations()' — si mañana añadimos un estado SUSPENDED,
 * solo tocamos este enum.
 */
public enum AccountStatus {

    ACTIVE,
    BLOCKED,
    CLOSED;

    /**
     * ¿Puede esta cuenta hacer depósitos y retiradas?
     * Solo las cuentas ACTIVE pueden operar.
     */
    public boolean allowsOperations() {
        return this == ACTIVE;
    }

    /**
     * ¿Se puede reactivar esta cuenta (volver a ACTIVE)?
     * Solo las cuentas BLOCKED pueden desbloquearse.
     * Una cuenta CLOSED es irreversible — como cerrar una cuenta en un banco real.
     */
    public boolean canBeReactivated() {
        return this == BLOCKED;
    }
}
