package com.mx.demo.controller;

import com.mx.demo.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/oper")
public class DataBaseController {

    @Autowired
    private DataBaseService dataBaseService;

    @RequestMapping("/index")
    public String toIndex(){
        System.out.println("index...");
        return "DataBaseQuery";
    }

    @RequestMapping("/query")
    @ResponseBody
    public List<Map<String, String>> query(HttpServletRequest request) throws Exception{
        String username = request.getParameter("username");
        String description = request.getParameter("description");
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("description", description);
        return dataBaseService.dataBaseService(map);
    }
}
