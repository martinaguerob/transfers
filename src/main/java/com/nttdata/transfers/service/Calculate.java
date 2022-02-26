package com.nttdata.transfers.service;

@FunctionalInterface
public interface Calculate {

    Double calcular(Double monto, Double saldo);

}
