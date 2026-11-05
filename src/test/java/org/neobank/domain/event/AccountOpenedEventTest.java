package org.neobank.domain.event;

import org.junit.jupiter.api.Test;
import org.neobank.domain.model.AccountId;
import org.neobank.domain.model.UserId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.UUID;

public class AccountOpenedEventTest {
    @Test
    void should_preserve_event_data_when_created() {
        // 1. Prepara un AccountId, un UserId y un Instant conocidos.
        // 2. Construye AccountOpenedEvent con esos tres valores.
        // 3. Comprueba con AssertJ que sus accesores devuelven esos valores.
        AccountId accountId = AccountId.generate();
        UserId userId= UserId.of(UUID.randomUUID());
        Instant instant = Instant.parse("2026-10-28T10:00:00Z");

        AccountOpenedEvent event = new AccountOpenedEvent(accountId, userId, instant);
        assertThat(event.accountId()).isEqualTo(accountId);
        assertThat(event.userId()).isEqualTo(userId);
        assertThat(event.occurredOn()).isEqualTo(instant);

    }

    @Test
    void should_reject_event_when_account_id_is_null() {
        UserId userId = UserId.of(UUID.randomUUID());
        Instant occurredOn = Instant.parse("2026-10-28T10:00:00Z");

        assertThatThrownBy(() -> new AccountOpenedEvent(null, userId, occurredOn))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_event_when_user_id_is_null() {
        AccountId accountId = AccountId.generate();
        Instant occurredOn = Instant.parse("2026-10-28T10:00:00Z");

        assertThatThrownBy(() -> new AccountOpenedEvent(accountId, null, occurredOn))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_event_when_occurred_on_is_null() {
        AccountId accountId = AccountId.generate();
        UserId userId = UserId.of(UUID.randomUUID());

        assertThatThrownBy(() -> new AccountOpenedEvent(accountId, userId, null))
                .isInstanceOf(NullPointerException.class);
    }
}

