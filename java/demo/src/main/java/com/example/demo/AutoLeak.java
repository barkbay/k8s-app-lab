package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static java.lang.Math.max;
import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/autoleak")
public class AutoLeak {

    private static final List<byte[]> bomb = new ArrayList<>();
    private static final LeakThread LEAK_THREAD = new LeakThread();

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity leak() {

        System.out.printf("/autoleak called\n");
        synchronized (LEAK_THREAD) {
            if (LEAK_THREAD.getState().equals(Thread.State.NEW)) {
                LEAK_THREAD.setDaemon(true);
                System.out.printf("Autoleak thread started\n");
                LEAK_THREAD.start();
            } else {
                System.out.printf("Autoleak thread state is %s\n", LEAK_THREAD.getState());
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    static class LeakThread extends Thread {

        static Runtime runtime = Runtime.getRuntime();
        static long maxMemory = runtime.maxMemory();

        LeakThread() {
            super("AutoLeak");
        }

        @Override
        public void run() {
            System.out.printf("max memory is %d bytes\n", maxMemory);
            int ratio = 2;
            for (;; ) {
                long freeMemory = runtime.freeMemory();
                int fill = toIntExact(freeMemory / ratio);
                try {
                    Thread.sleep(2000);
                    System.out.printf("Max : %d - Free : %d - Ratio : %d - Add %d bytes\n", maxMemory, freeMemory, ratio, fill);
                    bomb.add(new byte[fill]);
                    System.out.println(bomb.size());
                } catch (OutOfMemoryError e) {
                    ratio = ratio + 1;
                    System.gc();
                } catch (Throwable t) {
                    t.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}
