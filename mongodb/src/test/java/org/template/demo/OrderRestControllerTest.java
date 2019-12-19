package org.template.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.commons.logging.Log;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.AssertTrue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = { OrderApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OrderRestControllerTest {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String orderJson;
    private String itemJson;

    @Before
    public void setUp() throws JsonProcessingException {
        Map<String, Object> map = getOrderObjectMap();
        this.orderJson = this.objectMapper.writeValueAsString(map);

        map = getItemObjectMap();
        this.itemJson = this.objectMapper.writeValueAsString(map);
        this.log.debug("order JSON: " + this.orderJson);
        this.log.debug("item JSON: " + this.itemJson);
    }

    @Test
    public void createOrder() throws Exception {
        newOrder();
    }

    private Map<String, Object> getOrderObjectMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("street1", "1600 Pennsylvania Ave NW");
        map.put("street2", null);
        map.put("state", "DC");
        map.put("city", "Washington");
        map.put("country", "United States");
        map.put("zipCode", 20500);
        map.put("account", "111-111-111");
        return map;
    }

    private Map<String, Object> getItemObjectMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("quantity", 1);
        map.put("price", 21.99);
        map.put("tax", .06);
        map.put("name", "Best. Cloud. Ever. (T-Shirt, Men's Large)");
        map.put("productId", "SKU-24642");
        return map;
    }

    private String newOrder() throws Exception {

        MvcResult orderCreationResult = this.mockMvc
                .perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(this.orderJson))
                .andExpect(request().asyncStarted()).andReturn();

        AtomicReference<String> orderId = new AtomicReference<>();

        this.mockMvc.perform(asyncDispatch(orderCreationResult))
                .andExpect(mvcResult -> {
                    String location = mvcResult.getResponse().getHeader("Location");
                    String orderPath = "/orders";
                    Assert.assertTrue(location.contains(orderPath));
                    orderId.set(location.split(orderPath)[1]);
                }).andExpect(status().isCreated());

        String returnedOrderId = orderId.get();

        MvcResult itemCreationResult = this.mockMvc
                .perform(post("/orders/" + returnedOrderId + "/items").contentType(MediaType.APPLICATION_JSON).content(this.itemJson))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(itemCreationResult)).andExpect(status().isCreated());

        MvcResult submitOrderResult = this.mockMvc.perform(put("/orders/" + returnedOrderId).contentType(MediaType.APPLICATION_JSON).content(returnedOrderId))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(submitOrderResult)).andExpect(status().isAccepted());
        return returnedOrderId;
    }
}
