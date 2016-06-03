package com.bezmax.cqrscourse.cooking.messages


class MessageBase<T> {
    UUID msgId
    UUID causeId
    String corrId

    T body

    public static <M> MessageBase<M> newExchange(M body) {
        return newExchange(body, UUID.randomUUID().toString())
    }

    public static <M> MessageBase<M> newExchange(M body, String corrId) {
        def id = UUID.randomUUID()
        return new MessageBase<M>(
                msgId: id,
                corrId: corrId,
                causeId: id,
                body: body
        )
    }

    public <M> MessageBase<M> buildResponse(M body) {
        return new MessageBase<M>(
                msgId: UUID.randomUUID(),
                corrId: this.corrId,
                causeId: this.msgId,
                body: body
        )
    }

    private MessageBase() {
    }

    @Override
    public String toString() {
        "Message[$msgId, $corrId, $causeId](body = $body)"
    }
}
