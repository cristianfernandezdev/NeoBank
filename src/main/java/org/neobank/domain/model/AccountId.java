package org.neobank.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que envuelve el UUID de una cuenta.
 *
 * ¿Por qué no usar UUID directamente? Seguridad de tipos.
 * Si un método pide AccountId, no puedes pasarle un UserId por error.
 * El compilador te protege — en banca, confundir IDs es un bug grave.
 *
 * Usamos record (Java 21) porque nos da equals(), hashCode() y toString()
 * gratis, y es inmutable por diseño.
 */
public record AccountId(UUID value) {

    // Constructor compacto del record: se ejecuta antes de asignar los campos.
    // Validamos que nunca sea null — un ID sin valor no tiene sentido.
    public AccountId {
        Objects.requireNonNull(value, "El ID de cuenta no puede ser nulo");
    }

    // Factory method: genera un AccountId nuevo con UUID aleatorio.
    // Se usa al CREAR una cuenta nueva.
    public static AccountId generate() {
        return new AccountId(UUID.randomUUID());
    }

    // Factory method: crea un AccountId a partir de un UUID que ya existe.
    // Se usa al RECONSTRUIR una cuenta desde la base de datos.
    public static AccountId of(UUID value) {
        return new AccountId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
