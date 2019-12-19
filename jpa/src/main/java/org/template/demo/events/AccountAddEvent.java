package org.template.demo.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.template.demo.address.AddressDto;
import org.template.demo.creditcard.CreditCardDto;

@Data
@AllArgsConstructor
public class AccountAddEvent {

//    private String accountId;
    private String accountNumber;
    private AddressDto address;
    private CreditCardDto creditCard;
}
