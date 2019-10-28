package org.template.demo.service;

import com.mysql.jdbc.AssertionFailedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.template.demo.AccountApplication;
import org.template.demo.account.Account;
import org.template.demo.address.Address;
import org.template.demo.address.AddressType;
import org.template.demo.creditcard.CreditCard;
import org.template.demo.creditcard.CreditCardType;
import org.template.demo.customer.Customer;
import org.template.demo.customer.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
@ActiveProfiles(profiles = "development")
public class AccountApplicationTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void customerTest() {
        Account account = new Account("12345");
        Customer customer = new Customer("Jane", "Doe", "jane.doe@gmail.com", account);
        CreditCard creditCard = new CreditCard("1234567890", CreditCardType.VISA);
        customer.getAccount().getCreditCards().add(creditCard);

        String street1 = "1600 Pensylvania Ave NM";
        Address address = new Address(street1, null, "DC", "Washington", "United States", AddressType.SHIPPING, 20500);
        customer.getAccount().getAddresses().add(address);

        customer = customerRepository.save(customer);
        Customer persistedResult = customerRepository.findOne(customer.getId());
        Assert.assertNotNull(persistedResult.getAccount());
        Assert.assertNotNull(persistedResult.getCreatedAt());
        Assert.assertNotNull(persistedResult.getLastModified());

        Assert.assertTrue(persistedResult.getAccount().getAddresses().stream()
                .anyMatch(add -> address.getStreet1().equals(street1)));

        customerRepository.findByEmailContaining(customer.getEmail())
                .orElseThrow(() -> new AssertionFailedException(new RuntimeException("" +
                        "there's supposed to be a matching record!")));
    }
}
