package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.infrastructure.stats.HasTopicStats

interface Publisher extends HasTopicStats {
    def <T> void publish(T body)
    def <T> void publish(T body, String corrId)

    public <M> void subscribe(Class<M> msgType, Handles<M> handler)
    public <M> void unsubscribe(Class<M> msgType, Handles<M> handler)

    public <M> void subscribe(String topic, Handles<M> handler)
    public <M> void unsubscribe(String topic, Handles<M> handler)
}