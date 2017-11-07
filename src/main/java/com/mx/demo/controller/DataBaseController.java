package com.mx.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oper")
public class DataBaseController {

    @RequestMapping("/index")
    public String toIndex(){
        System.out.println("index...");
        return "DataBaseQuery";
    }
}
