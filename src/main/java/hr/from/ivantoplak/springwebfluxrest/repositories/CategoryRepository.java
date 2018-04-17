package hr.from.ivantoplak.springwebfluxrest.repositories;

import hr.from.ivantoplak.springwebfluxrest.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
