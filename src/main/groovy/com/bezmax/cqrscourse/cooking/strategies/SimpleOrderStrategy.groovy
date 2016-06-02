package com.bezmax.cqrscourse.cooking.strategies

import com.bezmax.cqrscourse.cooking.HasQueueStats
import com.bezmax.cqrscourse.cooking.HasTopicStats
import com.bezmax.cqrscourse.cooking.Publisher
import com.bezmax.cqrscourse.cooking.actors.AssistantManager
import com.bezmax.cqrscourse.cooking.actors.Cashier
import com.bezmax.cqrscourse.cooking.actors.Cook
import com.bezmax.cqrscourse.cooking.actors.Printer
import com.bezmax.cqrscourse.cooking.actors.Waiter
import com.bezmax.cqrscourse.cooking.infrastructure.MessageStats
import com.bezmax.cqrscourse.cooking.infrastructure.SimplePublisher
import com.bezmax.cqrscourse.cooking.infrastructure.ThreadedDispatcher
import com.bezmax.cqrscourse.cooking.infrastructure.TopicStats
import com.bezmax.cqrscourse.cooking.messages.FoodCooked
import com.bezmax.cqrscourse.cooking.messages.OrderPaid
import com.bezmax.cqrscourse.cooking.messages.OrderPlaced
import com.bezmax.cqrscourse.cooking.messages.OrderPriced

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

        publisher.subscribe(OrderPlaced, cookPool)
        publisher.subscribe(FoodCooked, assistPool)
        publisher.subscribe(OrderPriced, cashierPool)
        publisher.subscribe(OrderPaid, printer)

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
