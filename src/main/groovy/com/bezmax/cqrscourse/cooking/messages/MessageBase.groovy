package com.bezmax.cqrscourse.cooking.messages


abstract class MessageBase {
    UUID uid = UUID.randomUUID()


    @Override
    public String toString() {
        "Message[$uid]"
    }
}
