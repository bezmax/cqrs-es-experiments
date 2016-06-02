package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.HasTopicStats
import com.bezmax.cqrscourse.cooking.Publisher
import com.bezmax.cqrscourse.cooking.messages.MessageBase

class SimplePublisher implements Publisher, HasTopicStats {
    private final Map<String, Set<Handles>> subscribers = [:]

    def <M extends MessageBase> void publish(M msg) {
        publish(msg.class.name, msg)
    }

    public <M extends MessageBase> void publish(String topic, M msg) {
        subscribers[topic]?.each {it.handle(msg)}
    }

    public <M extends MessageBase> void subscribe(String topic, Handles<M> handler) {
        synchronized (subscribers) {
            def pool = subscribers[topic]
            subscribers[topic] = pool == null ? [handler] as Set : pool + handler
        }
    }

    def <M extends MessageBase> void subscribe(Class<M> msgType, Handles<M> handler) {
        subscribe(msgType.name, handler)
    }

    def <M extends MessageBase> void unsubscribe(Class<M> msgType, Handles<M> handler) {
        unsubscribe(msgType.name, handler)
    }

    def <M extends MessageBase> void unsubscribe(String topic, Handles<M> handler) {
        synchronized (subscribers) {
            def pool = subscribers[topic]
            subscribers[topic] = pool == null ? [] as Set : pool - handler
        }
    }

    List<TopicStats> getTopicStats() {
        subscribers.collect {key, val ->
            new TopicStats(
                    topic: key,
                    subscribers: val.collect {
                        it.toString()
                    })
        }
    }
}
