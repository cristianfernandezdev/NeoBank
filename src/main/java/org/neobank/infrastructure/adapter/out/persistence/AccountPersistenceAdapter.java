package org.neobank.infrastructure.adapter.out.persistence;

import org.neobank.application.port.out.SaveAccountPort;
import org.neobank.domain.model.Account;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements SaveAccountPort {

    private final SpringDataAccountRepository repository;

    @Override
    public void save(Account account) {
        // 1. Mapeo Manual (Data Mapper)
        // Usamos el constructor que nos generó Lombok (@AllArgsConstructor) en la entidad JPA
        AccountJpaEntity entity = new AccountJpaEntity(
                account.getId(),
                account.getUserId(),
                account.getBalance()
        );

        // 2. Ejecutar la persistencia
        // Spring Data coge la entidad y genera el SQL por debajo
        repository.save(entity);
    }
}