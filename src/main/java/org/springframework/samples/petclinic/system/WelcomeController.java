package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
class WelcomeController {

    private static final String MESSAGE = "> BEST DAY<";
//    private static final String MESSAGE = "> " + Days.WEDNESDAY.name() + " <";

    @GetMapping("/")
    public ModelAndView welcome() {
        ModelAndView model = new ModelAndView();
        model.addObject("day", MESSAGE);
        model.setViewName("welcome");
//        adding a thread sleep
//        try {
//            Thread.sleep(2000);
//        }
//        catch(Exception e) {
//        }

        return model;
    }


//        @GetMapping("/")
//        public String welcome() {
//            return "welcome";
//    }

//    public enum Days {
//        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
//    }
}
