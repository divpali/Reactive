import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class MonoTest {

    @Test
    public void firstMono() {
        Mono.just("A")  //Mono is the publisher
            .log()
            .subscribe();
    }

    @Test
    public void monoWithConsumer() {
        Mono.just("A")
                .log()
                .subscribe(System.out::println);
    }

    @Test
    public void monoWithDoOn() {
        Mono.just("A")
                .log()
                .doOnSubscribe(sub -> System.out.println("subscribe : "+sub))
                .doOnRequest(res -> System.out.println("request : " + res))
                .doOnSuccess(suc -> System.out.println("success : "+ suc))
                .subscribe(System.out::println);
    }

    @Test
    public void emptyMono() {
        Mono.empty()  //empty Mono is the publisher that can be used for void return
                .log()
                .subscribe(System.out::println);
    }

    @Test
    public void emptyMonoWithConsumer() {
        Mono.empty()
                .log()
                .subscribe(System.out::println,
                        null,
                        () -> System.out.println("Done"));
    }

    @Test
    public void monoWithError() {
        Mono.error(RuntimeException::new)
                .log()
                .subscribe();
    }

    @Test
    public void errorExceptionMono() {
        Mono.error(Exception::new)
                .log()
                .subscribe();
    }

    @Test
    public void errorConsumerMono() {
        Mono.error(Exception::new)
                .log()
                .subscribe(System.out::println,
                        e -> System.out.println("Error : "+ e));
    }

    @Test
    public void doOnErrorMono() {
        Mono.error(Exception::new)
                .doOnError(e -> System.out.println("Error : "+ e))
                .log()
                .subscribe();
    }

    @Test
    public void resumeOnErrorMono() {
        Mono.error(Exception::new)
                .onErrorResume(e -> {
                    System.out.println("Error caught: "+ e);
                    return Mono.just("B");
                })
                .log()
                .subscribe();
    }

    @Test
    public void returnOnErrorMono() {
        Mono.error(Exception::new)
                .onErrorReturn("B")
                .log()
                .subscribe();
    }
}
