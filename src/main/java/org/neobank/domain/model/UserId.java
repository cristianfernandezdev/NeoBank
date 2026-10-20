package org.neobank.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que envuelve el UUID de un usuario.
 * Misma idea que AccountId: seguridad de tipos para no confundir identificadores.
 *
 * No tiene generate() porque el aggregate User aún no existe (Fase 5).
 * De momento solo recibimos el UserId desde fuera.
 */
public record UserId(UUID value) {

    public UserId {
        Objects.requireNonNull(value, "El ID de usuario no puede ser nulo");
    }

    // Factory method: crea un UserId a partir de un UUID existente.
    public static UserId of(UUID value) {
        return new UserId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
