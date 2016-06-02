package com.bezmax.cqrscourse.cooking

import com.bezmax.cqrscourse.cooking.messages.MessageBase

interface Publisher extends HasTopicStats {
    public <M extends MessageBase> void publish(M msg)
    public <M extends MessageBase> void subscribe(Class<M> msgType, Handles<M> handler)
    public <M extends MessageBase> void unsubscribe(Class<M> msgType, Handles<M> handler)

    public <M extends MessageBase> void publish(String topic, M msg)
    public <M extends MessageBase> void subscribe(String topic, Handles<M> handler)
    public <M extends MessageBase> void unsubscribe(String topic, Handles<M> handler)
}