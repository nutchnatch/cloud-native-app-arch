package org.template.demo.catalog;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface CatalogRepository extends GraphRepository<Catalog> {
}
