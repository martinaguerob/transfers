package com.nttdata.transfers.service;

import com.nttdata.transfers.entity.Transfers;
import com.nttdata.transfers.model.MovementBankAccount;
import reactor.core.publisher.Mono;

public interface TransfersService extends CrudService<Transfers, String> {

    //Movement
    Mono<MovementBankAccount> saveMovementSourceAccount(String numberAccount, Float amount);
    Mono<MovementBankAccount> saveMovementDestinationAccount(String numberAccount, Float amount);
}
