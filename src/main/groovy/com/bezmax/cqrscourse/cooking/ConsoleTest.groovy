package com.bezmax.cqrscourse.cooking

import com.bezmax.cqrscourse.cooking.actors.*
import com.bezmax.cqrscourse.cooking.strategies.SimpleOrderStrategy
import com.bezmax.cqrscourse.cooking.web.JsonTransformer
import org.slf4j.LoggerFactory

import static spark.Spark.*

class ConsoleTest {
    static LOGGER = LoggerFactory.getLogger(ConsoleTest.class)

    static void main(params) {
        Random r = new Random()
        def strategy = new SimpleOrderStrategy(
                waiters: [
                        new Waiter(),
                        new Waiter(),
                        new Waiter()
                ],
                cashiers: [
                        new Cashier(name: "Tom"),
                        new Cashier(name: "Jerry"),
                        new Cashier(name: "Ted")
                ],
                assists: [
                        new AssistantManager(name: "Rick"),
                        new AssistantManager(name: "Max"),
                        new AssistantManager(name: "Greg")
                ],
                cooks: [
                        new Cook(name: "John", cookTime: r.nextInt(3000)),
                        new Cook(name: "Bob", cookTime: r.nextInt(3000)),
                        new Cook(name: "Richard", cookTime: r.nextInt(3000))
                ]
        )

        strategy.start()

        initWeb(strategy)
    }

    static initWeb(SimpleOrderStrategy strategy) {
        port(8080)
        staticFiles.location("/web")

        get("/queueStats", "application/json", {req, resp ->
            resp.type("application/json")
            strategy.queueStats
        }, new JsonTransformer());

        get("/topicStats", "application/json", {req, resp ->
            resp.type("application/json")
            strategy.topicStats
        }, new JsonTransformer());

        post("/addOrders/:count", {req, resp ->
            int count = Integer.valueOf(req.params("count"))
            strategy.addOrders(count, false)
        });

        post("/addOrdersShady/:count", {req, resp ->
            int count = Integer.valueOf(req.params("count"))
            strategy.addOrders(count, true)
        });
    }
}
