package com.mx.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/index")
    public String index(){
        System.out.println("index");
        return "test";
    }

    @RequestMapping("/home1")
    public String getHome(){
        return "home";
    }

    @RequestMapping("/indexHtml")
    public String getIndex(){


        //return "redirect:/index.html";
        return "forward:/index.html";
    }

    @RequestMapping("/indexJsp")
    public String getJspIndex(){

        //return "redirect:/index.html";
        return "index";
    }

    @RequestMapping("/{para}")
    @ResponseBody
    public String test(@PathVariable(name = "para") String para){
        System.out.println(para);
        return para;
    }
}
