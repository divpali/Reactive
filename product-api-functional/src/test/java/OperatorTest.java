import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class OperatorTest {

    @Test
    public void map() {
        Flux.range(1,5)
                .map(i -> i*10)
                .subscribe(System.out::println);
    }

    @Test
    public void flatMap() {
        Flux.range(1,5)
                .flatMap(i -> Flux.range(i*10,2))
                .subscribe(System.out::println);
    }

    @Test
    public void flatMapMany() {     //converts Mono into a flux
        Mono.just(3)
                .flatMapMany(i -> Flux.range(1, i))
                .subscribe(System.out::println);
    }

    @Test
    public void concat() throws InterruptedException {
        Flux<Integer> oneToFive = Flux.range(1,5)   //thread 1
                .delayElements(Duration.ofMillis(200));

        Flux<Integer> sixToTen = Flux.range(6,5)    //thread 2
                .delayElements(Duration.ofMillis(400));

        Flux.concat(oneToFive, sixToTen)
                .subscribe(System.out::println);

        Thread.sleep(4000);
    }

    @Test
    public void merge() throws InterruptedException {   //merge does not combine the flux in a sequential way

        Flux<Integer> oneToFive = Flux.range(1,5)
                .delayElements(Duration.ofMillis(200));

        Flux<Integer> sixToTen = Flux.range(6,5)
                .delayElements(Duration.ofMillis(400));

        Flux.merge(oneToFive, sixToTen)
                .subscribe(System.out::println);

        Thread.sleep(4000);

    }

    @Test
    public void zip() throws InterruptedException {

        Flux<Integer> oneToFive = Flux.range(1,5)
                .delayElements(Duration.ofMillis(200));

        Flux<Integer> sixToTen = Flux.range(6,5)
                .delayElements(Duration.ofMillis(400));

        Flux.zip(oneToFive, sixToTen,
                (item1, item2) -> item1 + " , "+ item2)
                .subscribe(System.out::println);

        Thread.sleep(4000);

    }


}
