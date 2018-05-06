package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/leak")
public class Leak {

    private static final List<byte[]> bomb = new ArrayList<>();

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity leak(@RequestParam("size") int size) {
        byte[] data = new byte[size];
        System.out.printf("/leak called - %d bytes\n", size);
        bomb.add(data);
        return new ResponseEntity(HttpStatus.OK);
    }

}
