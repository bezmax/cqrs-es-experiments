package com.bezmax.cqrscourse.cooking

import com.bezmax.cqrscourse.cooking.actors.AssistentManager
import com.bezmax.cqrscourse.cooking.actors.Cashier
import com.bezmax.cqrscourse.cooking.actors.Cook
import com.bezmax.cqrscourse.cooking.actors.Printer
import com.bezmax.cqrscourse.cooking.actors.Waiter


class ConsoleTest {
    static void main(params) {
        def printer = new Printer()
        def cashier = new Cashier(printer)
        def assist = new AssistentManager(cashier)
        def cook = new Cook(assist)
        def waiter = new Waiter(cook)

        5.times { waiter.createOrder() }
    }
}
