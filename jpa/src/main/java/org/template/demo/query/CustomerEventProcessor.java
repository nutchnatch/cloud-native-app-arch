package org.template.demo.query;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.template.demo.account.Account;
import org.template.demo.account.AccountRepository;
import org.template.demo.address.Address;
import org.template.demo.address.AddressDto;
import org.template.demo.creditcard.CreditCard;
import org.template.demo.creditcard.CreditCardDto;
import org.template.demo.customer.Customer;
import org.template.demo.customer.CustomerRepository;
import org.template.demo.events.AccountAddEvent;
import org.template.demo.events.CustomerAddEvent;

import java.util.*;

@Component
public class CustomerEventProcessor {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @EventHandler
    public void on(AccountAddEvent event) {

        Address address = fromAddressDto(event.getAddress());
        Set<Address> addresses = new HashSet<>();
        addresses.add(address);
        Account account = new Account(event.getAccountNumber(), addresses);
//        account.setId(event.getAccountId());
        account.getCreditCards().add(fromCreditCardDto(event.getCreditCard()));

        accountRepository.save(account);
    }

    private Address fromAddressDto(AddressDto addressDto) {
        Address address = new Address();
        address.setStreet1(addressDto.getStreet1());
        address.setStreet2(addressDto.getStreet2());
        address.setState(addressDto.getState());
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setZipCode(addressDto.getZipCode());
        return address;
    }

    private CreditCard fromCreditCardDto(CreditCardDto creditCardDto) {
        CreditCard creditCard = new CreditCard();
        creditCard.setNumber(creditCardDto.getNumber());
        creditCard.setType(creditCardDto.getType());
        return creditCard;
    }

    @EventHandler
    public void on(CustomerAddEvent event) {

        Optional<Account> account = accountRepository.findByAccountNumber(event.getAccountNumber());
        account.map(currAccount -> {
            Customer customer = new Customer(event.getFirstName(), event.getLastName(), event.getEmail(), currAccount);
            this.customerRepository.save(customer);
            return currAccount;
        });
    }
}
