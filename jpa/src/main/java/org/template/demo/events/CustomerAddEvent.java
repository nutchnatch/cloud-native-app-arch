package org.template.demo.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.template.demo.address.Address;

@Data
@AllArgsConstructor
public class CustomerAddEvent {

    private String accountNumber;
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
}
