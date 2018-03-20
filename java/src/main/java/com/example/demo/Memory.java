package com.example.demo;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/memory")
public class Memory {

    @RequestMapping(method = RequestMethod.GET, produces = { MediaType.TEXT_PLAIN_VALUE })
    public @ResponseBody String cpu() {
        Runtime runtime = Runtime.getRuntime();
        StringBuilder sb = new StringBuilder(4096);
        sb.append("Max Heap Memory : ")
          .append(humanReadableByteCount(runtime.maxMemory(), true)).append("\n")
          .append("Free Heap memory : ").append(humanReadableByteCount(runtime.freeMemory(), true)).append("\n");
        return sb.toString();
    }

    // StackOverflow FTW !
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
