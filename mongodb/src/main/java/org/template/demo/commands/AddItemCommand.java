package org.template.demo.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddItemCommand {

    @TargetAggregateIdentifier
    private String orderId;

    private String itemId;

    private String name, productId;

    private Integer quantity;

    private Double price, tax;
}
