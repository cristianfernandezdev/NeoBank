package org.neobank.application.service;

import lombok.RequiredArgsConstructor;
import org.neobank.application.port.in.CreateAccountUseCase;
import org.neobank.application.port.out.SaveAccountPort;
import org.neobank.domain.model.Account;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase {

    // El servicio necesita comunicarse con el "sótano", así que inyectamos la interfaz (el Puerto).
    // NO inyectamos el repositorio de base de datos directamente para no romper el Hexágono.
    private final SaveAccountPort saveAccountPort;

    @Override
    public UUID create(UUID userId) {
        // 1. Dominio: Fabricamos la cuenta pura de Java (El balance empieza en cero)
        Account newAccount = new Account(userId);

        // 2. Infraestructura: Le decimos al puerto que guarde la cuenta.
        // Spring Boot se encargará mágicamente de llevar esto a tu AccountPersistenceAdapter
        saveAccountPort.save(newAccount);

        // 3. Devolvemos el ID generado para que el usuario sepa cuál es su cuenta
        return newAccount.getId();
    }
}