package com.mx.demo.controller;

import com.github.pagehelper.PageInfo;
import com.mx.demo.pojo.User;
import com.mx.demo.service.DataBaseService;
import com.mx.demo.util.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(DataBaseController.class);

    @Autowired
    private DataBaseService dataBaseService;

    @RequestMapping("/index")
    public String toIndex(){
        System.out.println("index...");
        return "DataBaseQuery";
    }

    @RequestMapping("/query")
    @ResponseBody
    public PageInfo<Map<String, String>> query(HttpServletRequest request) throws Exception{
        String username = request.getParameter("username");
        String description = request.getParameter("description");
        String pageNow = request.getParameter("page");
        String pageSize = request.getParameter("rows");
        int pageNowNum = 1;
        int pageSizeNum = 15;
        try {
            pageNowNum = Integer.parseInt(pageNow);
            pageSizeNum = Integer.parseInt(pageSize);
        } catch (Exception e){
            logger.warn("页码转化错误! 默认当前第1页,每页15行");
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("description", description);
        return dataBaseService.dataBaseService(map, pageNowNum, pageSizeNum);
    }

    @RequestMapping("/update")
    @ResponseBody
    public Map<String, String> update(HttpServletRequest request){
        Map<String, String> map = new HashMap<String, String>();
        dataBaseService.updateUser();
        return map;
    }

//    public Map<String, String> add(HttpServletRequest request) throws Exception{
//
//        String addData = request.getParameter("addData");
//
//    }
}
