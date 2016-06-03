package com.bezmax.cqrscourse.cooking.strategies

import com.bezmax.cqrscourse.cooking.HasQueueStats
import com.bezmax.cqrscourse.cooking.HasTopicStats
import com.bezmax.cqrscourse.cooking.Publisher
import com.bezmax.cqrscourse.cooking.actors.*
import com.bezmax.cqrscourse.cooking.infrastructure.MessageStats
import com.bezmax.cqrscourse.cooking.infrastructure.SimplePublisher
import com.bezmax.cqrscourse.cooking.infrastructure.ThreadedDispatcher
import com.bezmax.cqrscourse.cooking.infrastructure.TopicStats
import com.bezmax.cqrscourse.cooking.messages.commands.CollectPayment
import com.bezmax.cqrscourse.cooking.messages.commands.CookFood
import com.bezmax.cqrscourse.cooking.messages.commands.PriceOrder
import com.bezmax.cqrscourse.cooking.messages.events.FoodCooked
import com.bezmax.cqrscourse.cooking.messages.events.OrderPaid
import com.bezmax.cqrscourse.cooking.messages.events.OrderPlaced
import com.bezmax.cqrscourse.cooking.messages.events.OrderPriced

class SimpleOrderStrategy implements HasQueueStats, HasTopicStats {
    List<Waiter> waiters
    List<Cashier> cashiers
    List<AssistantManager> assists
    List<Cook> cooks

    private ThreadedDispatcher cashierPool
    private ThreadedDispatcher assistPool
    private ThreadedDispatcher cookPool
    private Printer printer = new Printer()

    private Publisher publisher = new SimplePublisher()

    void start() {
        waiters.each {it.pub = publisher}
        cashiers.each {it.pub = publisher}
        assists.each {it.pub = publisher}
        cooks.each {it.pub = publisher}

        cashierPool = new ThreadedDispatcher(cashiers)
        cashierPool.name = "CashierPool"
        assistPool = new ThreadedDispatcher(assists)
        assistPool.name = "AssistPool"
        cookPool = new ThreadedDispatcher(cooks)
        cookPool.name = "CookPool"

        publisher.subscribe(CookFood, cookPool)
        publisher.subscribe(PriceOrder, assistPool)
        publisher.subscribe(CollectPayment, cashierPool)
        publisher.subscribe(OrderPaid, printer)

        publisher.subscribe(OrderPlaced, new SimpleRoute(
                publisher: publisher,
                forwardTo: CookFood.name,
                transformer: {OrderPlaced p -> new CookFood(order: p.order)}
        ))

        publisher.subscribe(FoodCooked, new SimpleRoute(
                publisher: publisher,
                forwardTo: PriceOrder.name,
                transformer: {FoodCooked p -> new PriceOrder(order: p.order)}
        ))

        publisher.subscribe(OrderPriced, new SimpleRoute(
                publisher: publisher,
                forwardTo: CollectPayment.name,
                transformer: {OrderPriced p -> new CollectPayment(order: p.order)}
        ))

        cashierPool.start()
        assistPool.start()
        cookPool.start()
    }

    List<MessageStats> getQueueStats() {
        cashierPool.queueStats + assistPool.queueStats + cookPool.queueStats + printer.queueStats
    }

    void addOrders(int count) {
        while (count-- > 0) {
            waiters[count % waiters.size()].createOrder()
        }
    }

    List<TopicStats> getTopicStats() {
        return publisher.topicStats
    }
}
