package com.nttdata.transfers.service.impl;

import com.nttdata.transfers.config.WebClientConfig;
import com.nttdata.transfers.entity.Transfers;
import com.nttdata.transfers.model.BankAccount;
import com.nttdata.transfers.model.MovementBankAccount;
import com.nttdata.transfers.repository.TransfersRepository;
import com.nttdata.transfers.service.Calculate;
import com.nttdata.transfers.service.TransfersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class TransfersServiceImpl implements TransfersService {

    @Autowired
    TransfersRepository transfersRepository;
    private final WebClientConfig webClientConfig = new WebClientConfig();

    @Override
    public Flux<Transfers> findAll() {
        return transfersRepository.findAll();
    }

    @Override
    public Mono<Transfers> save(Transfers entity) {
        entity.setDate(new Date());
        entity.setStatus(true);
        entity.setComment(entity.getComment()==null ? " " : entity.getComment());

        Mono<BankAccount> sourceAccount = webClientConfig.getAccountByNumberAccount(entity.getSourceAccount());
        Mono<BankAccount> destinationAccount = webClientConfig.getAccountByNumberAccount(entity.getDestinationAccount());
        Calculate rest = ((monto, saldo) -> saldo - monto);
        Calculate add = ((monto, saldo) -> saldo + monto);
        return sourceAccount
                .filter(c -> c.getBalance() >= entity.getAmount())
                .flatMap(s -> {
                    Float nuevoSaldoS = rest.calcular(entity.getAmount(), s.getBalance());
                    s.setBalance(nuevoSaldoS);
                    return destinationAccount
                            .flatMap(d ->{
                                Float nuevoSaldoD = add.calcular(entity.getAmount(), d.getBalance());
                                d.setBalance(nuevoSaldoD);
                                webClientConfig.updateBankAccount(s, s.getId()).subscribe();
                                this.saveMovementSourceAccount(entity.getSourceAccount(), entity.getAmount()).subscribe();
                                webClientConfig.updateBankAccount(d, d.getId()).subscribe();
                                this.saveMovementDestinationAccount(entity.getDestinationAccount(), entity.getAmount()).subscribe();
                                return transfersRepository.save(entity);
                            });
                }).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE)));
    }

    @Override
    public Mono<Transfers> update(Transfers entity) {
        return null;
    }

    @Override
    public Mono<Transfers> deleteById(String id) {
        return transfersRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT)))
                .flatMap(t -> {
                    t.setStatus(false);
                    return transfersRepository.save(t);
                });
    }

    @Override
    public Mono<MovementBankAccount> saveMovementSourceAccount(String numberAccount, Float amount) {
        MovementBankAccount movementBankAccount = new MovementBankAccount();
        movementBankAccount.setAmount(amount);
        movementBankAccount.setDescription("Transferencia a cuenta");
        movementBankAccount.setNumberAccount(numberAccount);
        movementBankAccount.setStatus(true);
        return  webClientConfig.saveMovementBankAccount(movementBankAccount);
    }

    @Override
    public Mono<MovementBankAccount> saveMovementDestinationAccount(String numberAccount, Float amount) {
        MovementBankAccount movementBankAccount = new MovementBankAccount();
        movementBankAccount.setAmount(amount);
        movementBankAccount.setDescription("Transferencia de cuenta");
        movementBankAccount.setNumberAccount(numberAccount);
        movementBankAccount.setStatus(true);
        return  webClientConfig.saveMovementBankAccount(movementBankAccount);
    }
}
