package com.bezmax.cqrscourse.cooking.actors.midgets

import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.actors.midgets.simple.SimpleMidget
import com.bezmax.cqrscourse.cooking.actors.midgets.simple.ZimbMidget

class MidgetFactory {
    Midget summonMidgetFor(Order order) {
        return order.shady?new ZimbMidget():new SimpleMidget()
    }
}
