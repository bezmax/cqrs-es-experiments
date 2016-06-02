package com.bezmax.cqrscourse.cooking

import com.bezmax.cqrscourse.cooking.messages.MessageBase


interface CanHandle<M extends MessageBase> {
    void handle(M msg)
}
