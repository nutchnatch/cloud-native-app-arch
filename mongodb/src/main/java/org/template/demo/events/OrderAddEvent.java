package org.template.demo.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.template.demo.address.Address;

@Data
@AllArgsConstructor
public class OrderAddEvent {

    private final  String id;
    private final String accountId;
    private Address address;
}
