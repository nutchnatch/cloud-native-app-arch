package org.template.demo.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemAddEvent {

    private String orderId;

    private String name, productId;

    private Integer quantity;

    private Double price, tax;
}
