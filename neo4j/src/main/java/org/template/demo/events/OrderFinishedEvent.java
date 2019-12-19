package org.template.demo.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.template.demo.address.Address;

import java.util.Map;

@Data
@AllArgsConstructor
public class OrderFinishedEvent {

    private Map<String, Integer> productQuantityMap;

    private String shipmentAddress;
}
