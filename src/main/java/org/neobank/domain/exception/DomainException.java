package org.neobank.domain.exception;

/**
 * Clase base abstracta para TODAS las excepciones del dominio bancario.
 *
 * ¿Por qué abstracta? Porque no tiene sentido lanzar un "error de dominio" genérico.
 * Siempre quieres saber el error concreto: fondos insuficientes, cuenta no encontrada, etc.
 *
 * ¿Por qué extends RuntimeException (unchecked)?
 * Porque así las capas intermedias (application/) no están obligadas a hacer try/catch.
 * Solo la capa de infraestructura (GlobalExceptionHandler) captura y traduce a HTTP.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
