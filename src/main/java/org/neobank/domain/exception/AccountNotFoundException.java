package org.neobank.domain.exception;

import org.neobank.domain.model.AccountId;

/**
 * Se lanza cuando se busca una cuenta por su ID y no existe en el sistema.
 * En la Fase 3, el GlobalExceptionHandler la mapeará a HTTP 404 Not Found.
 */
public class AccountNotFoundException extends DomainException {

    public AccountNotFoundException(AccountId accountId) {
        super(String.format("No se encontró la cuenta con ID %s", accountId));
    }
}
