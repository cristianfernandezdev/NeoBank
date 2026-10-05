package org.neobank.domain.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public class Account {

    private final UUID id;
    private final UUID userId;
    private Money balance; // Mantenemos el nombre 'balance', pero ahora es de tipo Money

    // 1. CONSTRUCTOR PARA CREAR CUENTAS NUEVAS
    // Ahora exigimos que el Servicio nos diga en qué moneda se abre la cuenta
    public Account(UUID userId, Currency currency) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.balance = new Money(BigDecimal.ZERO, currency);
    }

    // 2. CONSTRUCTOR PARA RECONSTRUIR CUENTAS (Desde la base de datos)
    // El archivero (Adaptador) tendrá que darnos el objeto Money ya construido
    public Account(UUID id, UUID userId, Money balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public Money getBalance() { return balance; }

    // 4. LÓGICA DE NEGOCIO PURA (Refactorizada a Money)
    public void deposit(Money amount) {
        // La lógica de si es menor a cero y si las divisas coinciden
        // ahora la hace la clase Money por dentro. Aquí solo sumamos.
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        // En una implementación completa, Money también tendría un método subtract()
        // y un método isLessThan() para comprobar si hay fondos suficientes.
        this.balance = this.balance.subtract(amount);
    }
}