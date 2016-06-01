import com.bezmax.cqrscourse.cooking.CanHandleOrder
import com.bezmax.cqrscourse.cooking.Order
import com.bezmax.cqrscourse.cooking.Waiter
import spock.lang.Specification


class WaiterSpec extends Specification {
    def "creates new order and passes forward"() {
        setup:
        def nextHandler = Mock(CanHandleOrder)
        def waiter = new Waiter(nextHandler)

        when:
        waiter.createOrder()

        then:
        1 * nextHandler.handle(_ as Order)
    }
}