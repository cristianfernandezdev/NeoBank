package org.neobank.domain.event;

import java.time.Instant;

public sealed interface DomainEvent
        permits AccountOpenedEvent,
                MoneyDepositedEvent,
                MoneyWithdrawnEvent,
                AccountBlockedEvent,
                AccountClosedEvent {

    Instant occurredOn();
}
