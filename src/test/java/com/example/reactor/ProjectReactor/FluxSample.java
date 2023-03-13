package com.example.reactor.ProjectReactor;

import com.example.reactor.ProjectReactor.entity.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.Disposable;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
public class FluxSample {


    @Test
    public void createFlux() {

        //Creating empty Flux
        Flux<Object> empty = Flux.empty();
        empty.subscribe(s -> System.out.println("Empty Flux from -->" + s));

        //Creat Flux from Error
        Flux<Object> error = Flux.error(new NullPointerException("Error Occured"));
        error.subscribe(e -> System.out.println("Error Flux" + e));

        //Creat flux with Just
        Flux<String> just = Flux.just("Ravi", "Ramu");
        just.subscribe(v -> System.out.println(v));

        //Create flux with fromItrerable
        List<String> stringList = Arrays.asList("Apple", "Ball", "Cat");
        Flux<String> stringFlux = Flux.fromIterable(stringList);
        stringFlux.subscribe(c -> System.out.println(c));

        //Create Flux from Array
        Flux<Integer> integerFlux = Flux.fromArray(new Integer[]{1, 2, 3, 4, 5});
        integerFlux.subscribe(i -> System.out.println(i));

        //Create Flux from Stream
        Stream<Integer> integerStream = Stream.of(10, 11, 12, 13, 145);
        Flux<Integer> integerFlux1 = Flux.fromStream(integerStream);
        integerFlux1.subscribe(i -> System.out.println(i));

        //Create Flux from another publisher
        Flux<String> just1 = Flux.from(Mono.just("just"));
        just1.subscribe(i -> System.out.println(i));

        //Create Flux from range
        Flux<Integer> range = Flux.range(1, 5);
        range.subscribe(r -> System.out.println(r));


        StepVerifier.create(just.log()).expectNext("Ravi").expectNext("Ramu").expectComplete();

        Flux.empty().log();
        Flux.never().log();

    }

    @Test
    public void subscribe() {

        Flux<Integer> range = Flux.range(1, 4);
        range.subscribe();
        range.subscribe(i -> System.out.println(i));
        range.map(i -> {
            if (i <= 3)  return i;
            throw new RuntimeException("Value got 4");
        });
        Disposable done = range.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Done"));
        done.dispose();

        SampleSubscriber<Integer> sampleSubscriber = new SampleSubscriber<Integer>();
        Flux<Integer> range1 = Flux.range(1, 4);
        range1.subscribe(sampleSubscriber);

    }

    @Test
    public void generateFlux() {

        Flux<Object> generate = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 X " + state + " = " + 3 * state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });

        generate.subscribe(s -> System.out.println(s));

        Flux<Object> generate1 = Flux.generate(
                AtomicInteger::new,
                (state, sink) -> {
                    int i = state.getAndIncrement();
                    sink.next("3 x " + state + " = " + 3 * i);
                    if (i == 10) sink.complete();
                    return state;
                });
        generate1.subscribe(s->System.out.println(s));
    }

    @Test
    public void errorFlux() {
        Flux<Object> error = Flux.error(new RuntimeException());
        Flux<Integer> integerFlux = Flux.just(1, 4, 0).map(i -> (100 / i)).onErrorReturn(0);
        integerFlux.subscribe(s->System.out.println(s));
        StepVerifier.create(error.log()).expectError(RuntimeException.class).verify();
    }

    @Test
    public void handleError() {
        Flux<String> stringFlux = Flux.just("Hello", "World")
                .concatWith(Mono.just("Ravi"))
                .concatWith(Mono.error(new RuntimeException("Exception Occured")))
                .concatWith(Mono.just("Pannala"));
        StepVerifier.create(stringFlux).expectSubscription().expectNext("Hello").expectNext("World")
                .expectNext("Ravi").expectError(RuntimeException.class).verify();
    }

    @Test
    public void filter() {
        Flux<Integer> filter = Flux.range(1, 10).filter(i -> i % 2 == 0);
        Flux<Integer> filter1 = Flux.range(1, 10).filter(i -> i % 2 != 0);
        StepVerifier.create(filter.log()).expectSubscription()
                .expectNext(2).expectNext(4)
                .expectNext(6).expectNext(8)
                .verifyComplete();
    }

    @Test
    public void flatMap() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(new Contact("st1", "Ravi", "Ravi@gmail.com", "8777565"));
        // contactList.add(new Contact("st2", "avi", "avi@gmail.com", "777565"));

        Flux<Contact> cFlux = Flux.fromIterable(contactList).flatMap(c -> {
            return asynchrnousContact(c);
        }).log();

