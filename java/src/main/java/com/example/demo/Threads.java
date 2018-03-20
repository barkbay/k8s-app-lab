package com.example.demo;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

@Controller
@RequestMapping("/threads")
public class Threads {

    @RequestMapping(method = RequestMethod.GET, produces = { MediaType.TEXT_PLAIN_VALUE } )
    public @ResponseBody String cpu() {
        ThreadInfo[]  threadInfos = ManagementFactory.getThreadMXBean()
                .dumpAllThreads(true,
                        true);
        StringBuilder dump        = new StringBuilder();
        dump.append(String.format("%n"));
        for (ThreadInfo threadInfo : threadInfos) {
            dump.append(String.format("%s \n", threadInfo.getThreadName()));
        }

        return dump.toString();
    }

}
