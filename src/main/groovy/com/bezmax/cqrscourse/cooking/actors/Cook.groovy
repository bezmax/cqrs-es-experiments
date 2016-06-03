package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.infrastructure.Exchange
import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.messages.commands.CookFood
import com.bezmax.cqrscourse.cooking.messages.events.FoodCooked
import org.slf4j.LoggerFactory

class Cook implements Handles<CookFood> {
    static LOGGER = LoggerFactory.getLogger(Cook)

    static cookbook = [
        "razorblade icecream": "razor blades, ice cream",
        "baked mushroom": "mushroom"
    ]

    def name
    def cookTime = 3000

    void handle(Exchange<CookFood> exchange, CookFood msg) {
        LOGGER.debug("{}: {}", this, exchange)
        Order o = msg.order
        o.ingredients = o.items.collect {cookbook[it.item]}.join(", ")
        Thread.sleep(cookTime)

        exchange.respond(new FoodCooked(order: o))
    }

    @Override
    public String toString() {
        "Cook[$name]"
    }
}
