import com.bezmax.cqrscourse.trailingstoploss.CanPublishMessages
import com.bezmax.cqrscourse.trailingstoploss.messages.PositionAcquired
import com.bezmax.cqrscourse.trailingstoploss.messages.PriceUpdated
import com.bezmax.cqrscourse.trailingstoploss.messages.RemoveFrom10SecWindow
import com.bezmax.cqrscourse.trailingstoploss.messages.RemoveFrom13SecWindow
import com.bezmax.cqrscourse.trailingstoploss.messages.SendToMeIn
import com.bezmax.cqrscourse.trailingstoploss.TrailingStopLossAgent
import com.bezmax.cqrscourse.trailingstoploss.messages.StopLossHit
import com.bezmax.cqrscourse.trailingstoploss.messages.StopLossPriceUpdated
import spock.lang.Specification


class ProcessManagerSpec extends Specification {
    def publisher
    def agent

    void setup() {
        publisher = Mock(CanPublishMessages)
        agent = new TrailingStopLossAgent(publisher: publisher, buffer: 50)
    }

    def "price changes -> register delayed removal messages"() {
        when:
        agent.receive(new PositionAcquired(price: 1000))
        then:
        1*publisher.publish({ StopLossPriceUpdated msg ->
            msg.price == 950
        })

        when:
        agent.receive(new PriceUpdated(price: 1000))
        then:
        1*publisher.publish({ SendToMeIn msg ->
            msg.message instanceof RemoveFrom10SecWindow
            msg.message.price == 1000
        })
        1*publisher.publish({ SendToMeIn msg ->
            msg.message instanceof RemoveFrom13SecWindow
            msg.message.price == 1000
        })
    }

    def "potential stop loss higher for 10 seconds -> move stop loss up"() {
        when:
        agent.receive(new PositionAcquired(price: 1000))
        then:
        1*publisher.publish({ StopLossPriceUpdated msg ->
            msg.price == 950
        })

        when:
        agent.receive(new PriceUpdated(price: 1000))
        agent.receive(new PriceUpdated(price: 990))
        agent.receive(new PriceUpdated(price: 1100))
        then:
        3*publisher.publish({ SendToMeIn msg -> msg.message instanceof RemoveFrom10SecWindow })
        3*publisher.publish({ SendToMeIn msg -> msg.message instanceof RemoveFrom13SecWindow })
        0*publisher.publish(StopLossPriceUpdated)

        when:
        agent.receive(new RemoveFrom10SecWindow(price: 1000))
        agent.receive(new RemoveFrom10SecWindow(price: 990))
        then:
        0*publisher.publish(StopLossPriceUpdated)

        when:
        agent.receive(new RemoveFrom10SecWindow(price: 1100))
        then:
        1*publisher.publish({ StopLossPriceUpdated msg -> msg.price == 1050 })
    }

    def "price is lower than stop loss for 13 seconds -> notify about stop loss hit"() {
        when:
        agent.receive(new PositionAcquired(price: 1000))
        then:
        1*publisher.publish({ StopLossPriceUpdated msg ->
            msg.price == 950
        })

        when:
        agent.receive(new PriceUpdated(price: 1000))
        agent.receive(new PriceUpdated(price: 950))
        agent.receive(new PriceUpdated(price: 940))
        then:
        3*publisher.publish({ SendToMeIn msg -> msg.message instanceof RemoveFrom10SecWindow })
        3*publisher.publish({ SendToMeIn msg -> msg.message instanceof RemoveFrom13SecWindow })
        0*publisher.publish(StopLossHit)

        when:
        agent.receive(new RemoveFrom13SecWindow(price: 1000))
        agent.receive(new RemoveFrom13SecWindow(price: 950))
        then:
        0*publisher.publish(StopLossHit)

        when:
        agent.receive(new RemoveFrom13SecWindow(price: 940))
        then:
        1*publisher.publish({ StopLossHit msg ->
            msg.price == 950
        })
    }

    def "price changes in the window -> do nothing"() {
        when:
        agent.receive(new PositionAcquired(price: 1000))
        then:
        1*publisher.publish({ StopLossPriceUpdated msg ->
            msg.price == 950
        })

        when:
        agent.receive(new PriceUpdated(price: 1000))
        agent.receive(new PriceUpdated(price: 900))
        agent.receive(new PriceUpdated(price: 800))
        agent.receive(new PriceUpdated(price: 1200))

        then:
        4*publisher.publish({ SendToMeIn msg -> msg.message instanceof RemoveFrom10SecWindow })
        4*publisher.publish({ SendToMeIn msg -> msg.message instanceof RemoveFrom13SecWindow })

        and:
        0*publisher.publish(StopLossHit)

        and:
        0*publisher.publish(StopLossPriceUpdated)
    }
}