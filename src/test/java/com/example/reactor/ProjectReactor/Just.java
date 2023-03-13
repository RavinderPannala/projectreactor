package com.example.reactor.ProjectReactor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@SpringBootTest
public class Just {

    @Test
    public void just() {
        Mono<String> ravi = Mono.just("Ravi");
        Flux<String> ravi1 = Flux.just("Ravi");
        Flux<String> just = Flux.just("Ravi", "Ravi");

        Mono.just(asyncMethod())
                .doOnNext(System.out::println)
                .log();
    }

    /*
    An Optional is a container object which may or may not contain a non-null value.
    The Mono#justOrEmpty flattens the Optional and creates a new Mono that emits the value of Optional
    if non-null and invokes onNext method.
    If Optional value is null, then skips the onNext and calls the onComplete directly.
     */
    @Test
    public void justOrEmpty() {

        Mono<Integer> integerMono = Mono.justOrEmpty(asyncMethod())
                .doOnNext(System.out::println)
                .log();
        integerMono.subscribe(s -> System.out.println(s));
    }

    /*
    A value can be a null or non-null. The Mono#justOrEmpty(T data) creates a new Mono that emits the value if non-null
    and invokes onNext method. If the value is null,
    then skips the onNext and calls onComplete directly.
     */
    @Test
    public void justOrEmptyOptional() {

        Mono<Integer> integerMono = Mono.justOrEmpty(asyncOptional())
                .doOnNext(System.out::println)
                .log();
        integerMono.subscribe(s -> System.out.println(s));
    }

    public Integer asyncMethod() {
        return null;
    }

    public Optional<Integer> asyncOptional() {
        return Optional.of(10);
    }
}
