package org.template.demo.commands;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.template.core.AccountService;
import org.template.demo.events.AccountAddEvent;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class AccountAggregate {

    @Autowired
    private AccountService accountService;

    @AggregateIdentifier
    private String accountId;

    @AggregateIdentifier
    private String accountNumber;

    public AccountAggregate() {

    }

    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        this.accountNumber = command.getAccountNumber();
        apply(new AccountAddEvent(command.getAccountNumber(), command.getAddress(), command.getCreditCard()));
    }

    @EventSourcingHandler
    protected void on(AccountAddEvent event) {
        System.out.println("event");
    }
}
