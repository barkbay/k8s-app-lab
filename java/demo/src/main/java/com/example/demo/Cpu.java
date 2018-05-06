package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cpu")
public class Cpu {

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String cpu() {
        return "Runtime.getRuntime().availableProcessors() : " + Runtime.getRuntime().availableProcessors();
    }

}
