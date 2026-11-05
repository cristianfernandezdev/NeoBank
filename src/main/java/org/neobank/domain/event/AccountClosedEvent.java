package org.neobank.domain.event;

import org.neobank.domain.model.AccountId;

import java.time.Instant;
import java.util.Objects;

public record AccountClosedEvent(AccountId accountId, Instant occurredOn) implements DomainEvent {

    public AccountClosedEvent {
        Objects.requireNonNull(accountId, "El ID de cuenta no puede ser nulo");
        Objects.requireNonNull(occurredOn, "El instante del evento no puede ser nulo");
    }
}
