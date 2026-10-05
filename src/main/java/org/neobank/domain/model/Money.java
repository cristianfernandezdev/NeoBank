package org.neobank.domain.model;

import java.util.Currency;
import java.math.BigDecimal;

public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("El importe y la divisa son obligatorios.");
        }
        this.amount = amount;
        this.currency = currency;
    }

    // --- GETTERS (Para poder leer el valor desde fuera) ---
    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    // --- COMPORTAMIENTO (Las matemáticas del banco) ---

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Violación de dominio: No se pueden sumar " + this.currency + " con " + other.currency);
        }
        // Retornamos una NUEVA instancia. NUNCA modificamos this.amount.
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Violación de dominio: No se pueden restar " + this.currency + " con " + other.currency);
        }
        return new Money(this.amount.subtract(other.amount), this.currency);
    }
}
