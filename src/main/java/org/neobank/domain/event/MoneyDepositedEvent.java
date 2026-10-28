package org.neobank.domain.event;

import org.neobank.domain.model.AccountId;
import org.neobank.domain.model.Money;

import java.time.Instant;


public record MoneyDepositedEvent(Money money, AccountId accountId,Instant  occurredOn) implements DomainEvent {

}
