package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.messages.MessageBase
import org.slf4j.LoggerFactory


class FairQueueDispatcher<M extends MessageBase> implements Handles<M> {
    static LOGGER = LoggerFactory.getLogger(FairQueueDispatcher)

    def name = "FairDispatcher"
    def threshold = 5
    List<QueuedDispatcher<M>> orderHandlers

    void handle(M msg) {
        LOGGER.debug("${msg}")

        def target
        while (null == (target = orderHandlers.find { it.count < threshold })) {
            Thread.sleep(500)
        }

        target.handle(msg)
    }

    String toString() {
        name
    }
}
