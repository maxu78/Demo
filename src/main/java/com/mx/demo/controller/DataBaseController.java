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
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/oper")
public class DataBaseController {

    Logger logger = LoggerFactory.getLogger(DataBaseController.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

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
        Map<String, String> result = new HashMap<String, String>();
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String description = request.getParameter("description");
        map.put("id", id);
        map.put("username", username);
        map.put("description", description);
        try {
            dataBaseService.updateUser(map);
            map.put("status", "ok");
        }catch (Exception e){
            map.put("status", "fail");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping("/checkSame")
    @ResponseBody
    public Map<String, String> checkSame(HttpServletRequest request){
        Map<String, String> result = new HashMap<String, String>();
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        Map<String, String> map = dataBaseService.checkSame(username);
        if(map == null){
            result.put("status","ok");
        }else{
            result.put("status", "notExist");
        }
        return map;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Map<String, String> add(HttpServletRequest request){
        Map<String, String> result = new HashMap<String, String>();
        String addData = request.getParameter("addData");
        try {
            dataBaseService.addUser(addData);
            result.put("status", "ok");
        }catch (Exception e){
            result.put("status", "fail");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, String> delete(HttpServletRequest request){
        Map<String, String> result = new HashMap<String, String>();
        String ids = request.getParameter("ids");
        List<String> list = new ArrayList<String>();
        String[] id = ids.split(",");
        for(int i=0; i<id.length; i++){
            list.add(id[i]);
        }
        try {
            dataBaseService.deleteUser(list);
            result.put("status", "ok");
        }catch (Exception e){
            result.put("status", "fail");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/exportExcel")
    @ResponseBody
    public void exportExcel(HttpServletRequest request, HttpServletResponse response){
        String outPath = "D://excel/User.xls";
        BufferedInputStream bis = null;
        OutputStream os = null;
        Map<String, String> result = new HashMap<String, String>();
        String username = request.getParameter("username");
        String description = request.getParameter("description");
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("description", description);
        try {
            dataBaseService.exportExcel(map, outPath);

            //下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + outPath);
            byte[] buff = new byte[1024];
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(outPath)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
