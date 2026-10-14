package org.neobank.domain.model;

import org.neobank.domain.exception.CurrencyMismatchException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Value Object que representa dinero: una cantidad + una moneda.
 *
 * ¿Qué es un Value Object?
 * Es un objeto que se define por su VALOR, no por un identificador.
 * Dos Money con la misma cantidad y moneda son IGUALES, igual que
 * dos billetes de 100€ "valen lo mismo" aunque sean billetes físicos distintos.
 *
 * Tres reglas de un Value Object:
 * 1. INMUTABLE — una vez creado, no cambia. add() devuelve uno NUEVO.
 * 2. IGUALDAD POR VALOR — equals() compara amount + currency, no la referencia en memoria.
 * 3. AUTO-VALIDANTE — si el constructor no lanza excepción, el objeto es válido.
 */
public final class Money {
    // ──────────────────────────────────────────────────────────────────
    // ¿Por qué "final class"?
    //
    // "final" significa que NADIE puede hacer "extends Money" para crear
    // una subclase. ¿Por qué nos importa? Porque Money define su igualdad
    // con equals() basándose en amount + currency. Si alguien creara
    // "class SuperMoney extends Money" con campos extra, el equals()
    // se rompería: un Money y un SuperMoney con la misma cantidad
    // podrían dar resultados inconsistentes.
    //
    // Regla general: los Value Objects siempre son final.
    // ──────────────────────────────────────────────────────────────────

    // ──────────────────────────────────────────────────────────────────
    // CONSTANTE: Escala fija de 4 decimales.
    //
    // ¿Por qué 4? Es el estándar bancario internacional.
    // - EUR y USD usan 2 decimales normalmente, pero en cálculos
    //   intermedios (intereses, conversiones) necesitas más precisión.
    // - Algunas monedas como el dinar kuwaití usan 3 decimales.
    // - 4 decimales cubre todos los casos con margen.
    //
    // Al tenerlo como constante, si algún día necesitáramos cambiar
    // la escala, solo tocamos AQUÍ, no en 20 sitios diferentes.
    // ──────────────────────────────────────────────────────────────────
    private static final int SCALE = 4;

    // ──────────────────────────────────────────────────────────────────
    // CONSTANTE: Modo de redondeo HALF_UP.
    //
    // Es el redondeo "del colegio": si el siguiente dígito es >= 5, sube.
    //   10.12345 → 10.1235 (el 5 sube el 4 a 5)
    //   10.12341 → 10.1234 (el 1 no sube nada)
    //
    // Otros modos existen (HALF_DOWN, HALF_EVEN, FLOOR, CEILING),
    // pero HALF_UP es el más intuitivo y usado en banca minorista.
    // ──────────────────────────────────────────────────────────────────
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    // ──────────────────────────────────────────────────────────────────
    // CAMPOS: ambos "final" → una vez asignados en el constructor,
    // NO se pueden cambiar. Esto es lo que hace a Money INMUTABLE.
    //
    // "private" → nadie accede directamente, solo a través de getters.
    // ──────────────────────────────────────────────────────────────────
    private final BigDecimal amount;
    private final Currency currency;

    // ══════════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ══════════════════════════════════════════════════════════════════

    // ──────────────────────────────────────────────────────────────────
    // Constructor a nivel de paquete (package-private) con validación completa.
    //
    // ¿Por qué package-private en vez de public?
    // Para que el código externo fuera de domain/ esté obligado a usar los factory
    // methods Money.of() y Money.zero(), mientras que clases dentro del mismo
    // paquete (como el Aggregate Account) pueden construirlo directamente.
    //
    // ¿Qué es "auto-validante"? Significa que si este constructor
    // termina sin lanzar excepción, el Money creado es 100% válido.
    // Es como un control de pasaporte: si lo pasas, estás dentro.
    // En el resto del código NUNCA necesitas comprobar "¿este Money
    // es válido?" — si existe, lo es.
    // ──────────────────────────────────────────────────────────────────
    Money(BigDecimal amount, Currency currency) {
        // Validación: ni la cantidad ni la moneda pueden ser null.
        // Objects.requireNonNull es la forma estándar en Java de hacer
        // esta comprobación — lanza NullPointerException con mensaje claro.
        Objects.requireNonNull(amount, "El importe no puede ser nulo");
        Objects.requireNonNull(currency, "La divisa no puede ser nula");

        // ──────────────────────────────────────────────────────────────
        // setScale(4, HALF_UP): NORMALIZACIÓN de la escala.
        //
        // ¿Qué problema resuelve? BigDecimal distingue entre:
        //   new BigDecimal("10.1")  → internamente escala 1
        //   new BigDecimal("10.10") → internamente escala 2
        //   BigDecimal.TEN          → internamente escala 0
        //
        // Sin normalizar, 10.1€ y 10.10€ serían "distintos" para equals().
        // Con setScale(4), TODOS se convierten a 10.1000 → siempre iguales.
        //
        // También redondea si hay más de 4 decimales:
        //   10.12345 → 10.1235 (HALF_UP: el 5 sube)
        // ──────────────────────────────────────────────────────────────
        this.amount = amount.setScale(SCALE, ROUNDING);
        this.currency = currency;
    }

