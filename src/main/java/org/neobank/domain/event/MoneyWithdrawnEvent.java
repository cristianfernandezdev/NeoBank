package org.neobank.domain.event;

import org.neobank.domain.model.AccountId;
import org.neobank.domain.model.Money;

import java.time.Instant;
import java.util.Objects;

public record MoneyWithdrawnEvent(Money money, AccountId accountId, Instant occurredOn)implements DomainEvent {
    public MoneyWithdrawnEvent{
        Objects.requireNonNull(money,"El valor money no puede ser nulo");
        Objects.requireNonNull(accountId,"La cuenta no puede ser nula");
        Objects.requireNonNull(occurredOn,"El instante no puede ser nulo");
    }
}
