package hr.from.ivantoplak.springwebfluxrest.repositories;

import hr.from.ivantoplak.springwebfluxrest.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
