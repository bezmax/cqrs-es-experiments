package com.bezmax.cqrscourse.cooking.strategies

import com.bezmax.cqrscourse.cooking.CanHandle
import com.bezmax.cqrscourse.cooking.CanForward
import com.bezmax.cqrscourse.cooking.HasMessageStats
import com.bezmax.cqrscourse.cooking.Publisher
import com.bezmax.cqrscourse.cooking.actors.AssistantManager
import com.bezmax.cqrscourse.cooking.actors.Cashier
import com.bezmax.cqrscourse.cooking.actors.Cook
import com.bezmax.cqrscourse.cooking.actors.Printer
import com.bezmax.cqrscourse.cooking.actors.Waiter
import com.bezmax.cqrscourse.cooking.infrastructure.MessageStats
import com.bezmax.cqrscourse.cooking.infrastructure.SimplePublisher
import com.bezmax.cqrscourse.cooking.infrastructure.ThreadedDispatcher
import com.bezmax.cqrscourse.cooking.messages.FoodCooked
import com.bezmax.cqrscourse.cooking.messages.OrderPaid
import com.bezmax.cqrscourse.cooking.messages.OrderPlaced
import com.bezmax.cqrscourse.cooking.messages.OrderPriced

class SimpleOrderStrategy implements HasMessageStats {
    List<Waiter> waiters
    List<Cashier> cashiers
    List<AssistantManager> assists
    List<Cook> cooks

    private cashierPool
    private assistPool
    private cookPool
    private printer = new Printer()

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

        publisher.subscribe(OrderPlaced.toString(), cookPool)
        publisher.subscribe(FoodCooked.toString(), assistPool)
        publisher.subscribe(OrderPriced.toString(), cashierPool)
        publisher.subscribe(OrderPaid.toString(), printer)

        cashierPool.start()
        assistPool.start()
        cookPool.start()
    }

    List<MessageStats> getMessageStats() {
        cashierPool.messageStats + assistPool.messageStats + cookPool.messageStats + printer.messageStats
    }

    void addOrders(int count) {
        while (count-- > 0) {
            waiters[count % waiters.size()].createOrder()
        }
    }
}
