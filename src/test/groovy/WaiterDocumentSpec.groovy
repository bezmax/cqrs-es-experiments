import com.bezmax.cqrscourse.cooking.Order
import spock.lang.Specification

class WaiterDocumentSpec extends Specification {
    def testJson =
"""
{
    "tableNumber" : 23,
    "lineItems" : [{
        "quantity": 2,
        "item": "razor blade ice cream",
        "price": 2.99
    }],
    "tax": 3.00,
    "total" : 3.00,
    "paid" : false,
    "ingredients" : "razor blades, ice cream"
}
"""
    def "wrapper changes table number without impacting the rest"() {
        setup:
        def wrapper = new Order(testJson)

        expect:
        wrapper.tableNumber == 23

        when:
        wrapper.tableNumber = 54
        def newJson = wrapper.serialize()

        then:
        newJson.contains("\"tableNumber\":54")
        newJson.contains("\"lineItems\"")
    }
}
