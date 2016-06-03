package com.bezmax.cqrscourse.cooking

import com.bezmax.cqrscourse.cooking.infrastructure.Exchange

interface Handles<M> {
    void handle(Exchange<M> exchange, M msg)
}
