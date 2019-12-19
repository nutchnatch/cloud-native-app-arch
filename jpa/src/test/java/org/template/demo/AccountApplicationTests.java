package org.template.demo;

//import com.mysql.jdbc.AssertionFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.exceptions.AssertionFailedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.template.demo.AccountApplication;
import org.template.demo.account.Account;
import org.template.demo.account.AccountRepository;
import org.template.demo.address.Address;
import org.template.demo.address.AddressType;
import org.template.demo.creditcard.CreditCard;
import org.template.demo.creditcard.CreditCardType;
import org.template.demo.customer.Customer;
import org.template.demo.customer.CustomerRepository;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = AccountApplication.class)
@ActiveProfiles(profiles = "test")
public class AccountApplicationTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String customerJson;

    @Before
    public void setUp() throws JsonProcessingException {

        Map<String, Object> map = getCustomerObjectMapper();
    }


    @Test
    public void customerTest() throws Exception {
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

    private Map<String, Object> getCustomerObjectMapper() {

        Map<String, Object> map = new HashMap<>();
//        map.put("accountNumber", "1111-1111-1111");
//        map.put("street1", "1600 Pennsylvania Ave NW");
//        map.put("street2", null);
//        map.put("state", "DC");
//        map.put("city", "Washington");
//        map.put("country", "United States");
//        map.put("zipCode", 20500);
//        map.put("addressType", "shipping");
//        map.put("creditType", "visa");
//        map.put("creditNumber", "123-234-345");

        map.put("firstName", "Bob");
        map.put("lastName", "Marley");
        map.put("email", "marley@gmail.com");
        return map;
    }
}
