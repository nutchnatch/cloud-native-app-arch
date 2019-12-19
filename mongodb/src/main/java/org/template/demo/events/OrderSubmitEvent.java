package org.template.demo.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderSubmitEvent {

    private String orderId;
}
