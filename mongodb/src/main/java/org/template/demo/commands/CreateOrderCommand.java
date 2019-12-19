package org.template.demo.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.template.demo.address.Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderCommand {

    @TargetAggregateIdentifier
    private String orderId;

    private String accountId;

    private Address address;
}
