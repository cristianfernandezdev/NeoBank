package org.neobank.domain.exception;

import org.neobank.domain.model.AccountId;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Se lanza cuando se intenta retirar más dinero del que hay en la cuenta.
 * Incluye en el mensaje el saldo actual y la cantidad solicitada para facilitar el debug.
 * En la Fase 3, el GlobalExceptionHandler la mapeará a HTTP 422 Unprocessable Entity.
 */
public class InsufficientFundsException extends DomainException {

    public InsufficientFundsException(AccountId accountId, BigDecimal currentBalance,
                                      BigDecimal requestedAmount, Currency currency) {
        super(String.format(
                "Fondos insuficientes en cuenta %s: saldo = %s %s, solicitado = %s %s",
                accountId,
                currentBalance, currency.getCurrencyCode(),
                requestedAmount, currency.getCurrencyCode()));
    }
}
