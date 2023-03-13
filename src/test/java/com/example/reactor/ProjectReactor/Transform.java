package com.example.reactor.ProjectReactor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@SpringBootTest
public class Transform {

    AtomicInteger ai = new AtomicInteger();
    @Test
    public void trnasform(){

        Flux.fromIterable(Arrays.asList("blue","green","orange","purple"))
                .doOnNext(System.out::println)
                .transform(function)
                .subscribe(s->System.out.println("Values are transformed to--->"+s));

    }

    @Test
    public void trnasformDefered(){

        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println)
                .transformDeferred(filterAndMap);
        stringFlux.subscribe(s->System.out.println("Values are transformed to--->"+s));
        stringFlux.subscribe(s->System.out.println("Values are transformed to--->"+s));
    }

    public Function<Flux<String>, Flux<String>> function = f -> f.filter(color -> !color.equals("orange"))
            .map(s -> s.toUpperCase());


    Function<Flux<String>, Flux<String>> filterAndMap = f -> {
        if (ai.incrementAndGet() == 1) {
            return f.filter(color -> !color.equals("orange"))
                    .map(String::toUpperCase);
        }
        return f.filter(color -> !color.equals("purple"))
                .map(String::toUpperCase);
    };
}
