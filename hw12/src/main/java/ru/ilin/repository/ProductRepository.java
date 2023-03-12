package ru.ilin.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.ilin.document.Product;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
