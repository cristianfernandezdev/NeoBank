package org.neobank.domain.event;

import org.junit.jupiter.api.Test;
import org.neobank.domain.model.AccountId;
import org.neobank.domain.model.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyWithdrawnEventTest {

    @Test
    void should_preserve_event_data_when_created() {
        Money money = Money.of(new BigDecimal("20.50"), Currency.getInstance("EUR"));
        AccountId accountId = AccountId.generate();
        Instant occurredOn = Instant.parse("2026-10-28T10:00:00Z");

        MoneyWithdrawnEvent event = new MoneyWithdrawnEvent(money, accountId, occurredOn);

        assertThat(event.money()).isEqualTo(money);
        assertThat(event.accountId()).isEqualTo(accountId);
        assertThat(event.occurredOn()).isEqualTo(occurredOn);
    }

    @Test
    void should_reject_event_when_money_is_null() {
        AccountId accountId = AccountId.generate();
        Instant occurredOn = Instant.parse("2026-10-28T10:00:00Z");

        assertThatThrownBy(() -> new MoneyWithdrawnEvent(null, accountId, occurredOn))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_event_when_account_id_is_null() {
        Money money = Money.of(20, "EUR");
        Instant occurredOn = Instant.parse("2026-10-28T10:00:00Z");

        assertThatThrownBy(() -> new MoneyWithdrawnEvent(money, null, occurredOn))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_event_when_occurred_on_is_null() {
        Money money = Money.of(20, "EUR");
        AccountId accountId = AccountId.generate();

        assertThatThrownBy(() -> new MoneyWithdrawnEvent(money, accountId, null))
                .isInstanceOf(NullPointerException.class);
    }
}
