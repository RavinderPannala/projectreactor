package com.example.reactor.ProjectReactor;

import com.example.reactor.ProjectReactor.entity.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.util.Random;
import java.util.stream.DoubleStream;

@SpringBootTest
public class MonoSample {


    @Test
    public void zip() {
        Mono<String> mono = Mono.justOrEmpty("Ravi");
        Mono<String> just = Mono.just("Pannala");
        Mono<String> log = Mono.zip(mono, just, (a, b) -> a + "" + b).log();
        StepVerifier.create(log).expectSubscription().expectNext("RaviPannala").expectComplete().verify();
    }

    public void block() {
        String str = Mono.just("Ravi").block();
    }

    @Test
    public void createMono() {
        Mono<String> mono = Mono.just("Ravi");
        mono.subscribe(a -> System.out.println("Mono with Just--->" + a));
        Mono<Object> error = Mono.error(new Throwable());
        error.subscribe(a -> System.out.println("Mono with Error--->" + a));
        Mono<Object> empty = Mono.empty();
        empty.subscribe(a -> System.out.println("Mono with empty--->" + a));
        Mono<Integer> from = Mono.from(Flux.range(1, 10));
        from.subscribe(s -> System.out.println("Mono with from --->" + s));
        Random rd = new Random();
        Mono<Double> doubleMono = Mono.fromSupplier(rd::nextDouble);
        doubleMono.subscribe(s -> System.out.println("From Supplier-->" + s));
        Mono<String> hello = Mono.fromCallable(() -> "Hello");
        hello.subscribe(a -> System.out.println("From Callable --->" + a));
    }

    @Test
    public void map() {
        Mono<Contact> mono = Mono.just(new Contact("1", "Ravi", "Ravi@gmail.com", "3423523"));
        Mono<String> stringMono = mono.map(c -> {
            c.setName(null);
            return c;
        }).map(c -> c.getName()).onErrorReturn("Error Occured");
        stringMono.subscribe(a -> System.out.println(a));
    }

    @Test
    public void zipWith() {
        Mono<String> ravinder = Mono.just("Ravinder");
        Mono<Integer> just = Mono.just(1);
        Mono<String> map = ravinder.zipWith(just).map(t -> {
            return t.getT1() + t.getT2();
        });
        map.subscribe(s->System.out.println("zip With---->"+s));
    }

    @Test
    public void filter(){
        Mono<String> str1 = Mono.just("Ravi");
        Mono<String> str2= Mono.just("Pannala");
        Mono<String> map = str1.zipWith(str2).map(tule2 -> {
            return tule2.getT2() + tule2.getT1();
        });
        Mono<String> stringMono = map.filter(str -> str.contains("Ravi")).switchIfEmpty(Mono.just("Error Occured"));
        stringMono.subscribe(s->System.out.println("filter Mono--->"+s));
    }

    private <T> T identityWithThreadLogging(T el, String operation) {
        System.out.println(operation + " -- " + el + " -- " +
                Thread.currentThread().getName());
        return el;
    }
    @Test
    public void flatMapWithoutChangingScheduler() {
        Flux.range(1,4)
                .map(n->identityWithThreadLogging(n,"map1"))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(f->Mono.just(f).map(n->identityWithThreadLogging(n,"mono")))
                .subscribe(n-> {
                    identityWithThreadLogging(n,"subscribe");
                    System.out.println(n);
                });
    }

    @Test
    public void combiningParallelAndSequentialFlux() {
        Flux.range(1, 4).
                subscribeOn(Schedulers.parallel()).
                map(n -> identityWithThreadLogging(n, "map1"))
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .map(n  -> identityWithThreadLogging(n, "parallelFlux"))
                .sequential()
                .map(n -> identityWithThreadLogging(n, "map2")).
                subscribe(n -> identityWithThreadLogging(n, "subscribe"));
    }

}
