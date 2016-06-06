package com.bezmax.cqrscourse.cooking.messages.commands

import java.time.LocalDateTime

class DelayedMessage<T> {
    LocalDateTime when
    String target
    T body


    @Override
    public String toString() {
"""\
DelayedMessage{
    when=$when,
    target='$target',
    body=$body
}"""
    }
}
