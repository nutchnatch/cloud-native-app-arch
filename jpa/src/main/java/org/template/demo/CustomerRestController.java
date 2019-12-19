package org.template.demo;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.template.demo.address.Address;
import org.template.demo.address.AddressDto;
import org.template.demo.address.AddressType;
import org.template.demo.commands.AddAccountCommand;
import org.template.demo.commands.AddCustomerCommand;
import org.template.demo.creditcard.CreditCard;
import org.template.demo.creditcard.CreditCardDto;
import org.template.demo.creditcard.CreditCardType;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/customers", consumes = APPLICATION_JSON_VALUE)
public class CustomerRestController {

    @Autowired
    private CommandGateway gateway;

    @PostMapping
    CompletableFuture<ResponseEntity> createCustomer(@RequestBody Map<String, String> body) {
        String id = UUID.randomUUID().toString();
//        Long customerId = Long.valueOf(id);

        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        String email = body.get("email");

//        AddressDto address = createAddress(body);
//        CreditCardDto creditCard = createCreditCard(body);
        String accountNumber = body.get("accountNumber");
        AddCustomerCommand customerCommand = new AddCustomerCommand(id, firstName, lastName, email, accountNumber);

        return this.gateway.send(customerCommand).thenApply(
                accountId -> {
                    URI uri = uri("/customers/{id}", Collections.singletonMap("id", customerCommand.getCustomerId()));
                    return ResponseEntity.created(uri).build();
                }
        );
    }

    private AddressDto createAddress(Map<String, String> body) {

        String id = UUID.randomUUID().toString();
        String street1 = body.get("street1");
        String street2 = body.get("street2");
        String state = body.get("state");
        String city = body.get("city");
        String country = body.get("country");
        Integer zipCode = Integer.parseInt(body.get("zipCode"));
        String addressType = body.get("addressType");
        AddressType type = addressType.equals("shipping") ? AddressType.SHIPPING : AddressType.BILLING;
        return new AddressDto(street1, street2, state, city, country, type, zipCode);
    }

    private CreditCardDto createCreditCard(Map<String, String> body) {

        String id = UUID.randomUUID().toString();
        String creditCardNumber = body.get("creditNumber");
        String creditType = body.get("creditType");
        CreditCardType creditCardType = creditType.equals("visa") ? CreditCardType.VISA : CreditCardType.MASTERCARD;
        return new CreditCardDto(creditCardNumber, creditCardType);
    }

    private static URI uri(String uri, Map<String, String> template) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path(uri)
                .build()
                .expand(template);
        return uriComponents.toUri();
    }
}
