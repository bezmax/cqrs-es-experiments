package com.bezmax.cqrscourse.cooking.infrastructure

import com.bezmax.cqrscourse.cooking.CanStart
import com.bezmax.cqrscourse.cooking.Handles
import com.bezmax.cqrscourse.cooking.infrastructure.stats.HasQueueStats
import com.bezmax.cqrscourse.cooking.infrastructure.stats.MessageStats
import com.bezmax.cqrscourse.cooking.messages.commands.DelayedMessage
import org.slf4j.LoggerFactory

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class DelayedSender implements Handles<DelayedMessage<?>>, CanStart, HasQueueStats {
    static final LOGGER = LoggerFactory.getLogger(DelayedSender)

    private final SortedSet<Exchange<DelayedMessage<?>>> schedule =
            new TreeSet<Exchange<DelayedMessage<?>>>(new Comparator<Exchange<DelayedMessage<?>>>() {
                int compare(Exchange<DelayedMessage<?>> o1, Exchange<DelayedMessage<?>> o2) {
                    o1.message.body.when.compareTo(o2.message.body.when)
                }
            });

    public void handle(Exchange<DelayedMessage<?>> exchange, DelayedMessage<?> msg) {
        synchronized (schedule) {
            LOGGER.info("Received msg: ${msg}")
            schedule.add(exchange)
            schedule.notifyAll()
        }
    }

    private waitNext() {
        synchronized (schedule) {
            if (!schedule.empty) {
                def elem = schedule.first()
                def msecs = ChronoUnit.MILLIS.between(LocalDateTime.now(), elem.message.body.when)
                LOGGER.info("Waiting $msecs msecs for: ${elem.message}")
                if (msecs > 0) {
                    schedule.wait(msecs)

                    if (ChronoUnit.MILLIS.between(LocalDateTime.now(), elem.message.body.when) > 0) {
                        return
                    }
                }
                schedule.remove(elem)
                elem.respond(elem.message.body.target, elem.message.body.body)
                LOGGER.info("Resending: ${elem.message}")
            } else {
                schedule.wait(1000)
            }
        }
    }

    def start() {
        Thread.start {
            while (true) {
                try {
                    waitNext()
                } catch (InterruptedException e) {
                    //Ignore
                }
            }
        }
    }

    List<MessageStats> getQueueStats() {
        return [] + new MessageStats(name: "DelayedSender", count: schedule.size())
    }
}
