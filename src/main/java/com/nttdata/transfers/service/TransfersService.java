package com.nttdata.transfers.service;

import com.nttdata.transfers.entity.Transfers;
import com.nttdata.transfers.model.MovementBankAccount;
import reactor.core.publisher.Mono;

public interface TransfersService extends CrudService<Transfers, String> {

    //Movement
    Mono<MovementBankAccount> saveMovementAccount(String numberAccount, Double amount, String description);

}
