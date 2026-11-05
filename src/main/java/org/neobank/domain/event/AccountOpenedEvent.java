package org.neobank.domain.event;


import org.neobank.domain.model.AccountId;
import org.neobank.domain.model.UserId;

import java.time.Instant;
import java.util.Objects;

public record AccountOpenedEvent(AccountId accountId, UserId userId, Instant occurredOn) implements DomainEvent {
    public AccountOpenedEvent{
        Objects.requireNonNull(accountId,"El account id no puede estar vacio");
        Objects.requireNonNull(userId,"El user id no puede estar vacio ");
        Objects.requireNonNull(occurredOn,"Se necesita un instante de tiempo");
    }

}
