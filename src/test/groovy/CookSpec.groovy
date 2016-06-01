import com.bezmax.cqrscourse.cooking.CanHandleOrder
import com.bezmax.cqrscourse.cooking.actors.Cook
import com.bezmax.cqrscourse.cooking.Order
import spock.lang.Specification


class CookSpec extends Specification {
    def "cook adds ingredients"() {
        setup:
        def nextHandler = Mock(CanHandleOrder)
        def cook = new Cook(nextHandler)
        def order = new Order()

        when:
        cook.handle(order)

        then:
        order.ingredients == "razor blades, ice cream"
    }
}