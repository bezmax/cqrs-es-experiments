package com.bezmax.cqrscourse.cooking

import com.bezmax.cqrscourse.cooking.messages.MessageBase

interface CanForward<M extends MessageBase> {
    void setForwardTo(CanHandle<M> handler)
}
