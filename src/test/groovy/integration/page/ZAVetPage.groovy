package integration.pages

import geb.Page

class ZAVetPage extends Page {
    static url = 'http://localhost:7000/vets.html'
    static at = { title == "PetClinic :: a Spring Framework demonstration" }
    static content = {
        heading { $("h2").text() }
    }
}
