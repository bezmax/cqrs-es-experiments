package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.CanHandleOrder
import com.bezmax.cqrscourse.cooking.Order
import org.slf4j.LoggerFactory


class Printer implements CanHandleOrder {
    static LOGGER = LoggerFactory.getLogger(Printer)

    void handle(Order o) {
        LOGGER.info(o.serialize())
    }
}
