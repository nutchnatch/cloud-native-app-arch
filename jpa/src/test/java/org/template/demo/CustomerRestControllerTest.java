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
import org.template.demo.AccountApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@SpringBootTest( classes = {AccountApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@ActiveProfiles(profiles = "test")
public class CustomerRestControllerTest {

//    private Log log = LogFactory.getLog(getClass());
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private String customerJson;
//
//    @Before
//    public void setUp() throws JsonProcessingException {
//
//        Map<String, Object> map = getCustomerObjectMapper();
//        this.customerJson = objectMapper.writeValueAsString(map);
//
//        this.log.debug("customer Json:" + this.customerJson);
//    }

    @Test
    public void createCustomer() throws Exception {
        System.out.println();
    }

//    private String newCustomer() throws Exception {
//
//        MvcResult accountCreationResult = mockMvc.perform(post("/customers")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.customerJson))
//                .andExpect(request().asyncStarted())
//                .andReturn();
//
//        AtomicReference<String> customerId = new AtomicReference<>();
//        this.mockMvc.perform(asyncDispatch(accountCreationResult))
//                .andExpect(mvcResult -> {
//                    String location = mvcResult.getResponse().getHeader("Location");
//                    String accountPath = "/customers";
//                    Assert.assertTrue(location.contains(accountPath));
//                    customerId.set(location.split(accountPath)[1]);
//                }).andExpect(status().isCreated());
//
//        String returnedId = customerId.get();
//
//        return returnedId;
//    }
//
//    private Map<String, Object> getCustomerObjectMapper() {
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("firstName", "Bob");
//        map.put("lastName", "Marley");
//        map.put("email", "marley@gmail.com");
//        return map;
//    }
}
