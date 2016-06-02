package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.CanHandle
import com.bezmax.cqrscourse.cooking.Publisher
import com.bezmax.cqrscourse.cooking.messages.MessageBase

class SimplePublisher implements Publisher {
    private Map<String, Set<CanHandle>> subscribers = [:]

    public <M extends MessageBase> void publish(String topic, M msg) {
        subscribers[topic]?.each {it.handle(msg)}
    }

    public <M extends MessageBase> void subscribe(String topic, CanHandle<M> handler) {
        def pool = subscribers[topic]
        if (pool == null) {
            pool = new HashSet<CanHandle<M>>()
            subscribers[topic] = pool
        }

        pool.add(handler)
    }
}
