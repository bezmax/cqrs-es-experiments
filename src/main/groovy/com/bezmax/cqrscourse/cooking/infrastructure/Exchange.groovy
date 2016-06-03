package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.messages.MessageBase


interface Exchange<T> {
    String getSourceTopic()
    MessageBase<T> getMessage()
    String getCorrId()

    public <R> void respond(R response)
    public <R> void respond(String topic, R response)
}