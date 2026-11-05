package org.neobank.domain.event;

import org.junit.jupiter.api.Test;
import org.neobank.domain.model.AccountId;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountBlockedEventTest {

    @Test
    void should_preserve_event_data_when_created() {
        AccountId accountId = AccountId.generate();
        Instant occurredOn = Instant.parse("2026-10-28T10:00:00Z");

        AccountBlockedEvent event = new AccountBlockedEvent(accountId, occurredOn);

        assertThat(event.accountId()).isEqualTo(accountId);
        assertThat(event.occurredOn()).isEqualTo(occurredOn);
    }

    @Test
    void should_reject_event_when_account_id_is_null() {
        Instant occurredOn = Instant.parse("2026-10-28T10:00:00Z");

        assertThatThrownBy(() -> new AccountBlockedEvent(null, occurredOn))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_event_when_occurred_on_is_null() {
        AccountId accountId = AccountId.generate();

        assertThatThrownBy(() -> new AccountBlockedEvent(accountId, null))
                .isInstanceOf(NullPointerException.class);
    }
}
