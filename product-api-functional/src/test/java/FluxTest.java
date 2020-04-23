import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.sql.SQLOutput;
import java.time.Duration;
import java.util.Arrays;

public class FluxTest {

    @Test
    public void firstFlux() {
        Flux.just("A","B","C")
                .log()
                .subscribe();
    }

    @Test
    public void iterableFlux() {
        Flux.just(Arrays.asList("A", "B", "C"))
                .log()
                .subscribe();           //onNext([A, B, C])
        //Can't use "just" to iterate over each element in the list
        //have to use iterate option
        System.out.println();
        Flux.fromIterable(Arrays.asList("A","B","C"))
                .log()
                .subscribe();           //onNext(A), onNext(B), onNext(C)

    }

    @Test
    public void fluxFromRange() {
        Flux.range(10,5)
                .log()
                .subscribe();
    }

    @Test
    public void fluxFromInterval() {
        Flux.interval(Duration.ofSeconds(1))        //nothing is rendered in onNext
                .log()                              //since this thread is running independent from main thread of this method
                .subscribe();                       //hence once the method is executed all threads stop execution

    }

    @Test
    public void fluxFromIntervalThread() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))        //use thread sleep in the end of the method and then check
                .log()
                .subscribe();
        Thread.sleep(6000);
    }

    @Test
    public void fluxFromIntervalThreadTakestwoValues() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))        //this take mathod takes 2 values and cancels the subscription
                .log()                              //in order to work with back pressure we need to use request method
                .take(2)                            //under subscription object
                .subscribe();
        Thread.sleep(6000);
    }

    @Test
    public void fluxRequest() {
        Flux.range(1, 5)
                .log()
                .subscribe(null,
                        null,
                        null,
                        s -> s.request(3)        //to work with back pressure we need to use request method under subscription object
                        );                           //here onComplete is not called
    }

    @Test
    public void fluxLimitRate() {
        Flux.range(1, 5)
                .log()
                .limitRate(3)
                .subscribe();                       //here onComplete is called
    }

}
