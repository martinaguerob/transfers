package com.nttdata.transfers.repository;

import com.nttdata.transfers.entity.Transfers;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransfersRepository extends ReactiveMongoRepository<Transfers, String> {
}
