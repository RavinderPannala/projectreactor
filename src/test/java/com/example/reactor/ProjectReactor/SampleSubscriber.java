package com.example.reactor.ProjectReactor;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

public class SampleSubscriber<I extends Number> extends BaseSubscriber<Integer> {

    protected void hookOnSubscribe(Subscription subscription) {
        System.out.println("Hook on Subscribe");
        request(1);
    }

    protected void hookOnNext(Integer value) {
        System.out.println("Hook on Next");
        request(1);
    }
}
