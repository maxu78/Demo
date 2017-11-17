package com.mx.demo.controller;

import com.github.pagehelper.PageInfo;
import com.mx.demo.pojo.User;
import com.mx.demo.service.DataBaseService;
import com.mx.demo.util.FileUtil;
import com.mx.demo.util.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/oper")
public class DataBaseController {

    Logger logger = LoggerFactory.getLogger(DataBaseController.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private String uploadPath = "/upload/";

    private String downloadPath = "/download/";

    private String modelPath = "/model/";

    @Autowired
    private DataBaseService dataBaseService;

    @RequestMapping("/index")
    public String toIndex(){
        return "DataBaseQuery";
    }

    @RequestMapping("/batchImport")
    public String toBatchAdd(){
        return "DataBaseBatchImport";
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
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String outPath = downloadPath+"User.xls";
        String username = request.getParameter("username");
        String description = request.getParameter("description");
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("description", description);
        dataBaseService.exportExcel(map, outPath);

        FileUtil util = new FileUtil();
        util.getDownload(outPath, request, response);
    }

    @RequestMapping("/upload")
    @ResponseBody
    public List<Map<String, String>> upLoadFile(MultipartHttpServletRequest request){
        String inputName = "batchimportfile";
        logger.info(request.getParameter(inputName));
        Date now = new Date();
        String fileName = sdf.format(now);
        FileUtil util = new FileUtil();
        String filePath = util.singleUploadFile(request, inputName, uploadPath, fileName);

        //读取excel文件
        List<Map<String, String>> result = dataBaseService.checkExcel(filePath);
        //放到session中
        HttpSession session = request.getSession();
        session.setAttribute("batchImportList", result);
        return result;
    }

    @RequestMapping("/batchAdd")
    @ResponseBody
    public Map<String, String> batchAdd(HttpServletRequest request){
        String status = "ok";
        Map<String, String> map = new HashMap<String, String>();
        HttpSession session = request.getSession();
        List<Map<String, String>> list = (List<Map<String, String>>) session.getAttribute("batchImportList");
        try{
            dataBaseService.batchAdd(list);
            session.removeAttribute("batchImportList");
        } catch (Exception e){
            e.printStackTrace();
            status = "fail";
        }
        map.put("status", status);
        return map;
    }

    @RequestMapping("/downModel")
    @ResponseBody
    public void downModel(HttpServletRequest request, HttpServletResponse response){
        FileUtil util = new FileUtil();
        String fileName = request.getParameter("fileName");
        try {
            util.getDownload(modelPath + fileName, request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
