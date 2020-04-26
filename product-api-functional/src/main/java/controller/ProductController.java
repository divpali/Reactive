package controller;

import com.springwebflux.productapifunctional.ProductRepository;
import model.Product;
import model.ProductEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //In case we use "id" that returns nothing it can still return an empty Mono
    //but it would be better to provide a HTTP response like "Not found", hence
    //we use ResponseEntity
    @GetMapping("{id}")
    public Mono<ResponseEntity<Product>> getProduct(@PathVariable String id) {
        return productRepository.findById(Long.valueOf(id))
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> saveProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping({"id"})
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable(value = "id") String id,
                                                       @RequestBody Product product) {
        return productRepository.findById(Long.valueOf(id))
                .flatMap(existingProduct -> {
                    existingProduct.setProductName(product.getProductName());
                    existingProduct.setPrice(product.getPrice());
                    return productRepository.save(existingProduct);
                })
                .map(updateProduct -> ResponseEntity.ok(updateProduct))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /*
        productRepository.delete return Mono<Void>, hence a map operator is not the right operator to use.

     */
    @DeleteMapping({"id"})
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable(value = "id") String id) {
        return productRepository.findById(Long.valueOf(id))
                .flatMap(existingProduct ->
                        productRepository.delete(existingProduct)
                .then(Mono.just(ResponseEntity.ok().<Void>build())));


    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductEvent> getProductEvent() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(val ->
                        new ProductEvent(val, "Product Event"));
    }
}