        StepVerifier.create(cFlux).expectSubscription()
                .expectNext(new Contact("ST1", "RAVI", "RAVI@GMAIL.COM", "8777565"))
                .verifyComplete();
    }

    private Mono<Contact> asynchrnousContact(Contact c) {
        Contact contact = new Contact(c.getId().toUpperCase(), c.getName().toUpperCase(), c.getEmail().toUpperCase(), c.getPhone());
        return Mono.just(contact);
    }

    @Test
    private void map() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(new Contact("st1", "Ravi", "Ravi@gmail.com", "8777565"));
        Flux<Contact> mapF = Flux.fromIterable(contactList)
                .map(c -> new Contact(c.getId().toUpperCase(), c.getName().toUpperCase(), c.getEmail().toUpperCase(), c.getPhone()))
                .log();
        StepVerifier.create(mapF).expectSubscription()
                .expectNext(new Contact("ST1", "RAVI", "RAVI@GMAIL.COM", "8777565"))
                .verifyComplete();

    }

    @Test
    public void parallel() {
        ParallelFlux<Integer> log = Flux.range(1, 10)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(i -> i + 1)
                .map(i -> i * 2)
                .map(i -> i + 1)
                .log();
        StepVerifier.create(log).expectSubscription().expectNextCount(9).expectComplete();

    }

    @Test
    public void multipleSubscribes() throws InterruptedException {
        System.out.println("Starts");
        Flux<String> just = Flux.just("a", "b", "c").log().delayElements(Duration.ofSeconds(1));
        Disposable subscribe = just.map(i -> i.toUpperCase()).subscribe(a -> System.out.println("Observer 1:---" + a));
        just.subscribe(a -> System.out.println("ObServer 2:--" + a));
        System.out.println("Ends");
        Thread.sleep(10000);


    }

    @Test
    public void create() {
        Flux<Integer> integerFlux = Flux.create((FluxSink<Integer> fluxSink) -> {
            IntStream.range(0, 5).peek(i -> System.out.println("Going to Emit" + i)).forEach(fluxSink::next);
        }, FluxSink.OverflowStrategy.DROP).log();

        //First observer. takes 1 ms to process each element
        integerFlux.delayElements(Duration.ofMillis(1)).subscribe(i -> System.out.println("First :: " + i));

        //Second observer. takes 2 ms to process each element
        integerFlux.delayElements(Duration.ofMillis(2)).subscribe(i -> System.out.println("Second:: " + i));
    }

    @Test
    public void generate() {
        AtomicInteger atomicInteger = new AtomicInteger();
        Flux<Integer> generate = Flux.generate((SynchronousSink<Integer> synSink) -> {
            System.out.println("Flux generate");
            synSink.next(atomicInteger.getAndIncrement());
        });
        generate.delayElements(Duration.ofMillis(50))
                .subscribe(i -> System.out.println("First consumed ::" + i));
    }

    @Test
    public void schedularImmediate() {
        /*Flux<Integer> log = Flux.range(0, 5).publishOn(Schedulers.immediate()).map(i -> {
            System.out.println("Mapping for " + i + " is done by thread " + Thread.currentThread().getName());
            return i;
        }).log();

        log.subscribe(i->System.out.println(i));*/

        Flux<Integer> log1 = Flux.range(0, 2).publishOn(Schedulers.parallel()).map(i -> {
            System.out.println("Mapping for " + i + " is done by thread " + Thread.currentThread().getName());
            return i;
        }).log();

        log1.subscribeOn(Schedulers.parallel()).subscribe(i -> System.out.println("Paraller Subscription  " + Thread.currentThread().getName()));
    }

    @Test
    public void combine() {
        Flux<Integer> log = Flux.just(1, 3, 4, 5).log();
        Flux<Character> log1 = Flux.just('a', 'b', 'c', 'd').log();
        Flux<? extends Serializable> concat = Flux.concat(log, log1);
        Flux<String> stringFlux = Flux.combineLatest(log, log1, (a, b) -> a + " " + b);
        stringFlux.subscribe(i -> System.out.println(i));
    }

    @Test
    public void zip() {
        Flux<Integer> just = Flux.just(1, 2, 3, 4);
        Flux<Integer> just1 = Flux.just(9, 8, 7, 6);
        Flux<Tuple2<Integer, Integer>> zip = Flux.zip(just, just1);
        zip.subscribe(tuple -> {
            System.out.println("First Source " + tuple.getT1() + " Second Source " + tuple.getT2());
        });
    }

    /*@Test
    public void fileReading() throws IOException {
        Path ipPath = Paths.get("C:\\Users\\rpannala\\Downloads\\RAVINDER_REDDY_PANNALA.docx");

        Flux<String> stringFlux = Flux.using(
                () -> Files.lines(ipPath),
                Flux::fromStream,
                Stream::close
        );

        Path opPath = Paths.get("C:\\Users\\rpannala\\Downloads\\large-output-file.txt");
        BufferedWriter bw = Files.newBufferedWriter(opPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        stringFlux
                .subscribe(s -> write(bw, s),
                        (e) -> close(bw),  // close file if error / oncomplete
                        () -> close(bw)
                );
    }

    private void close(Closeable closeable){
        try {
            closeable.close();
            System.out.println("Closed the resource");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void write(BufferedWriter bw, String string){
        try {
            bw.write(string);
            bw.newLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }*/
}
