package org.neobank.domain.model;

import org.neobank.domain.event.AccountOpenedEvent;
import org.neobank.domain.event.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;


public class Account {

    private  final AccountId accountId;
    private final UserId userId;
    private Money balance;
    private  AccountStatus accountStatus;
    private final Instant createdAt;
    private final List<DomainEvent> events;
    // Mantenemos el nombre 'balance', pero ahora es de tipo Money

    // 1. CONSTRUCTOR PARA CREAR CUENTAS NUEVASco
    // Ahora exigimos que el Servicio nos diga en qué moneda se abre la cuenta

    // 2. CONSTRUCTOR PARA RECONSTRUIR CUENTAS (Desde la base de datos)
    // El archivero (Adaptador) tendrá que darnos el objeto Money ya construido
    private Account(AccountId accountId, UserId userId, Money balance,  AccountStatus accountStatus, Instant createdAt) {
        this.accountId = Objects.requireNonNull(accountId, "El ID de cuenta no puede ser nulo");
        this.userId = Objects.requireNonNull(userId, "El ID de usuario no puede ser nulo");
        this.balance = Objects.requireNonNull(balance, "El saldo no puede ser nulo");
        this.accountStatus = Objects.requireNonNull(accountStatus, "El estado de la cuenta no puede ser nulo");
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creación no puede ser nula");
        events = new ArrayList<>();
    }

    public AccountId getId() { return accountId; }
    public UserId getUserId() { return userId; }
    public Money getBalance() { return balance; }
    public AccountStatus getStatus() { return accountStatus; }
    public Instant getCreatedAt() { return createdAt; }

    // 4. LÓGICA DE NEGgitOCIO PURA (Refactorizada a Money)
    public void deposit(Money amount) {
        this.balance = this.balance.add(amount);
    }
    public static Account open(UserId userId,Currency currency) {
        Account account = new Account(AccountId.generate(), userId, Money.zero(currency), AccountStatus.ACTIVE, Instant.now());
        account.addEvent(new AccountOpenedEvent(account.accountId,account.userId,account.createdAt));
        return account;
    }
    public static Account reconstitute(AccountId accountId, UserId userId, Money balance, AccountStatus accountStatus, Instant createdAt) {
        return new Account(accountId, userId, balance, accountStatus, createdAt);
    }
    private void addEvent(DomainEvent event) {
        this.events.add(event);
    }

    public List<DomainEvent> getEvents() {
        List<DomainEvent> events = List.copyOf(this.events);
        this.events.clear();
        return events;
    }

    public void withdraw(Money amount) {
        // En una implementación completa, Money también tendría un método subtract()
        // y un método isLessThan() para comprobar si hay fondos suficientes.
        this.balance = this.balance.subtract(amount);
    }
}
