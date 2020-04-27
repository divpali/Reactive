package handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

import repository.ProductRepository;
import model.Product;

import model.ProductEvent;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ProductHandler {

    private ProductRepository repository;

    public ProductHandler(ProductRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> getAllProducts(ServerRequest request) {
        Flux<Product> products = repository.findAll();

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(products, Product.class);
    }

    public Mono<ServerResponse> getProduct(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<Product> productMono = this.repository.findById(id);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return productMono
                .flatMap(product ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(BodyInserters.fromObject(product)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> saveProduct(ServerRequest request) {
        Mono<Product> productMono = request.bodyToMono(Product.class);

        return productMono.flatMap(product ->
                ServerResponse.status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .body(repository.save(product), Product.class));
    }

    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Product> existingProductMono = this.repository.findById(id);
        Mono<Product> productMono = request.bodyToMono(Product.class);

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return productMono.zipWith(existingProductMono,
                (product, existingProduct) ->
                        new Product(existingProduct.getId(), product.getName(), product.getPrice())
        )
                .flatMap(product ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(repository.save(product), Product.class)
                ).switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<Product> productMono = this.repository.findById(id);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        return productMono
                .flatMap(existingProduct ->
                        ServerResponse.ok()
                                .build(repository.delete(existingProduct))
                )
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteAllProducts(ServerRequest request) {
        return ServerResponse.ok()
                .build(repository.deleteAll());
    }

    public Mono<ServerResponse> getProductEvents(ServerRequest request) {
        Flux<ProductEvent> eventsFlux = Flux.interval(Duration.ofSeconds(1)).map(val ->
                new ProductEvent(val, "Product Event")
        );

        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(eventsFlux, ProductEvent.class);
    }

}