    // ══════════════════════════════════════════════════════════════════
    //  FACTORY METHODS — Formas cómodas de crear Money
    // ══════════════════════════════════════════════════════════════════

    // ──────────────────────────────────────────────────────────────────
    // Money.of(100, "EUR") — Crea Money a partir de un número y un código de moneda.
    //
    // ¿Por qué un factory method en vez de hacer new Money() directamente?
    //
    // 1. LEGIBILIDAD:
    //    Money.of(100, "EUR")
    //    vs
    //    new Money(new BigDecimal("100"), Currency.getInstance("EUR"))
    //
    // 2. CONTROL: el constructor externo está restringido. Si mañana necesitamos
    //    cachear objetos Money frecuentes (como Money.zero), podemos
    //    hacerlo aquí sin cambiar el código que llama.
    //
    // 3. NOMBRES DESCRIPTIVOS: un constructor siempre se llama Money().
    //    Un factory method puede llamarse of(), zero(), fromCents()...
    //    cada nombre explica QUÉ estás creando.
    //
    // BigDecimal.valueOf(amount) convierte el double/long a BigDecimal.
    // ──────────────────────────────────────────────────────────────────
    public static Money of(long amount, String currencyCode) {
        return new Money(
                BigDecimal.valueOf(amount),
                Currency.getInstance(currencyCode)
        );
    }

    // ──────────────────────────────────────────────────────────────────
    // Versión que acepta BigDecimal directamente.
    // Útil al reconstruir desde la base de datos, donde el valor
    // ya viene como BigDecimal.
    // ──────────────────────────────────────────────────────────────────
    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    // ──────────────────────────────────────────────────────────────────
    // Money.zero("EUR") — Crea un Money con importe 0.
    // Se usa al ABRIR una cuenta nueva: empieza con saldo cero.
    // ──────────────────────────────────────────────────────────────────
    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    // ══════════════════════════════════════════════════════════════════
    //  GETTERS — Para leer los valores desde fuera
    // ══════════════════════════════════════════════════════════════════

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    // ══════════════════════════════════════════════════════════════════
    //  OPERACIONES ARITMÉTICAS — Las "matemáticas del banco"
    // ══════════════════════════════════════════════════════════════════

    // ──────────────────────────────────────────────────────────────────
    // add(Money other): Suma dos Money.
    //
    // IMPORTANTE: no modifica this.amount. Devuelve un Money NUEVO.
    // Esto es la INMUTABILIDAD: el objeto original nunca cambia.
    //
    //   Money cien = Money.of(100, "EUR");
    //   Money cincuenta = Money.of(50, "EUR");
    //   Money resultado = cien.add(cincuenta);  // → 150 EUR (nuevo objeto)
    //   // cien sigue siendo 100 EUR — no ha cambiado
    //
    // ¿Por qué inmutable? Porque si dos partes del código tienen
    // referencia al mismo Money, una no puede "romperle" el valor
    // a la otra sin querer.
    //
    // Antes de sumar, comprueba que las monedas sean iguales.
    // No tiene sentido sumar EUR + USD sin conversión.
    // ──────────────────────────────────────────────────────────────────
    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    // ──────────────────────────────────────────────────────────────────
    // subtract(Money other): Resta dos Money.
    // Misma lógica que add: inmutable, valida moneda, devuelve nuevo.
    //
    // NOTA: subtract NO comprueba si el resultado es negativo.
    // Esa validación la hará el Aggregate (Account), que es quien sabe
    // la regla de negocio "no puedes tener saldo negativo".
    // Money es solo "matemáticas puras".
    // ──────────────────────────────────────────────────────────────────
    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    // ══════════════════════════════════════════════════════════════════
    //  MÉTODOS DE CONSULTA — "Preguntas" que le haces al dinero
    // ══════════════════════════════════════════════════════════════════

