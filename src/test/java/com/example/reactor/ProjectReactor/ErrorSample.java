package com.example.reactor.ProjectReactor;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sun.reflect.annotation.ExceptionProxy;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootTest
public class ErrorSample {

    Logger log = LoggerFactory.getLogger(ErrorSample.class);

    @Test
    public void onErrorReturnDirectly_Mono() {
        Mono.
                just(2).
                map(i -> i / 0). //Arthimetic Exception
                onErrorReturn(0).
                subscribe(num -> System.out.println("Number after exception--->" + num));
    }

    /*
    type – the error type to match
    fallbackValue – the value to emit if an error occurs that matches the type
     */
    @Test
    public void onErrorReturnExceptionTypeMatched_Mono() {
        Mono.just(2)
                .map(i -> i / 0)
                .onErrorReturn(ArithmeticException.class, 0)
                .subscribe(num -> System.out.println("ExceptionTypeMatched--->" + num));
    }

    /*
        predicate – the error predicate to match
        fallbackValue – the value to emit if an error occurs that matches the predicate
     */

    @Test
    public void onErrorReturnPredicateValueMatched_Mono() {

        Mono.just(2)
                .map(i -> i / 0)
                .onErrorReturn(error -> error instanceof ArithmeticException, 0)
                .subscribe(num -> System.out.println("Predicate Value matched--->" + num));
    }

    /*
    Params: fallback – the function to choose the fallback to an alternative Mono
    Returns: a Mono falling back upon source onError
     */
    @Test
    public void onErrorResumeDefault_Mono() {
        //Function<Throwable, Mono<Integer>> function = error -> Mono.just(4);
        Function<Throwable, Mono<Integer>> function = error -> Mono.error(new RuntimeException("Exceptions occurred"));
        Mono.just(2)
                .map(i -> i / 0)
                .onErrorResume(function)
                .subscribe(num -> System.out.println("Error Occured pass another function" + num));
    }

    /*
    Params:type – the error type to match fallback – the function to choose the fallback to an alternative Mono
    Returns:a Mono falling back upon source onError
     */
    @Test
    public void onErrorResumeExceptionTypeMatched_Mono() {
        Function<Throwable, Mono<Integer>> function = error -> Mono.just(4);
        Mono
                .just(2)
                .map(i -> i / 0)
                .onErrorResume(ArithmeticException.class, function)
                .subscribe(num -> System.out.println("Exception type matched--->" + num));
    }

    /*
    Params:predicate – the error predicate to match fallback – the function to choose the fallback to an alternative Mono
    Returns:a Mono falling back upon source onError
     */

    @Test
    public void onErrorResumePredicateMatched_Mono() {
        Function<Throwable, Mono<Integer>> function = error -> Mono.just(4);
        Mono.just(2)
                .map(i -> i / 0)
                .onErrorResume(error -> error instanceof ArithmeticException, function)
                .subscribe(num -> System.out.println("Predicate Matched--->" + num));

    }

    /*
     param errorConsumer a {@link BiConsumer} fed with errors matching the {@link Class} and the value that triggered the error.
	 return a {@link Mono} that attempts to continue processing on errors
     */
    @Test
    public void onErrorContinueDefault_Mono() {
        BiConsumer<Throwable, Object> biConsumer = (e, i) -> {
            System.out.println("Error-->" + e + "----> Value" + i);
        };
        Mono.just(2).map(i -> i / 0).onErrorContinue(biConsumer).subscribe(num -> System.out.println("onErrorContinue--->" + num));

    }

    /*
    Params:type – the Class of Exception that are resumed from. errorConsumer – a BiConsumer fed with errors matching the Class and the value that triggered the error.
    Returns:a Mono that attempts to continue processing on some errors.
     */
    @Test
    public void onErrorContinueExceptionClassMatched_Mono() {
        BiConsumer<Throwable, Object> biConsumer = (e, i) -> {
            System.out.println("Error-->" + e + "----> Value" + i);
        };
        Mono.just(2)
                .map(i -> i / 0)
                .onErrorContinue(ArithmeticException.class, biConsumer)
                .subscribe(num -> System.out.println(num));
    }

