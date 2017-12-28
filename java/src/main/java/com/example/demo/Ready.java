package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/ready")
public class Ready {

    private static final AtomicInteger delay;

    static {
        String delayEnv = System.getenv("READINESS_DELAY");
        if (delayEnv == null) {
            delay = new AtomicInteger(0);
        } else {
            delay = new AtomicInteger(Integer.parseInt(delayEnv));
        }
        System.out.println("Readiness delay is " + delay.get());
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ResponseEntity ready() {
        if (delay.get() > 0) {
            System.out.printf("/ready called - %d remaining \n", delay.decrementAndGet());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            System.out.printf("/ready called\n");
            return new ResponseEntity(HttpStatus.OK);
        }
    }

}
