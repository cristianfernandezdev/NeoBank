package org.neobank.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;



public class Account {

    private  final AccountId accountId;
    private final UserId userId;
    private Money balance;
    private  AccountStatus accountStatus;
    private final Instant createdAt;// Mantenemos el nombre 'balance', pero ahora es de tipo Money

    // 1. CONSTRUCTOR PARA CREAR CUENTAS NUEVAS
    // Ahora exigimos que el Servicio nos diga en qué moneda se abre la cuenta

    // 2. CONSTRUCTOR PARA RECONSTRUIR CUENTAS (Desde la base de datos)
    // El archivero (Adaptador) tendrá que darnos el objeto Money ya construido
    private Account(AccountId accountId, UserId userId, Money balance,  AccountStatus accountStatus, Instant createdAt) {
        this.accountId = Objects.requireNonNull(accountId, "El ID de cuenta no puede ser nulo");
        this.userId = Objects.requireNonNull(userId, "El ID de usuario no puede ser nulo");
        this.balance = Objects.requireNonNull(balance, "El saldo no puede ser nulo");
        this.accountStatus = Objects.requireNonNull(accountStatus, "El estado de la cuenta no puede ser nulo");
        this.createdAt = Objects.requireNonNull(createdAt, "La fecha de creación no puede ser nula");
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
        return new Account(AccountId.generate(), userId, Money.zero(currency), AccountStatus.ACTIVE, Instant.now());
    }
    public static Account reconstitute(AccountId accountId, UserId userId, Money balance, AccountStatus accountStatus, Instant createdAt) {
        return new Account(accountId, userId, balance, accountStatus, createdAt);
    }

    public void withdraw(Money amount) {
        // En una implementación completa, Money también tendría un método subtract()
        // y un método isLessThan() para comprobar si hay fondos suficientes.
        this.balance = this.balance.subtract(amount);
    }
}
