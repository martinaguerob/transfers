package com.nttdata.transfers.controller;

import com.nttdata.transfers.entity.Transfers;
import com.nttdata.transfers.service.TransfersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    @Autowired
    TransfersService transfersService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Transfers> getTransfers(){
        System.out.println("Listar transferencias");
        return transfersService.findAll();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Transfers> saveTransfers(@RequestBody Transfers transfers){
        return transfersService.save(transfers);
    }
}
