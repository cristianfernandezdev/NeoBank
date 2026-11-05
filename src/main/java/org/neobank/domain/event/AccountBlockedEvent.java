package org.neobank.domain.event;

import org.neobank.domain.model.AccountId;

import java.time.Instant;
import java.util.Objects;

public record AccountBlockedEvent(AccountId accountId, Instant occurredOn) implements DomainEvent {

    public AccountBlockedEvent {
        Objects.requireNonNull(accountId, "El ID de cuenta no puede ser nulo");
        Objects.requireNonNull(occurredOn, "El instante del evento no puede ser nulo");
    }
}
