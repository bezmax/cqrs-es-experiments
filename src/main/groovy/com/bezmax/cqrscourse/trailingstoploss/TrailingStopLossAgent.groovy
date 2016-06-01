package com.bezmax.cqrscourse.trailingstoploss

import com.bezmax.cqrscourse.trailingstoploss.messages.PositionAcquired
import com.bezmax.cqrscourse.trailingstoploss.messages.PriceUpdated
import com.bezmax.cqrscourse.trailingstoploss.messages.RemoveFrom10SecWindow
import com.bezmax.cqrscourse.trailingstoploss.messages.RemoveFrom13SecWindow
import com.bezmax.cqrscourse.trailingstoploss.messages.SendToMeIn
import com.bezmax.cqrscourse.trailingstoploss.messages.StopLossHit
import com.bezmax.cqrscourse.trailingstoploss.messages.StopLossPriceUpdated

class TrailingStopLossAgent {
    enum State {
        NO_POSITION, POSITION_ACQUIRED
    }

    CanPublishMessages publisher
    int buffer

    State state = State.NO_POSITION

    List<Integer> prices10SecWindow = []
    List<Integer> prices13SecWindow = []

    int stopLossPrice
    int marketPrice

    private int calcStopLoss(marketPrice) {
        return marketPrice - buffer
    }

    def receive(PositionAcquired msg) {
        stopLossPrice = calcStopLoss(msg.price)
        state = State.POSITION_ACQUIRED
        publisher.publish(new StopLossPriceUpdated(price: stopLossPrice))
    }

    def receive(PriceUpdated msg) {
        if (state != State.POSITION_ACQUIRED) {
            return;
        }
        marketPrice = msg.price
        prices10SecWindow << msg.price
        prices13SecWindow << msg.price
        publisher.publish(new SendToMeIn(
                seconds: 10,
                message: new RemoveFrom10SecWindow(price: msg.price))
        )
        publisher.publish(new SendToMeIn(
                seconds: 13,
                message: new RemoveFrom13SecWindow(price: msg.price))
        )
    }

    def receive(RemoveFrom10SecWindow msg) {
        if (state != State.POSITION_ACQUIRED) {
            return;
        }
        def minNewStopLoss = calcStopLoss(prices10SecWindow.min())
        if (minNewStopLoss > stopLossPrice) {
            stopLossPrice = minNewStopLoss
            publisher.publish(new StopLossPriceUpdated(price: minNewStopLoss))
        }
        prices10SecWindow.removeElement(msg.price)
    }

    def receive(RemoveFrom13SecWindow msg) {
        if (state != State.POSITION_ACQUIRED) {
            return;
        }
        if (prices13SecWindow.max() < stopLossPrice) {
            publisher.publish(new StopLossHit(price: stopLossPrice))
            resetState()
        } else {
            prices13SecWindow.removeElement(msg.price)
        }
    }

    private resetState() {
        state = State.NO_POSITION
        prices13SecWindow = []
        prices10SecWindow = []
    }
}
