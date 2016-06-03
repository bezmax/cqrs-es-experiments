package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.infrastructure.stats.HasTopicStats
import com.bezmax.cqrscourse.cooking.infrastructure.stats.TopicStats
import com.bezmax.cqrscourse.cooking.messages.MessageBase

class SimplePublisher implements Publisher, HasTopicStats {
    private final Map<String, Set<Handles>> subscribers = [:]

    def <T> void publish(T body) {
        def wrapper = MessageBase.newExchange(body)
        publish(body.class.name, wrapper)
        publish(wrapper.corrId, wrapper)
    }

    def <T> void publish(T body, String corrId) {
        def wrapper = MessageBase.newExchange(body, corrId)
        publish(body.class.name, wrapper)
        publish(wrapper.corrId, wrapper)
    }

    def <T> void respondTo(MessageBase<?> source, T body) {
        respondTo(source, body.class.name, body)
    }

    def <T> void respondTo(MessageBase<?> source, String target, T body) {
        MessageBase<T> wrapper = source.buildResponse(body)
        publish(target, wrapper)
        publish(wrapper.corrId, wrapper)
    }

    public <M extends MessageBase> void publish(String topic, M msg) {
        def exchange = new SimpleExchange<M>(
                publisher: this,
                sourceTopic: topic,
                message: msg
        )
        subscribers[topic]?.each {it.handle(exchange, msg.body)}
    }

    public <M> void subscribe(String topic, Handles<M> handler) {
        synchronized (subscribers) {
            def pool = subscribers[topic]
            subscribers[topic] = pool == null ? [handler] as Set : pool + handler
        }
    }

    def <M> void subscribe(Class<M> msgType, Handles<M> handler) {
        subscribe(msgType.name, handler)
    }

    def <M> void unsubscribe(Class<M> msgType, Handles<M> handler) {
        unsubscribe(msgType.name, handler)
    }

    def <M> void unsubscribe(String topic, Handles<M> handler) {
        synchronized (subscribers) {
            def pool = subscribers[topic]
            def newPool = pool == null ? [] as Set : pool - handler
            if (newPool.empty) {
                subscribers.remove(topic)
            } else {
                subscribers[topic] = newPool
            }
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

    public static class SimpleExchange<T> implements Exchange<T> {
        SimplePublisher publisher
        String sourceTopic
        MessageBase<T> message

        String getCorrId() {
            return message.corrId
        }

        def <R> void respond(R response) {
            publisher.respondTo(message, response)
        }

        def <R> void respond(String topic, R response) {
            publisher.respondTo(message, topic, response)
        }

        String toString() {
            "Exchange($sourceTopic, $message)"
        }
    }
}
