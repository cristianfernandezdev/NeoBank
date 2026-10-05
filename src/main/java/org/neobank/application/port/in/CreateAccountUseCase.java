package org.neobank.application.port.in;

import java.util.UUID;

// Esta interfaz es el contrato. Le dice al mundo web:
// "Si quieres crear una cuenta, pásame el UUID de un usuario y yo te devolveré el UUID de la nueva cuenta".
public interface CreateAccountUseCase {
    UUID create(UUID userId);
}