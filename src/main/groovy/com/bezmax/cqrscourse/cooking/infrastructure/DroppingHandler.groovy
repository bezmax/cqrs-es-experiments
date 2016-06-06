package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import org.slf4j.LoggerFactory


class DroppingHandler<T> implements Handles<T> {
    static LOGGER = LoggerFactory.getLogger(DroppingHandler)

    Handles<T> forwardTo
    def dropChance = 0.1

    void handle(Exchange<T> exchange, T msg) {
        if (Math.random() > dropChance) {
            forwardTo.handle(exchange, msg)
        } else {
            LOGGER.info("Oops!")
        }
    }

    String toString() {
        return "DroppingHandler -> ${forwardTo}"
    }
}
