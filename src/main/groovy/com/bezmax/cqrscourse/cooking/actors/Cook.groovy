package com.bezmax.cqrscourse.cooking.actors

import com.bezmax.cqrscourse.cooking.CanHandle
import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.Publisher

import com.bezmax.cqrscourse.cooking.messages.FoodCooked
import com.bezmax.cqrscourse.cooking.messages.OrderPlaced
import org.slf4j.LoggerFactory

class Cook implements CanHandle<OrderPlaced> {
    static LOGGER = LoggerFactory.getLogger(Cook)

    static cookbook = [
        "razorblade icecream": "razor blades, ice cream",
        "baked mushroom": "mushroom"
    ]

    def name
    def cookTime = 3000
    Publisher pub

    void handle(OrderPlaced msg) {
        LOGGER.debug("{}: {}", this, msg)
        Order o = msg.order
        o.ingredients = o.items.collect {cookbook[it.item]}.join(", ")
        Thread.sleep(cookTime)
        pub.publish(FoodCooked.toString(), new FoodCooked(order: o))
    }

    @Override
    public String toString() {
        "Cook[$name]"
    }
}
