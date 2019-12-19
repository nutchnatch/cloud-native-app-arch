package org.template.demo.commands;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.template.demo.address.Address;
import org.template.demo.events.ItemAddEvent;
import org.template.demo.events.OrderAddEvent;
import org.template.demo.events.OrderFinishedEvent;
import org.template.demo.events.OrderSubmitEvent;
import org.template.demo.order.LineItem;
import org.template.demo.order.Order;
import org.template.demo.order.OrderRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class OrderAggregate {

    @Autowired
    private OrderRepository orderRepository;

    @AggregateIdentifier
    private String orderId;

    private boolean submitted;

    public OrderAggregate() {

    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand orderCommand) {
        apply(new OrderAddEvent(orderCommand.getOrderId(), orderCommand.getAccountId(), orderCommand.getAddress()));
    }

    @CommandHandler
    public void addItem(AddItemCommand command) {
        Assert.hasLength(command.getItemId());
        Assert.hasLength(command.getName());
        Assert.hasLength(command.getOrderId());
        Assert.hasLength(command.getProductId());
        Assert.notNull(command.getPrice());
        Assert.notNull(command.getQuantity());
        Assert.notNull(command.getTax());
        apply(new ItemAddEvent(command.getOrderId(), command.getName(), command.getProductId(), command.getQuantity(),
                command.getPrice(), command.getTax()));
    }

    @CommandHandler
    public void submitOrder(SubmitOrderCommand command) {
        if(!this.submitted) {
            apply(new OrderSubmitEvent(this.orderId));
        }
    }

    @EventSourcingHandler
    protected void on(OrderAddEvent event) {
        this.orderId = event.getId();
        this.submitted = false;
    }

    @EventSourcingHandler
    protected void on(OrderSubmitEvent event) {
        this.submitted = true;

        Order order = this.orderRepository.findOne(event.getOrderId());
        List<LineItem> items = order.getLineItems();
        Map<String, Integer> productMap = new HashMap<>();
        items.stream()
                .forEach(item -> productMap.put(item.getName(), item.getQuantity()));
        apply(new OrderFinishedEvent(productMap, order.getShippingAddress().getStreet1()));
    }
}
