package hr.from.ivantoplak.springwebfluxrest.controllers.v1;

import hr.from.ivantoplak.springwebfluxrest.domain.Category;
import hr.from.ivantoplak.springwebfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CategoryController.BASE_URL)
public class CategoryController {

    public static final String BASE_URL = "/api/v1/categories";

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Category> list() {

        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Category> getById(@PathVariable String id) {

        return categoryRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody Publisher<Category> categoryStream) {

        return categoryRepository.saveAll(categoryStream).then();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Category> update(@PathVariable String id, @RequestBody Category category) {

        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {

        return categoryRepository.findById(id)
                .flatMap(foundCategory -> {

                    if (category.getDescription() != null) {
                        foundCategory.setDescription(category.getDescription());
                    }

                    return categoryRepository.save(foundCategory);
                });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> delete(@PathVariable String id) {

        return categoryRepository.deleteById(id).then();
    }
}
