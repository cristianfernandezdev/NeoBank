package org.neobank.domain.event;

import org.neobank.domain.model.AccountId;
import org.neobank.domain.model.Money;

import java.time.Instant;
import java.util.Objects;

public record MoneyDepositedEvent(Money money, AccountId accountId, Instant occurredOn) implements DomainEvent {

    public MoneyDepositedEvent {
        Objects.requireNonNull(money, "El importe no puede ser nulo");
        Objects.requireNonNull(accountId, "El ID de cuenta no puede ser nulo");
        Objects.requireNonNull(occurredOn, "El instante del ingreso no puede ser nulo");
    }
}
