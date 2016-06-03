package com.bezmax.cqrscourse.cooking.messages.commands

import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.messages.MessageBase


class CollectPayment extends MessageBase {
    Order order
}
