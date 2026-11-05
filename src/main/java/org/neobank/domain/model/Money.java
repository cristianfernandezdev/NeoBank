package org.neobank.domain.model;

import org.neobank.domain.exception.CurrencyMismatchException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/** Importe y moneda inmutables, con igualdad por valor. */
public final class Money {
    // Politica de NeoBank: cuatro decimales y redondeo HALF_UP.
    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;
    private final BigDecimal amount;
    private final Currency currency;
    Money(BigDecimal amount, Currency currency) {
        Objects.requireNonNull(amount, "El importe no puede ser nulo");
        Objects.requireNonNull(currency, "La divisa no puede ser nula");
        this.amount = amount.setScale(SCALE, ROUNDING);
        this.currency = currency;
    }
    public static Money of(long amount, String currencyCode) {
        return new Money(
                BigDecimal.valueOf(amount),
                Currency.getInstance(currencyCode)
        );
    }
    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }
    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }
    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }
    // Puede dar un resultado negativo; Account debe proteger las reglas de saldo.
    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
    public boolean isGreaterThanOrEqual(Money other) {
        requireSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }
    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "El Money a comparar no puede ser nulo");
        if (!this.currency.equals(other.currency)) {
            throw new CurrencyMismatchException(this.currency, other.currency);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0
                && currency.equals(money.currency);
    }
    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }
    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency.getCurrencyCode();
    }
}
