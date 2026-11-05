package org.neobank.application.port.in;

import org.neobank.domain.model.UserId;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

// Esta interfaz es el contrato. Le dice al mundo web:
// "Si quieres crear una cuenta, pásame el UUID de un usuario y yo te devolveré el UUID de la nueva cuenta".
public interface CreateAccountUseCase {
    UUID create(UserId userId, Currency currency);
}