    /*
    Params:errorPredicate – a Predicate used to filter which errors should be resumed from. This MUST be idempotent, as it can be used several times. errorConsumer – a BiConsumer fed with errors matching the predicate and the value that triggered the error.
    Returns:a Mono that attempts to continue processing on some errors.
     */
    @Test
    public void onErrorContinuePredicateMatched_Mono() {
        BiConsumer<Throwable, Object> biConsumer = (e, i) -> {
            System.out.println("Error-->" + e + " Value--->" + i);
        };
        Mono.just(2)
                .map(i -> i / 0)
                .onErrorContinue(error -> error instanceof ArithmeticException, biConsumer)
                .subscribe(s -> System.out.println(s));
    }

    /*
    param onError the error callback to call on {@link Subscriber#onError(Throwable)}

	  return a new {@link Mono}
     */
    @Test
    public void doOnError_Mono() {
        Consumer<Throwable> consumer = (error) -> {
            log.info("caught error");
        };
        Mono.just(2)
                .map(i -> i / 0) // will produce ArithmeticException
                .doOnError(consumer)
                .onErrorResume(e -> Mono.empty())
                .subscribe(num -> log.info("Number: {}", num));
    }

    /*
     @param exceptionType the type of exceptions to handle
	 @param onError the error handler for relevant errors
	 @param <E> type of the error to handle

	 @return an observed  {@link Mono}
     */
    @Test
    public void doOnErrorIfArithmeticException_Mono() {
        Mono.just(2)
                .map(i -> i / 0) // will produce ArithmeticException
                .doOnError(
                        ArithmeticException.class,
                        error -> log.info("caught error")
                ).onErrorResume(e -> Mono.empty())
                .subscribe(num -> log.info("Number: {}", num));
    }

    /*
    Params:predicate – the matcher for exceptions to handle onError – the error handler for relevant error
    Returns:an observed Mono
     */

    @Test
    public void doOnErrorIfPredicatePasses_Mono() {
        Mono.just(2)
                .map(i -> i / 0) // will produce ArithmeticException
                .doOnError(
                        error -> error instanceof ArithmeticException,
                        System.err::println
                ).onErrorResume(e -> Mono.empty())
                .subscribe(num -> log.info("Number: {}", num));
    }

    /*
    Params:mapper – the error transforming Function
    Returns:a Mono that transforms source errors to other errors
     */
    @Test
    public void onErrorMap_Mono() {
        Mono.just(2)
                .map(i -> i / 0)
                .onErrorMap(e -> new RuntimeException("2 is not divisible by 0"))
                .subscribe(n -> System.out.println("Number -->" + n));
    }

    /*
    Params:type – the class of the exception type to react to mapper – the error transforming Function
    Returns:a Mono that transforms some source errors to other erro
     */
    @Test
    public void onErrorMapExceptionMatched_Mono() {
        Mono.just(2)
                .map(i -> i / 0)
                .onErrorMap(ArithmeticException.class, e -> new RuntimeException(" number is not divisible by 0"))
                .subscribe(n -> System.out.println(n));
    }

    @Test
    public void onErrorMapPredicateMatch_mono() {
        Mono.just(2).map(i -> i / 0)
                .onErrorMap(error -> error instanceof ArrayIndexOutOfBoundsException, e -> new Exception("Number is not divisible by 0"))
                .subscribe(num -> System.out.println(num));
    }

    @Test
    public void onErrorMapPredicateMatch_Flux(){
        Flux.just(2,0,1).map(i->i/0)
                .onErrorMap(ArithmeticException.class, e -> new RuntimeException(" number is not divisible by 0"))
                .subscribe(n -> System.out.println("Num-->"+n));
    }

}
