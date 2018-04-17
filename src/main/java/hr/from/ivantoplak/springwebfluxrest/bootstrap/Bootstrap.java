package hr.from.ivantoplak.springwebfluxrest.bootstrap;

import hr.from.ivantoplak.springwebfluxrest.domain.Category;
import hr.from.ivantoplak.springwebfluxrest.domain.Vendor;
import hr.from.ivantoplak.springwebfluxrest.repositories.CategoryRepository;
import hr.from.ivantoplak.springwebfluxrest.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) {

        //load data
        log.info("### Loading data on bootstrap ###");

        loadCategories();

        loadVendors();

    }

    private void loadVendors() {
        if (vendorRepository.count().block() == 0) {

            vendorRepository.save(Vendor.builder().firstName("Jimmy").lastName("Page").build()).block();
            vendorRepository.save(Vendor.builder().firstName("John").lastName("Bonham").build()).block();
            vendorRepository.save(Vendor.builder().firstName("John Paul").lastName("Jones").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Robert").lastName("Plant").build()).block();

            log.info("Loaded vendors:" + vendorRepository.count().block());
        }
    }

    private void loadCategories() {
        if (categoryRepository.count().block() == 0) {

            categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Nuts").build()).block();
            categoryRepository.save(Category.builder().description("Breads").build()).block();
            categoryRepository.save(Category.builder().description("Meats").build()).block();
            categoryRepository.save(Category.builder().description("Eggs").build()).block();

            log.info("Loaded categories: " + categoryRepository.count().block());
        }
    }
}
