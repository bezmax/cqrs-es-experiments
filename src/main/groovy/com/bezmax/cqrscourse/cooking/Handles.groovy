package com.bezmax.cqrscourse.cooking

import com.bezmax.cqrscourse.cooking.messages.MessageBase


interface Handles<M extends MessageBase> {
    void handle(M msg)
}
