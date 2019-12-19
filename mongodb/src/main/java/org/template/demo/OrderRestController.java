package org.template.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.template.demo.commands.AddItemCommand;
import org.template.demo.commands.CancelOrderCommand;
import org.template.demo.commands.CreateOrderCommand;
import org.template.demo.commands.SubmitOrderCommand;
import org.template.demo.address.Address;
import org.template.demo.address.AddressType;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/orders", consumes = APPLICATION_JSON_VALUE)
public class OrderRestController {

    @Autowired
    private final CommandGateway gateway;

    @Autowired
    public OrderRestController(CommandGateway gateway) {
        this.gateway = gateway;
    }

    @PostMapping
    CompletableFuture<ResponseEntity> createOrders(@RequestBody Map<String, String> body) {
        String id = UUID.randomUUID().toString();
        String street1 = body.get("street1");
        String street2 = body.get("street2");
        String state = body.get("state");
        String city = body.get("city");
        String country = body.get("country");
        Integer zipCode = Integer.parseInt(body.get("zipCode"));
        Address address = new Address(street1, street2, state, city, country, zipCode, AddressType.SHIPPING);
        CreateOrderCommand order = new CreateOrderCommand(id, body.get("account"), address);

        return this.gateway.send(order).thenApply(
                orderId -> {
                    URI uri = uri("/orders/{id}", Collections.singletonMap("id", order.getOrderId()));
                    return ResponseEntity.created(uri).build();
                }
        );
    }

    @PostMapping("/{orderId}/items")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CompletableFuture<ResponseEntity> addItem(@PathVariable String orderId, @RequestBody Map<String, Object> body) {

        int quantity = Integer.class.cast(body.get("quantity"));
        double price = Double.class.cast(body.get("price"));
        double tax = Double.class.cast(body.get("tax"));
        AddItemCommand command = new AddItemCommand(orderId, UUID.randomUUID().toString(),
                String.class.cast(body.get("name")), String.class.cast(body.get("productId")),
                quantity, price, tax);

        return this.gateway.send(command)
                .thenApply(commandId -> {

                    Map<String, String> parms = new HashMap<>();
                    parms.put("orderId", orderId);
                    parms.put("itemId", command.getItemId());
                    URI uri = uri("/orders/{orderId}/items/{itemId}", parms);
                    return ResponseEntity.created(uri).build();
                });
    }

    @DeleteMapping("/{orderId}")
    CompletableFuture<ResponseEntity> cancelOrder(@PathVariable String orderId) {

        CancelOrderCommand cancelOrderCommand = new CancelOrderCommand(orderId);
        return this.gateway.send(cancelOrderCommand)
                .thenApply(none -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{orderId}")
    CompletableFuture<ResponseEntity> submitOrder(@PathVariable String orderId) {

        SubmitOrderCommand command = new SubmitOrderCommand(orderId);
        return this.gateway.send(command)
                .thenApply(currOrderId -> ResponseEntity.accepted().build());
    }

    private static URI uri(String uri, Map<String, String> template) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path(uri)
                .build()
                .expand(template);
        return uriComponents.toUri();

    }
}
