package com.webi.rummy.game.service
import com.webi.ent.util.RummyGameUtil
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(RummyGameService)
class RummyGameServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    def "test something"() {
        when:
        List deck = RummyGameUtil.getNewHandPositionDeck()
        println deck

        then:
        noExceptionThrown()
    }
}
