package org.template.demo.query;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.template.demo.events.ItemAddEvent;
import org.template.demo.events.OrderAddEvent;
import org.template.demo.events.OrderFinishedEvent;
import org.template.demo.events.OrderSubmitEvent;
import org.template.demo.invoice.Invoice;
import org.template.demo.invoice.InvoiceRepository;
import org.template.demo.order.LineItem;
import org.template.demo.order.Order;
import org.template.demo.order.OrderRepository;
import org.template.demo.order.OrderStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Component
public class OrderEventProcessor {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    OrderEventProcessor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventHandler
    public void on(OrderAddEvent event) {

        Order order = new Order(event.getId(), event.getAccountId(), event.getAddress());
        this.orderRepository.save(order);

        Invoice invoice = new Invoice(event.getAccountId(), event.getAddress());
        invoice.addOrder(order);
        this.invoiceRepository.save(invoice);
    }

    @EventHandler
    public void on(ItemAddEvent event) {
        LineItem lineItem = new LineItem(
                event.getName(),
                event.getProductId(),
                event.getQuantity(),
                event.getPrice(),
                event.getTax());
        Order order = this.orderRepository.findOne(event.getOrderId());
        order.addLineItem(lineItem);
        this.orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderSubmitEvent event) {
        Order order = this.orderRepository.findOne(event.getOrderId());
        order.setOrderStatus(OrderStatus.CONFIRMED);
        this.orderRepository.save(order);

        Map<String, Integer> productMap = new HashMap<>();
        order.getLineItems().stream()
                .forEach(item -> productMap.computeIfPresent(item.getProductId(), (i, v) -> ++v ));
//        apply(new OrderFinishedEvent(productMap, order.getShippingAddress()));
    }
}
