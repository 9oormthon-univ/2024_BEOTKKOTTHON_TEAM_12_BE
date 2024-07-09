package com.backend.wear.controller;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class TestController {
    //
    @RequestMapping()
    @GetMapping()
    public String main(){
        return "Hello World ";
    }
}