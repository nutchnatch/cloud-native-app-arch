package org.template.demo.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.template.demo.address.AddressDto;
import org.template.demo.creditcard.CreditCardDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAccountCommand {

    @TargetAggregateIdentifier
    private String accountId;
    @TargetAggregateIdentifier
    private String accountNumber;
    private AddressDto address;
    private CreditCardDto creditCard;
}
