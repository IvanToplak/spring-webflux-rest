package hr.from.ivantoplak.springwebfluxrest.controllers.v1;

import hr.from.ivantoplak.springwebfluxrest.domain.Category;
import hr.from.ivantoplak.springwebfluxrest.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class CategoryControllerTest {

    private static final String DESCRIPTION_1 = "Cat1";
    private static final String DESCRIPTION_2 = "Cat2";
    private static final String ID = "Id";

    private WebTestClient webTestClient;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryController categoryController;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void list() {

        //given
        given(categoryRepository.findAll())
                .willReturn(Flux.just(
                        Category.builder().description(DESCRIPTION_1).build(),
                        Category.builder().description(DESCRIPTION_2).build()));

        //when
        webTestClient.get()
                .uri(CategoryController.BASE_URL)
                .exchange()
                .expectBodyList(Category.class) //then
                .hasSize(2);
    }

    @Test
    public void getById() {

        //given
        given(categoryRepository.findById(ID))
                .willReturn(Mono.just(Category.builder().description(DESCRIPTION_1).build()));

        //when
        webTestClient.get()
                .uri(CategoryController.BASE_URL + "/" + ID)
                .exchange()
                .expectBody(Category.class); //then
    }

    @Test
    public void create() {

        //given
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().description(DESCRIPTION_1).build()));

        Mono<Category> categoryToSaveMono = Mono.just(Category.builder().description(DESCRIPTION_1).build());

        //when
        webTestClient.post()
                .uri(CategoryController.BASE_URL)
                .body(categoryToSaveMono, Category.class)
                .exchange()
                .expectStatus() //then
                .isCreated();
    }

    @Test
    public void update() {

        //given
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().description(DESCRIPTION_1).build()));

        Mono<Category> categoryToUpdateMono = Mono.just(Category.builder().description(DESCRIPTION_1).build());

        //when
        webTestClient.put()
                .uri(CategoryController.BASE_URL + "/" + ID)
                .body(categoryToUpdateMono, Category.class)
                .exchange()
                .expectStatus() //then
                .isOk();
    }

    @Test
    public void patch() {

        //given
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description(DESCRIPTION_1).build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().description(DESCRIPTION_2).build()));

        Mono<Category> categoryToUpdateMono = Mono.just(Category.builder().description(DESCRIPTION_2).build());

        //when
        webTestClient.patch()
                .uri(CategoryController.BASE_URL + "/" + ID)
                .body(categoryToUpdateMono, Category.class)
                .exchange()
                .expectStatus() //then
                .isOk();

        then(categoryRepository).should().save(any());
    }

    @Test
    public void delete() {

        //given
        given(categoryRepository.deleteById(ID)).willReturn(Mono.empty().then());

        //when
        webTestClient.delete()
                .uri(CategoryController.BASE_URL + "/" + ID)
                .exchange()
                .expectStatus().isOk(); //then
    }
}