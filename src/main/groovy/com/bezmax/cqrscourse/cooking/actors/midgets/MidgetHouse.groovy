package com.bezmax.cqrscourse.cooking.actors.midgets

import com.bezmax.cqrscourse.cooking.infrastructure.Exchange
import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.infrastructure.Publisher
import com.bezmax.cqrscourse.cooking.messages.events.OrderCompleted
import com.bezmax.cqrscourse.cooking.messages.events.OrderPlaced
import org.slf4j.LoggerFactory

class MidgetHouse implements HandlesOrderCompleted, HandlesOrderPlaced {
    static LOGGER = LoggerFactory.getLogger(MidgetHouse)
    private Map<String, Midget> midgets = [:]
    private Publisher pub

    MidgetFactory factory

    void handle(Exchange<OrderPlaced> exchange, OrderPlaced msg) {
        LOGGER.info("Summoning midget for corr: ${exchange.corrId}")
        def midget = factory.summonMidgetFor(msg.order)
        midgets[exchange.corrId] = midget
        pub.subscribe(exchange.corrId, midget)
        midget.handle(exchange, msg)
    }

    void handle(Exchange<OrderCompleted> exchange, OrderCompleted msg) {
        def midget = midgets[exchange.corrId]
        if (midget) {
            LOGGER.info("Dismissing midget for corr: ${exchange.corrId}")
            pub.unsubscribe(exchange.corrId, midget)
            midgets.remove(exchange.corrId)
        }
    }

    String toString() {
        "MidgetHouse"
    }

    interface HandlesOrderPlaced extends Handles<OrderPlaced> {}
    interface HandlesOrderCompleted extends Handles<OrderCompleted> {}
}
