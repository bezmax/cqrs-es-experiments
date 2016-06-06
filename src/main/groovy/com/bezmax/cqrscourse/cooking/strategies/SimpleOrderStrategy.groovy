package com.bezmax.cqrscourse.cooking.strategies

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.actors.midgets.MidgetHouse
import com.bezmax.cqrscourse.cooking.actors.midgets.MidgetFactory
import com.bezmax.cqrscourse.cooking.infrastructure.DelayedSender
import com.bezmax.cqrscourse.cooking.infrastructure.DroppingHandler
import com.bezmax.cqrscourse.cooking.infrastructure.QueuedDispatcher
import com.bezmax.cqrscourse.cooking.infrastructure.stats.HasQueueStats
import com.bezmax.cqrscourse.cooking.infrastructure.stats.HasTopicStats
import com.bezmax.cqrscourse.cooking.infrastructure.Publisher
import com.bezmax.cqrscourse.cooking.actors.*
import com.bezmax.cqrscourse.cooking.infrastructure.stats.MessageStats
import com.bezmax.cqrscourse.cooking.infrastructure.SimplePublisher
import com.bezmax.cqrscourse.cooking.infrastructure.ThreadedDispatcher
import com.bezmax.cqrscourse.cooking.infrastructure.stats.TopicStats
import com.bezmax.cqrscourse.cooking.messages.commands.CollectPayment
import com.bezmax.cqrscourse.cooking.messages.commands.CookFood
import com.bezmax.cqrscourse.cooking.messages.commands.DelayedMessage
import com.bezmax.cqrscourse.cooking.messages.commands.PriceOrder
import com.bezmax.cqrscourse.cooking.messages.events.OrderCompleted
import com.bezmax.cqrscourse.cooking.messages.events.OrderPaid
import com.bezmax.cqrscourse.cooking.messages.events.OrderPlaced

class SimpleOrderStrategy implements HasQueueStats, HasTopicStats {
    List<Waiter> waiters
    List<Cashier> cashiers
    List<AssistantManager> assists
    List<Cook> cooks

    private ThreadedDispatcher cashierPool
    private ThreadedDispatcher assistPool
    private ThreadedDispatcher cookPool
    private DroppingHandler dropper

    private QueuedDispatcher<OrderPlaced> orderPlacedBuffer

    private Printer printer = new Printer()

    private Publisher publisher = new SimplePublisher()

    private DelayedSender delayedSender = new DelayedSender()

    private MidgetHouse midgetHouse = new MidgetHouse(
            factory: new MidgetFactory(),
            pub: publisher
    )

    void start() {
        waiters.each {it.pub = publisher}

        cashierPool = new ThreadedDispatcher(cashiers)
        cashierPool.name = "CashierPool"
        assistPool = new ThreadedDispatcher(assists)
        assistPool.name = "AssistPool"

        cookPool = new ThreadedDispatcher<CookFood>(cooks)
        cookPool.name = "CookPool"

        dropper = new DroppingHandler<CookFood>(dropChance: 0.5)
        dropper.forwardTo = cookPool

        orderPlacedBuffer = new QueuedDispatcher<OrderPlaced>()
        orderPlacedBuffer.name = "OrderPlacedBuffer"
        orderPlacedBuffer.forwardTo = midgetHouse as Handles<OrderPlaced>

        publisher.subscribe(OrderPlaced, orderPlacedBuffer)
        publisher.subscribe(CookFood, dropper)
        publisher.subscribe(PriceOrder, assistPool)
        publisher.subscribe(CollectPayment, cashierPool)
        publisher.subscribe(OrderCompleted, printer)
        publisher.subscribe(OrderCompleted, midgetHouse as Handles<OrderCompleted>)
        publisher.subscribe(DelayedMessage, delayedSender)

        delayedSender.start()
        cashierPool.start()
        assistPool.start()
        cookPool.start()
        orderPlacedBuffer.start()
    }

    List<MessageStats> getQueueStats() {
        cashierPool.queueStats + assistPool.queueStats + cookPool.queueStats + printer.queueStats + orderPlacedBuffer.queueStats + delayedSender.queueStats
    }

    void addOrders(int count, boolean shady = false) {
        while (count-- > 0) {
            waiters[count % waiters.size()].createOrder(shady)
        }
    }

    List<TopicStats> getTopicStats() {
        return publisher.topicStats
    }
}
