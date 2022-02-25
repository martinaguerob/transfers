package com.nttdata.transfers.service;

@FunctionalInterface
public interface Calculate {

    Float calcular(Float monto, Float saldo);

}
