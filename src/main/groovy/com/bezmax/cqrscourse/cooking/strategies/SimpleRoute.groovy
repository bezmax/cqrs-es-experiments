package com.bezmax.cqrscourse.cooking.strategies

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.Publisher
import com.bezmax.cqrscourse.cooking.messages.MessageBase

import java.util.function.Function

class SimpleRoute<F extends MessageBase, T extends MessageBase> implements Handles<F> {
    Publisher publisher
    String forwardTo
    Function<F, T> transformer

    void handle(F msg) {
        T result = transformer.apply(msg)
        publisher.publish(forwardTo, result)
    }


    @Override
    public String toString() {
        "SimpleRoute($forwardTo)"
    }
}
