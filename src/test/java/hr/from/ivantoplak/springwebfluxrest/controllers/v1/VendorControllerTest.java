package hr.from.ivantoplak.springwebfluxrest.controllers.v1;

import hr.from.ivantoplak.springwebfluxrest.domain.Vendor;
import hr.from.ivantoplak.springwebfluxrest.repositories.VendorRepository;
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

public class VendorControllerTest {

    private static final String FIRST_NAME_1 = "John";
    private static final String LAST_NAME_1 = "Snow";
    private static final String FIRST_NAME_2 = "Jim";
    private static final String LAST_NAME_2 = "Doe";
    private static final String ID = "Id";

    private WebTestClient webTestClient;

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorController vendorController;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void list() {

        //given
        given(vendorRepository.findAll())
                .willReturn(Flux.just(
                        Vendor.builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1).build(),
                        Vendor.builder().firstName(FIRST_NAME_2).lastName(LAST_NAME_2).build()));

        //when
        webTestClient.get()
                .uri(VendorController.BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class) //then
                .hasSize(2);
    }

    @Test
    public void getById() {

        //given
        given(vendorRepository.findById(ID))
                .willReturn(Mono.just(Vendor.builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1).build()));

        //when
        webTestClient.get()
                .uri(VendorController.BASE_URL + "/" + ID)
                .exchange()
                .expectBody(Vendor.class); //then
    }

    @Test
    public void create() {

        //given
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1).build()));

        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1).build());

        //when
        webTestClient.post()
                .uri(VendorController.BASE_URL)
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus() //then
                .isCreated();
    }

    @Test
    public void update() {

        //given
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1).build()));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1).build());

        //when
        webTestClient.put()
                .uri(VendorController.BASE_URL + "/" + ID)
                .body(vendorToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus() //then
                .isOk();
    }

    @Test
    public void patch() {

        //given
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName(FIRST_NAME_1).lastName(LAST_NAME_1).build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName(FIRST_NAME_2).lastName(LAST_NAME_2).build()));

        Mono<Vendor> categoryToUpdateMono = Mono.just(Vendor.builder().firstName(FIRST_NAME_2).lastName(LAST_NAME_2).build());

        //when
        webTestClient.patch()
                .uri(VendorController.BASE_URL + "/" + ID)
                .body(categoryToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus() //then
                .isOk();

        then(vendorRepository).should().save(any());
    }

    @Test
    public void delete() {

        //given
        given(vendorRepository.deleteById(ID)).willReturn(Mono.empty().then());

        //when
        webTestClient.delete()
                .uri(VendorController.BASE_URL + "/" + ID)
                .exchange()
                .expectStatus().isOk(); //then
    }
}