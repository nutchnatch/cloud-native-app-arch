package org.template.demo.commands;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.template.demo.events.CustomerAddEvent;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class CustomerAggregate {

    @AggregateIdentifier
    private String customerId;

    public CustomerAggregate() {

    }

    @CommandHandler
    public CustomerAggregate(AddCustomerCommand command) {
        apply(new CustomerAddEvent(command.getAccountNumber(), command.getCustomerId(), command.getFirstName(), command.getLastName(), command.getEmail()));
    }

    @EventSourcingHandler
    protected void on(CustomerAddEvent event) {
        this.customerId = event.getCustomerId();
    }

}
