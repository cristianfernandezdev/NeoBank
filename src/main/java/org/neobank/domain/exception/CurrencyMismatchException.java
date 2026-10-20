package org.neobank.domain.exception;

import java.util.Currency;

/**
 * Se lanza cuando se intenta operar con dos monedas diferentes.
 * Ejemplo: sumar EUR + USD, o depositar dólares en una cuenta de euros.
 * En la Fase 3, el GlobalExceptionHandler la mapeará a HTTP 400 Bad Request.
 */
public class CurrencyMismatchException extends DomainException {

    public CurrencyMismatchException(Currency expected, Currency actual) {
        super(String.format(
                "Divisa incompatible: se esperaba %s pero se recibió %s",
                expected.getCurrencyCode(), actual.getCurrencyCode()));
    }
}
