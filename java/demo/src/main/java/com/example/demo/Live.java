package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/healthz")
public class Live {

    private static final AtomicInteger liveness;
    private static final AtomicInteger cliveness = new AtomicInteger(0);

    static {
        String liveEnv = System.getenv("LIVENESS_DELAY");
        if (liveEnv == null) {
            liveness = new AtomicInteger(-1);
        } else {
            liveness = new AtomicInteger(Integer.parseInt(liveEnv));
        }
        System.out.println("Liveness delay is " + liveness.get());
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity live() {
        if (liveness.get() < 0) {
            System.out.printf("/health called - ok\n");
            return new ResponseEntity(HttpStatus.OK);
        } else if (liveness.get() > 0 && cliveness.get() < liveness.get()) {
            System.out.printf("/health called - %d remaining \n", liveness.get() - (cliveness.incrementAndGet()));
            return new ResponseEntity(HttpStatus.OK);
        } else {
            System.out.printf("/health called - fail\n");
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
