package org.neobank.domain.event;


import org.neobank.domain.model.AccountId;
import org.neobank.domain.model.UserId;

import java.time.Instant;

public record AccountOpenedEvent(AccountId accountId, UserId userId, Instant occurredOn) implements DomainEvent {

}
