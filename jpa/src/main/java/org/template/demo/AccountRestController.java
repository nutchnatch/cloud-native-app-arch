package org.template.demo;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.template.core.AccountService;
import org.template.demo.account.AccountRepository;
import org.template.demo.address.AddressDto;
import org.template.demo.address.AddressType;
import org.template.demo.commands.AddAccountCommand;
import org.template.demo.creditcard.CreditCardDto;
import org.template.demo.creditcard.CreditCardType;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/accounts", consumes = APPLICATION_JSON_VALUE)
public class AccountRestController {

    @Autowired
    private CommandGateway gateway;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository repository;

    @PostMapping
    CompletableFuture<ResponseEntity> createAccount(@RequestBody Map<String, String> body) {

        String id = UUID.randomUUID().toString();
//        Long accountId = Long.valueOf(id);
        String accountNumber = body.get("accountNumber");
        AddressDto address = createAddress(body);
        CreditCardDto creditCard = createCreditCard(body);

//        AddAccountCommand accountCommand = new AddAccountCommand(12L, accountNumber, address, creditCard);
        AddAccountCommand accountCommand = new AddAccountCommand(id, accountNumber, address, creditCard);

        return this.gateway.send(accountCommand).thenApply(
               accountId -> {
                    URI uri = uri("/accounts/{id}", Collections.singletonMap("id", accountCommand.getAccountId()));
                    return ResponseEntity.created(uri).build();
                }
        );
    }

    private AddressDto createAddress(Map<String, String> body) {

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

    @GetMapping("/{id}")
    ResponseEntity getAccount(@PathVariable Long id) {

        return Optional.ofNullable(accountService.getAccount(id))
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

    private static URI uri(String uri, Map<String, String> template) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path(uri)
                .build()
                .expand(template);
        return uriComponents.toUri();
    }
}