    // ──────────────────────────────────────────────────────────────────
    // isPositive(): ¿El importe es mayor que cero?
    //
    // Lo usará Account.deposit() para validar que no te depositen 0€
    // o una cantidad negativa (que no tiene sentido).
    //
    // BigDecimal.compareTo() devuelve:
    //   -1 si this < other
    //    0 si this == other
    //   +1 si this > other
    //
    // Así que compareTo(ZERO) > 0 significa "¿soy mayor que cero?"
    // ──────────────────────────────────────────────────────────────────
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    // ──────────────────────────────────────────────────────────────────
    // isZero(): ¿El importe es exactamente cero?
    //
    // Lo usará Account.close() para comprobar que el saldo es cero
    // antes de cerrar la cuenta (no puedes cerrar una cuenta con dinero).
    // ──────────────────────────────────────────────────────────────────
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    // ──────────────────────────────────────────────────────────────────
    // isNegative(): ¿El importe es menor que cero?
    //
    // En teoría un saldo nunca debería ser negativo si las validaciones
    // funcionan bien. Pero tener este método es un check de seguridad
    // adicional — "cinturón y tirantes".
    // ──────────────────────────────────────────────────────────────────
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    // ──────────────────────────────────────────────────────────────────
    // isGreaterThanOrEqual(Money other): ¿Tengo al menos esta cantidad?
    //
    // Lo usará Account.withdraw() para comprobar fondos suficientes:
    //   if (!balance.isGreaterThanOrEqual(amount)) → InsufficientFundsException
    //
    // Ejemplo: balance = 100€, retiro = 50€
    //   balance.isGreaterThanOrEqual(retiro) → 100 >= 50 → true → se permite
    //
    // Ejemplo: balance = 30€, retiro = 50€
    //   balance.isGreaterThanOrEqual(retiro) → 30 >= 50 → false → excepción
    // ──────────────────────────────────────────────────────────────────
    public boolean isGreaterThanOrEqual(Money other) {
        requireSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    // ══════════════════════════════════════════════════════════════════
    //  VALIDACIÓN INTERNA
    // ══════════════════════════════════════════════════════════════════

    // ──────────────────────────────────────────────────────────────────
    // requireSameCurrency(): Comprueba que dos Money tengan la misma moneda.
    //
    // Se usa INTERNAMENTE en add(), subtract() y isGreaterThanOrEqual().
    // Si las monedas no coinciden, lanza CurrencyMismatchException
    // (nuestra excepción de dominio) en vez de IllegalArgumentException
    // (genérica de Java).
    //
    // ¿Por qué un método privado aparte? Principio DRY (Don't Repeat
    // Yourself): esta comprobación se hace en 3 sitios. Si la copiáramos
    // en cada uno, al cambiar algo tendríamos que cambiarlo 3 veces.
    // ──────────────────────────────────────────────────────────────────
    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "El Money a comparar no puede ser nulo");
        if (!this.currency.equals(other.currency)) {
            throw new CurrencyMismatchException(this.currency, other.currency);
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  EQUALS, HASHCODE, TOSTRING — Identidad por valor
    // ══════════════════════════════════════════════════════════════════

    // ──────────────────────────────────────────────────────────────────
    // equals(): ¿Dos Money representan el mismo dinero?
    //
    // SIN este método: Java compara por REFERENCIA (¿son el mismo
    // objeto en memoria?). Dos Money con 100 EUR darían false.
    //
    // CON este método: Java compara por VALOR (¿tienen la misma
    // cantidad y moneda?). Dos Money con 100 EUR dan true.
    //
    // Usamos compareTo() en vez de equals() para el BigDecimal porque:
    //   new BigDecimal("10.0").equals(new BigDecimal("10.00")) → false (!)
    //   new BigDecimal("10.0").compareTo(new BigDecimal("10.00")) == 0 → true (✓)
    // Nosotros ya normalizamos la escala, pero por seguridad usamos compareTo().
    // ──────────────────────────────────────────────────────────────────
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0
                && currency.equals(money.currency);
    }

    // ──────────────────────────────────────────────────────────────────
    // hashCode(): La "pareja obligatoria" de equals().
    //
    // Regla de Java: si dos objetos son iguales según equals(),
    // DEBEN devolver el mismo hashCode(). Si no cumples esto,
    // HashMap y HashSet se rompen (no encuentran objetos que sí existen).
    //
    // Objects.hash() calcula un hash combinando los campos que usamos
    // en equals(). Usamos amount.stripTrailingZeros() para que
    // 10.1000 y 10.1 produzcan el mismo hash (coherente con compareTo).
    // ──────────────────────────────────────────────────────────────────
    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }

    // ──────────────────────────────────────────────────────────────────
    // toString(): Representación legible para humanos.
    //
    //   Money.of(100, "EUR").toString() → "100.0000 EUR"
    //
    // Útil para logs, mensajes de error, y debugging.
    // ──────────────────────────────────────────────────────────────────
    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency.getCurrencyCode();
    }
}
