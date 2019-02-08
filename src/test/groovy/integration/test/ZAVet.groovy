package integration.test

import geb.spock.GebSpec
import integration.pages.ZAVetPage

class ZAVet extends GebSpec{

    def 'access vets'() {
        given:
        to ZAVetPage

        when: 'nothing'

        then:
        at ZAVetPage
        heading == "Veterinarians"
    }

    def 'access vets2'() {
        given:
        to ZAVetPage

        when: 'nothing'

        then:
        at ZAVetPage
        heading == "Veterinarians"
    }
}
