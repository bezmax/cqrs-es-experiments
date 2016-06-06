package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.messages.Message


interface Exchange<T> {
    String getSourceTopic()
    Message<T> getMessage()
    String getCorrId()

    public <R> void respond(R response)
    public <R> void respond(String topic, R response)
}