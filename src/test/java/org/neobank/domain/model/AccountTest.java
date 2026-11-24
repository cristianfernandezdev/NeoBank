package org.neobank.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest {

    @Test
    void should_open_account_when_data_is_valid() {
        UserId userId = UserId.of(UUID.randomUUID());
        Currency currency = Currency.getInstance("EUR");

        Account account = Account.open(userId, currency);

        assertThat(account.getId()).isNotNull();
        assertThat(account.getCreatedAt()).isNotNull();
        assertThat(account.getUserId()).isEqualTo(userId);
        assertThat(account.getBalance()).isEqualTo(Money.zero(currency));
        assertThat(account.getStatus()).isEqualTo(AccountStatus.ACTIVE);
    }

    @Test
    void should_reject_account_when_user_id_is_null() {
        Currency currency = Currency.getInstance("EUR");

        assertThatThrownBy(() -> Account.open(null, currency))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_account_when_currency_is_null() {
        UserId userId = UserId.of(UUID.randomUUID());

        assertThatThrownBy(() -> Account.open(userId, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_preserve_account_data_when_reconstituted() {
        AccountId accountId = AccountId.generate();
        UserId userId = UserId.of(UUID.randomUUID());
        Money balance = Money.of(125, "EUR");
        AccountStatus status = AccountStatus.BLOCKED;
        Instant createdAt = Instant.parse("2026-10-28T10:00:00Z");

        Account account = Account.reconstitute(accountId, userId, balance, status, createdAt);

        assertThat(account.getId()).isEqualTo(accountId);
        assertThat(account.getUserId()).isEqualTo(userId);
        assertThat(account.getBalance()).isEqualTo(balance);
        assertThat(account.getStatus()).isEqualTo(status);
        assertThat(account.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void should_reject_reconstitution_when_account_id_is_null() {
        UserId userId = UserId.of(UUID.randomUUID());
        Money balance = Money.of(125, "EUR");
        Instant createdAt = Instant.parse("2026-10-28T10:00:00Z");

        assertThatThrownBy(() -> Account.reconstitute(null, userId, balance, AccountStatus.BLOCKED, createdAt))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_reconstitution_when_user_id_is_null() {
        AccountId accountId = AccountId.generate();
        Money balance = Money.of(125, "EUR");
        Instant createdAt = Instant.parse("2026-10-28T10:00:00Z");

        assertThatThrownBy(() -> Account.reconstitute(accountId, null, balance, AccountStatus.BLOCKED, createdAt))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_reconstitution_when_balance_is_null() {
        AccountId accountId = AccountId.generate();
        UserId userId = UserId.of(UUID.randomUUID());
        Instant createdAt = Instant.parse("2026-10-28T10:00:00Z");

        assertThatThrownBy(() -> Account.reconstitute(accountId, userId, null, AccountStatus.BLOCKED, createdAt))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_reconstitution_when_status_is_null() {
        AccountId accountId = AccountId.generate();
        UserId userId = UserId.of(UUID.randomUUID());
        Money balance = Money.of(125, "EUR");
        Instant createdAt = Instant.parse("2026-10-28T10:00:00Z");

        assertThatThrownBy(() -> Account.reconstitute(accountId, userId, balance, null, createdAt))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_reject_reconstitution_when_created_at_is_null() {
        AccountId accountId = AccountId.generate();
        UserId userId = UserId.of(UUID.randomUUID());
        Money balance = Money.of(125, "EUR");

        assertThatThrownBy(() -> Account.reconstitute(accountId, userId, balance, AccountStatus.BLOCKED, null))
                .isInstanceOf(NullPointerException.class);
    }
}
