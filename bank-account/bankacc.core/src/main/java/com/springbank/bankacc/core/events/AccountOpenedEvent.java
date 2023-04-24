package com.springbank.bankacc.core.events;

import com.springbank.bankacc.core.models.AccountType;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;

@Data
@Builder
public class AccountOpenedEvent {

    @TargetAggregateIdentifier
    private String id;

    private String accountHolderId;
    private AccountType accountType;
    private double openingBalance;
    private Date creationDate;
}
