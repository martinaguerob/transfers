package com.nttdata.transfers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection = "transfers")
public class Transfers {

    @Id
    private String id;
    private String sourceAccount;
    private String destinationAccount;
    private Double amount;
    private String comment;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date date;
    private Boolean status;
}
