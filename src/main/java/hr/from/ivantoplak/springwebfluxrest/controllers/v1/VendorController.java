package hr.from.ivantoplak.springwebfluxrest.controllers.v1;

import hr.from.ivantoplak.springwebfluxrest.domain.Vendor;
import hr.from.ivantoplak.springwebfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VendorController.BASE_URL)
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Vendor> list() {

        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Vendor> getById(@PathVariable String id) {

        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream) {

        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {

        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {

        return vendorRepository.findById(id)
                .flatMap(foundVendor -> {

                    if (vendor.getFirstName() != null) {
                        foundVendor.setFirstName(vendor.getFirstName());
                    }

                    if (vendor.getLastName() != null) {
                        foundVendor.setLastName(vendor.getLastName());
                    }

                    return vendorRepository.save(foundVendor);
                });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> delete(@PathVariable String id) {

        return vendorRepository.deleteById(id).then();
    }
}
