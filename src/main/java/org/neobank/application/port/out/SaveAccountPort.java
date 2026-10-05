package org.neobank.application.port.out;

import org.neobank.domain.model.Account;

public interface SaveAccountPort {
    void save(Account account);
}