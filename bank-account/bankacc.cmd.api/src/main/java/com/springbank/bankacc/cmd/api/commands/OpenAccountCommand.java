package com.springbank.bankacc.cmd.api.commands;

import com.springbank.bankacc.core.models.AccountType;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class OpenAccountCommand {

    @TargetAggregateIdentifier
    private String id;

    @NotNull(message = "Account Holder Id cannot be null")
    private String accountHolderId;

    @NotNull(message = "Account Type cannot be null")
    private AccountType accountType;

    @Min(value = 50, message = "Opening Balance must be at least 50")
    private double openingBalance;
}
