package com.bezmax.cqrscourse.cooking

import com.bezmax.cqrscourse.cooking.messages.MessageBase

interface Publisher {
    public <M extends MessageBase> void publish(String topic, M msg)
    public <M extends MessageBase> void subscribe(String topic, CanHandle<M> handler)
}