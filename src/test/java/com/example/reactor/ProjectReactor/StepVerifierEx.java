package com.example.reactor.ProjectReactor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;

@SpringBootTest
public class StepVerifierEx {

    @Test
    public void create() {

        StepVerifier.create(Flux.just(1, 2, 3))
                .expectNext(1).expectNext(2).expectNext(3).expectComplete().verify();
    }

    @Test
    public void createWithDuration() {

        //  StepVerifier.create(Flux.just(1,2,3)).expectNextCount(3).expectComplete().verify();
        StepVerifier.create(Flux.just(1)).expectNextSequence(Arrays.asList(1)).expectComplete().verify();
    }

    @Test
    public void expectNextMatches() {
        Flux<String> map = Flux.just("ravi", "ramu", "Ra", "Zoo").filter(s->s.length()>2).map(String::toUpperCase);
        StepVerifier.create(map).
                expectNext("RAVI").
                expectNextMatches(s -> s.startsWith("RA")).
                expectComplete().
                verify();
    }

    @Test
    public void expectError(){
        Flux<Integer> integerFlux = Flux.just(1, 2, 3).concatWith(Flux.error(new ArithmeticException("Error Occured")));
        //StepVerifier.create(integerFlux).expectNext(1).expectNext(2).expectNext(3).expectError().verify();
        StepVerifier.create(integerFlux).expectNextCount(3).expectError(ArithmeticException.class).verify();
        StepVerifier
                .create(integerFlux).
                expectNext(1).
                expectNext(2).
                expectNext(3).
                expectErrorMatches(error->error.getMessage().matches("Error Occured")).
                verify();
    }
}
