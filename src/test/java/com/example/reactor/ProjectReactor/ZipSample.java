package com.example.reactor.ProjectReactor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple3;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class ZipSample {

    @Test
    public void zipSingleElementinSource() {
        Flux<String> f1 = Flux.just("F1");
        Flux<String> f2 = Flux.just("F2");
        Flux<String> f3 = Flux.just("F3");
        Flux<Tuple3<String, String, String>> zip = Flux.zip(f1, f2, f3);

        StepVerifier.create(zip).expectSubscription().expectNextCount(1).verifyComplete();
    }

    @Test
    public void zipWithMultipleValueSource() {
        Flux<String> f1 = Flux.just("F11", "F12");
        Flux<String> f2 = Flux.fromArray(new String[]{"F21", "F22"});
        Flux<String> f3 = Flux.fromStream(Stream.of("F31", "F32"));
        Flux<String> map = Flux.zip(f1, f2, f3).map(s -> {
            String s1 = s.getT1() + " " + s.getT2() + " " + s.getT3();
            return s1;
        });
        StepVerifier.create(map).expectSubscription().expectNext("F11 F21 F31").expectNext("F12 F22 F32").verifyComplete();
    }

    @Test
    public void zipWithEmptySource() {
        Flux<Object> empty = Flux.empty();
        Flux<String> fluxString = Flux.just("F11", "F21");
        Flux<String> from = Flux.from(Flux.just("F22", "F23"));
        Mono<Void> then = Flux.zip(empty, fluxString, from).map(s -> {
            return Mono.empty();
        }).then();

        StepVerifier.create(then).expectSubscription().expectNextCount(0).verifyComplete();
    }

    @Test
    public void prematureCompleteEmptySource() {
        StepVerifier.create(Flux.zip(obj -> 0, Flux.just(1), Mono.empty())).expectSubscription().expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void createZipWithPrefetchIterable() {
        List<Flux<Integer>> list = Arrays.asList(Flux.just(1), Flux.just(2));
        Flux<Integer> f = Flux.zip(list, 123, obj -> 0);
         f.subscribe(s->System.out.println(s));
        //StepVerifierEx.create(f).expectSubscription().expectNext()
    }

    @Test
    public void failNull() {
        StepVerifier.create(Flux.zip(obj -> 0, Flux.just(1), null))
                .verifyError(NullPointerException.class);
    }
}
