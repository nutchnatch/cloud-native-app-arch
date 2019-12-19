package org.template.demo.product;

import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.Optional;

public interface ProductRepository extends GraphRepository<Product> {

    Optional<Product> findByNameContaining(String name);
}
