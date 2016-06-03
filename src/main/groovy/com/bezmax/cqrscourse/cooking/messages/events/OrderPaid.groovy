package com.bezmax.cqrscourse.cooking.messages.events

import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.messages.MessageBase

class OrderPaid extends MessageBase {
    Order order
}
