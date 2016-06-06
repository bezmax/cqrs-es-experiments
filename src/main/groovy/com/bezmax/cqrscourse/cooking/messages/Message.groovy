package com.bezmax.cqrscourse.cooking.messages


class Message<T> {
    UUID msgId
    UUID causeId
    String corrId

    T body

    public static <M> Message<M> newExchange(M body) {
        return newExchange(body, UUID.randomUUID().toString())
    }

    public static <M> Message<M> newExchange(M body, String corrId) {
        def id = UUID.randomUUID()
        return new Message<M>(
                msgId: id,
                corrId: corrId,
                causeId: id,
                body: body
        )
    }

    public <M> Message<M> buildResponse(M body) {
        return new Message<M>(
                msgId: UUID.randomUUID(),
                corrId: this.corrId,
                causeId: this.msgId,
                body: body
        )
    }

    private Message() {
    }

    @Override
    public String toString() {
        "Message[$msgId, $corrId, $causeId](body = $body)"
    }
}
