package com.springwebflux.productapifunctional;

import model.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import repository.ProductRepository;

/*
Issue : No bean found for ProductRepository
Solution :
 */

@SpringBootApplication
public class ProductApiFunctionalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiFunctionalApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ProductRepository repository) {
		return args -> {
			Flux<Product> productFlux = Flux.just(
					new Product(null, "Big Latte", 2.99),
					new Product(null, "Big Decaf", 2.49),
					new Product(null, "Green Tea", 1.99))
					.flatMap(repository::save);

			/*
			then -> takes either no paramters or a mono
			thenMany -> takes any publisher, flux or mono
			 */
			productFlux
					.thenMany(repository.findAll())
					.subscribe(System.out::println);

			/*
			We are working with an embeded database which is not persistent between executions
			we don't need to delet the existing document or collection

			In case you need to work on a real mongo db database, you can inject "ReactiveMongoOperations"
			as a parameter of init method.
			 */

            /*operations.collectionExists(Product.class)
                    .flatMap(exists -> exists ? operations.dropCollection(Product.class) : Mono.just(exists))
					.thenMany(v -> operations.createCollection(Product.class))
					.thenMany(productFlux)
					.thenMany(repository.findAll())
					.subscribe(System.out::println);*/
		};
	}
}
