package org.neobank.domain.exception;

import org.neobank.domain.model.AccountId;

/**
 * Se lanza cuando se intenta operar (depositar, retirar) sobre una cuenta
 * que no está en estado ACTIVE (puede estar BLOCKED o CLOSED).
 * En la Fase 3, el GlobalExceptionHandler la mapeará a HTTP 409 Conflict.
 */
public class AccountNotActiveException extends DomainException {

    public AccountNotActiveException(AccountId accountId) {
        super(String.format(
                "La cuenta %s no está activa — no se permiten operaciones", accountId));
    }
}
