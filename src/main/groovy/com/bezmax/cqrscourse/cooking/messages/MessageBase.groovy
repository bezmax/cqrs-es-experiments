package com.bezmax.cqrscourse.cooking.messages


abstract class MessageBase {
    UUID msgId = UUID.randomUUID()
    UUID corrId
    UUID causeId

    MessageBase(UUID corrId, UUID causeId) {
        this.corrId = corrId
        this.causeId = causeId
    }

    @Override
    public String toString() {
        "Message[$msgId, $corrId, $causeId]"
    }
}
