package org.template.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest( classes = { AccountApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles(profiles = "test")
public class AccountRestControllerTest {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String accountJson;
    private String customerJson;

    @Before
    public void setUp() throws JsonProcessingException {

        Map<String, Object> map = getAccountObjectMapper();
        this.accountJson = objectMapper.writeValueAsString(map);
        map = getCustomerObjectMapper();
        this.customerJson = objectMapper.writeValueAsString(map);

        this.log.debug("account Json:" + this.accountJson);
    }

    @Test
    public void newAccountTest() throws Exception {
        newAccount();
    }

    private String newAccount() throws Exception {

        MvcResult accountCreationResult = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.accountJson))
                .andExpect(request().asyncStarted())
                .andReturn();

        AtomicReference<String> accountId = new AtomicReference<>();
        this.mockMvc.perform(asyncDispatch(accountCreationResult))
                .andExpect(mvcResult -> {
                    String location = mvcResult.getResponse().getHeader("Location");
                    String accountPath = "/accounts";
                    Assert.assertTrue(location.contains(accountPath));
                    accountId.set(location.split(accountPath)[1]);
                }).andExpect(status().isCreated());

        String returnedAccountId = accountId.get();

        MvcResult customerCreationResult = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.customerJson))
                .andExpect(request().asyncStarted())
                .andReturn();

        AtomicReference<String> customerId = new AtomicReference<>();
        this.mockMvc.perform(asyncDispatch(customerCreationResult))
                .andExpect(mvcResult -> {
                    String location = mvcResult.getResponse().getHeader("Location");
                    String accountPath = "/customers";
                    Assert.assertTrue(location.contains(accountPath));
                    customerId.set(location.split(accountPath)[1]);
                }).andExpect(status().isCreated());

        String returnedCustomerId = customerId.get();
        return returnedCustomerId;
    }

    private Map<String, Object> getAccountObjectMapper() {

        Map<String, Object> map = new HashMap<>();
        map.put("accountNumber", "1111-1111-1111");
        map.put("street1", "1600 Pennsylvania Ave NW");
        map.put("street2", null);
        map.put("state", "DC");
        map.put("city", "Washington");
        map.put("country", "United States");
        map.put("zipCode", 20500);
        map.put("addressType", "shipping");
        map.put("creditType", "visa");
        map.put("creditNumber", "123-234-345");
        return map;
    }

    private Map<String, Object> getCustomerObjectMapper() {

        Map<String, Object> map = new HashMap<>();
        map.put("accountNumber", "1111-1111-1111");
        map.put("firstName", "Bob");
        map.put("lastName", "Marley");
        map.put("email", "marley@gmail.com");
        return map;
    }
}
