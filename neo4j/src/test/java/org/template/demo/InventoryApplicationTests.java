package org.template.demo;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.template.demo.address.Address;
import org.template.demo.address.AddressRepository;
import org.template.demo.catalog.Catalog;
import org.template.demo.catalog.CatalogRepository;
import org.template.demo.inventory.Inventory;
import org.template.demo.inventory.InventoryRepository;
import org.template.demo.product.Product;
import org.template.demo.product.ProductRepository;
import org.template.demo.shipment.Shipment;
import org.template.demo.shipment.ShipmentRepository;
import org.template.demo.shipment.ShipmentStatus;
import org.template.demo.warehouse.Warehouse;
import org.template.demo.warehouse.WarehouseRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.template.demo.inventory.InventoryStatus.IN_STOCK;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApplication.class)
public class InventoryApplicationTests {

    @Autowired
    private ProductRepository products;

    @Autowired
    private ShipmentRepository shipments;

    @Autowired
    private WarehouseRepository warehouses;

    @Autowired
    private AddressRepository addresses;

    @Autowired
    private CatalogRepository catalogs;

    @Autowired
    private InventoryRepository inventories;

    @Autowired
    private Session session;

    @Before
    public void setup() {
        try {
            this.session.query("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n, r;",
                    Collections.emptyMap()).queryResults();
        }
        catch (Exception e) {
            Assert.fail("can't connect to Neo4j! " + ExceptionUtils.getMessage(e));
        }
    }

    @Test
    public void inventoryTest() {

        // <1>
        List<Product> products = Stream
                .of(
                        new Product("Best. Cloud. Ever. (T-Shirt, Men's Large)", "SKU-24642", 21.99),
                        new Product("Like a BOSH (T-Shirt, Women's Medium)", "SKU-34563", 14.99),
                        new Product("We're gonna need a bigger VM (T-Shirt, Women's Small)",
                                "SKU-12464", 13.99),
                        new Product("cf push awesome (Hoodie, Men's Medium)", "SKU-64233", 21.99))
                .map(p -> this.products.save(p)).collect(Collectors.toList());

        Product sample = products.get(0);
        Assert.assertEquals(this.products.findOne(sample.getId()).getUnitPrice(),
                sample.getUnitPrice());

        // <2>
        this.catalogs.save(new Catalog("Spring Catalog", products));

        // <3>
        Address warehouseAddress = this.addresses.save(new Address("875 Howard St",
                null, "CA", "San Francisco", "United States", 94103));
        Address shipToAddress = this.addresses.save(new Address(
                "1600 Amphitheatre Parkway", null, "CA", "Mountain View", "United States",
                94043));

        // <4>
        Warehouse warehouse = this.warehouses.save(new Warehouse("Pivotal SF",
                warehouseAddress));
        Set<Inventory> inventories = products
                .stream()
                .map(
                        p -> this.inventories.save(new Inventory(UUID.randomUUID().toString(), p,
                                warehouse, IN_STOCK))).collect(Collectors.toSet());
        Shipment shipment = shipments.save(new Shipment(inventories, shipToAddress,
                warehouse, ShipmentStatus.SHIPPED));
        Assert.assertEquals(shipment.getInventories().size(), inventories.size());
    }
}
