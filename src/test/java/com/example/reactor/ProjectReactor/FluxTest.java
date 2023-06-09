package com.example.reactor.ProjectReactor;

import com.example.reactor.ProjectReactor.entity.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

@SpringBootTest
public class FluxTest {

    private static List<String> words = Arrays.asList(
            "the",
            "quick",
            "brown",
            "fox",
            "jumped",
            "over",
            "the",
            "lazy",
            "dog"
    );

    @Test
    public void concatenateFlux() {
        Flux<Integer> evenFlux = Flux.range(1, 5).filter(x -> x % 2 == 0);
        Flux<Integer> oddFlux = Flux.range(1, 6).filter(x -> x % 2 != 0);
        Flux<Integer> concat = Flux.concat(evenFlux, oddFlux);

        StepVerifier.create(concat.log())
                .expectSubscription()
                .expectNext(2)
                .expectNext(4)
                .expectNext(1)
                .expectNext(3)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    public void concatenateWithFlux() {
        Flux<Integer> evenFlux = Flux.range(1, 5).filter(x -> x % 2 == 0);
        Flux<Integer> oddFlux = Flux.range(1, 6).filter(x -> x % 2 != 0);

        Flux<Integer> integerFlux = evenFlux.concatWith(oddFlux);

        StepVerifier.create(integerFlux.log())
                .expectSubscription()
                .expectNext(2)
                .expectNext(4)
                .expectNext(1)
                .expectNext(3)
                .expectNext(5)
                .verifyComplete();
    }


    @Test
    public void combineLatest() {
        Flux<Integer> evenFlux = Flux.range(1, 5).filter(x -> x % 2 == 0);
        Flux<Integer> oddFlux = Flux.range(1, 6).filter(x -> x % 2 != 0);
        Flux<Integer> combineLatestFlux = Flux.combineLatest(evenFlux, oddFlux, (a, b) -> a + b);

        StepVerifier.create(combineLatestFlux.log())
                .expectSubscription()
                .expectNext(5)
                .expectNext(7)
                .expectNext(9)
                .verifyComplete();
    }

    @Test
    public void merge() {
        Flux<Integer> evenFlux = Flux.range(1, 5).filter(x -> x % 2 == 0);
        Flux<Integer> oddFlux = Flux.range(1, 6).filter(x -> x % 2 != 0);
        Flux<Integer> combineLatestFlux = Flux.merge(evenFlux, oddFlux);

        StepVerifier.create(combineLatestFlux.log())
                .expectSubscription()
                .expectNext(2)
                .expectNext(4)
                .expectNext(1)
                .expectNext(3)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    public void zip() {
        Flux<Integer> evenFlux = Flux.range(1, 5).filter(x -> x % 2 == 0);
        Flux<Integer> oddFlux = Flux.range(1, 6).filter(x -> x % 2 != 0);
        //Flux<Integer> primeFlux = Flux.range(1, 5).filter(x -> x % 3 == 0);
        Flux<Integer> zip = Flux.zip(evenFlux, oddFlux, (a, b) -> a + b);

        StepVerifier.create(zip)
                .expectSubscription()
                .expectNext(3)
                .expectNext(7)
                .verifyComplete();
    }

    @Test
    public void verifyNextAs2() {
        final List<Integer> source = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Flux<Integer> flux = Flux.fromStream(source.stream());
        StepVerifier.create(flux.log())
                .expectNextSequence(source)
                .expectComplete()
                .verify();
    }

    @Test
    public void sampleCreation(){
        Flux<String> just = Flux.just("Ravi", "Rajesh");
        Flux<String> stringFlux = Flux.fromIterable(words);

        just.subscribe(System.out::println);
        stringFlux.subscribe(System.out::println);
    }

    @Test
    public void findingMissingLetters(){
        Flux<String> sort = Flux.fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .distinct()
                .sort()
                .zipWith(Flux.range(1,Integer.MAX_VALUE),(string,count)->String.format("%2d,%s",count,string));
        sort.subscribe(System.out::println);
    }

    @Test
    public void fluxToList(){
        Flux<String> just = Flux.just("Ravi", "Pannala", "Reddy");
        List<String> wordsList = new ArrayList<>();
        just.collectList().subscribe(wordsList::addAll);
        wordsList.forEach(System.out::println);

        just.collectMap(
                item -> item.split(":")[0],
                item -> item.split(":")[1]);
    }

    @Test
    public void startWith(){
        Flux<String> just = Flux.just("Ravi", "Reddy");
        Flux<String> pannala = just.startWith("Pannala");
        Flux<String> ayaan = pannala.startWith(Arrays.asList("Ayaan"));
        Flux<String> hello = ayaan.startWith(Mono.just("Hello"));
        hello.subscribe(s->System.out.println(s));

    }

    @Test
    public void concatenateWithValues(){
        Flux<String> stringFlux = Flux.just("avi", "annala").concatWithValues("Reddy", "Pannala");
        stringFlux.subscribe(s->System.out.println("ConcatenateWith Values-->"+s));
    }

    @Test
    public void groupBy(){
        Flux<Contact> contactFlux = Flux.fromIterable(fetchContact());
        Flux<GroupedFlux<String, Contact>> groupedFluxFlux = contactFlux.groupBy(Contact::getName);
        Mono<Map<String, Collection<Contact>>> mapMono = contactFlux.collectMultimap(Contact::getEmail, item -> item);
        Mono<Map<String, String>> mapMono1 = contactFlux.collectMap(Contact::getName, Contact::getEmail);
        Mono<List<Contact>> listMono = contactFlux.collectSortedList(Comparator.comparing(Contact::getName));
        mapMono.subscribe(System.out::println);
    }
    public List<Contact> fetchContact(){
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("1","Ravi","ravi@gmail.com","344343"));
        contacts.add(new Contact("2","Raju","ravi@gmail.com","332"));
        return contacts;
    }

}
