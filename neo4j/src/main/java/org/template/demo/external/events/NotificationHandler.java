package org.template.demo.external.events;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.template.demo.address.Address;
import org.template.demo.address.AddressRepository;
import org.template.demo.catalog.Catalog;
import org.template.demo.catalog.CatalogRepository;
import org.template.demo.events.OrderFinishedEvent;
import org.template.demo.inventory.Inventory;
import org.template.demo.inventory.InventoryRepository;
import org.template.demo.inventory.InventoryStatus;
import org.template.demo.product.Product;
import org.template.demo.product.ProductRepository;
import org.template.demo.shipment.Shipment;
import org.template.demo.shipment.ShipmentRepository;
import org.template.demo.shipment.ShipmentStatus;
import org.template.demo.warehouse.Warehouse;
import org.template.demo.warehouse.WarehouseRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ProcessingGroup("shipments")
@RestController
public class NotificationHandler {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private WarehouseRepository warehouses;

    @Autowired
    private AddressRepository addresses;

    @Autowired
    private CatalogRepository catalogs;

    @Autowired
    private InventoryRepository inventories;

    @Autowired
    private ProductRepository productRepository;

    @EventHandler
    public void on(OrderFinishedEvent event) {

//        addProducts();
//        List<Product> products = new ArrayList<>();
//        Map<String, Integer> prodMap = event.getProductQuantityMap();
//        prodMap.keySet().stream()
//                .map(prodName -> {
//                    Integer quant = Integer.class.cast(prodMap.get(prodName));
//                    Optional<Product> product = this.productRepository.findByNameContaining(prodName);
//                    for(int i = 0; i < quant; i ++) {
//                        product.ifPresent(products::add);
//                    }
//                    return prodName;
//                }).collect(Collectors.toList());

        //Should get those products from product service
        List<Product> products = Stream
                .of(
                        new Product("Best. Cloud. Ever. (T-Shirt, Men's Large)", "SKU-24642", 21.99),
                        new Product("Like a BOSH (T-Shirt, Women's Medium)", "SKU-34563", 14.99),
                        new Product("We're gonna need a bigger VM (T-Shirt, Women's Small)",
                                "SKU-12464", 13.99),
                        new Product("cf push awesome (Hoodie, Men's Medium)", "SKU-64233", 21.99))
                .map(p -> this.productRepository.save(p)).collect(Collectors.toList());

        this.catalogs.save(new Catalog("Spring Catalog", products));

        // <3>
        Address warehouseAddress = this.addresses.save(new Address("875 Howard St",
                null, "CA", "San Francisco", "United States", 94103));

        //Should get those addresses from account service
        Address shipToAddress = this.addresses.save(new Address(
                "1600 Amphitheatre Parkway", null, "CA", "Mountain View", "United States",
                94043));

//        Address shipToAddress = event.getShipmentAddress();

        // <4>
        Warehouse warehouse = this.warehouses.save(new Warehouse("Pivotal SF",
                warehouseAddress));
        Set<Inventory> inventories = products
                .stream()
                .map(
                        p -> this.inventories.save(new Inventory(UUID.randomUUID().toString(), p,
                                warehouse, InventoryStatus.IN_STOCK))).collect(Collectors.toSet());
        Shipment shipment = shipmentRepository.save(new Shipment(inventories, shipToAddress,
                warehouse, ShipmentStatus.SHIPPED));
    }

    private void addProducts() {

        List<Product> products = Stream
                .of(
                        new Product("Best. Cloud. Ever. (T-Shirt, Men's Large)", "SKU-24642", 21.99),
                        new Product("Like a BOSH (T-Shirt, Women's Medium)", "SKU-34563", 14.99),
                        new Product("We're gonna need a bigger VM (T-Shirt, Women's Small)",
                                "SKU-12464", 13.99),
                        new Product("cf push awesome (Hoodie, Men's Medium)", "SKU-64233", 21.99))
                .map(p -> this.productRepository.save(p)).collect(Collectors.toList());
    }
}
