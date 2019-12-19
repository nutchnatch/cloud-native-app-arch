package org.template.demo.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.template.demo.address.AddressDto;
import org.template.demo.creditcard.CreditCardDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerCommand {

    @TargetAggregateIdentifier
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String accountNumber;
//    private AddressDto address;
//    private CreditCardDto creditCard;
}
