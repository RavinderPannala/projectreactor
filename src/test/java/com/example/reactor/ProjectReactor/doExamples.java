package com.example.reactor.ProjectReactor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
public class doExamples {

    @Test
    public void doNext(){
        Flux<String> stringFlux = Flux.just("Hello", "Ravinder")
                .map(String::toUpperCase).doOnNext(s -> System.out.println("Do nExt Elemetns--->" + s))
                .doOnEach(s -> System.out.println(s));

        StepVerifier.create(stringFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
   public void doOnSubscribe(){
        Flux.just(1,2,3,4)
                .doOnSubscribe(subscription -> System.out.println(subscription.toString()))
                .subscribe(s->System.out.println("Subscribe happens"));
   }

   @Test
    public void donOnNext(){
        Flux.just(1,2,3,4)
                .doOnNext(s->System.out.println("Do On next element--->"+s))
                .subscribe(s->System.out.println("Subscription happen"));
   }

   @Test
   public void doOnComplete(){
      Flux.just(1,2,3,4)
              .doOnNext(s->System.out.println("Do on Next element--->"+s))
              .doOnComplete(()->System.out.println("Process Complete-->"))
              .subscribe();
   }

   @Test
    public void doOnError(){
        Flux.error(new RuntimeException("Exception occurred")).doOnError(s->System.out.println("Do On Error"))
                .subscribe();
   }

   @Test
    public void doOnFinnaly(){
        Flux.just(1)
                .doFinally(s->System.out.println("Do FInnally"+s))
                .subscribe();

        Flux.error(new RuntimeException("Exception"))
                .doFinally(s->System.out.println("Error Result"))
                .subscribe();
   }


}
