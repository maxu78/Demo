package com.mx.demo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mx.demo.dao.masterDao.DataBaseDao;
import com.mx.demo.pojo.User;
import com.mx.demo.service.DataBaseService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class DataBaseServiceImpl implements DataBaseService{

    private Logger logger = LoggerFactory.getLogger(DataBaseService.class);

    @Autowired
    private DataBaseDao dataBaseDao;
    @Override
    public PageInfo<Map<String, String>> dataBaseService(Map<String, String> map, int pageNow, int pageSize){
        Page<Map<String, String>> page = PageHelper.startPage(pageNow, pageSize);
        List<Map<String, String>> list = dataBaseDao.findByMap(map);
        logger.info(list.toString());
        return new PageInfo<Map<String, String>>(list);
    }

    @Override
    public void updateUser(Map<String, String> map) {
        dataBaseDao.updateUser(map);
    }

    @Override
    public Map<String, String> checkSame(String username) {
        return dataBaseDao.checkSame(username);
    }

    @Override
    public void addUser(String data) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String[] dataArr = data.split(";");
        for(int i=0; i<dataArr.length; i++){
            Map<String, String> map = new HashMap<String, String>();
            String[] colArr = dataArr[i].split(",");
            String id = UUID.randomUUID().toString();
            String username = "";
            String description = "";
            for(int j=0; j<colArr.length; j++){
                username = colArr[0];
                if(colArr.length>1){
                    description = colArr[1];
                }
            }
            map.put("id", id);
            map.put("username", colArr[0]);
            map.put("description", description);
            list.add(map);
        }
        for(Map<String, String> map : list){
            dataBaseDao.addUser(map);
        }
    }

    @Override
    public void deleteUser(List<String> list) {
        dataBaseDao.deleteUser(list);
    }

    @Override
    public void exportExcel(Map<String, String> map, String outPath) throws IOException {
        List<Map<String, String>> list = queryAllUser(map);
        List<String> title = new ArrayList<String>();
        title.add("ID");
        title.add("名称");
        title.add("详细");
        List<List<String>> body = new ArrayList<List<String>>();
        for(Map<String, String> m : list){
            List<String> userList = new ArrayList<String>();
            userList.add(m.get("id") == null ? "" : m.get("id"));
            userList.add(m.get("username") == null ? "" : m.get("username"));
            userList.add(m.get("description") == null ? "" : m.get("description"));
            body.add(userList);
        }
        makeExcel(title, body, outPath);
        //下载

    }


    public List<Map<String,String>> queryAllUser(Map<String, String> map) {
        return dataBaseDao.findByMap(map);
    }

    public void makeExcel(List<String> titleList,List<List<String>> list, String outPath) throws IOException {
        String fileType = outPath.substring(outPath.lastIndexOf(".") + 1, outPath.length());
        logger.info("excelType: "+fileType);
        Workbook wb = null;
        if (fileType.equals("xls")) {
            wb = new HSSFWorkbook();
        } else if (fileType.equals("xlsx")) {
            wb = new XSSFWorkbook();
        } else {
            logger.error("您的文档格式不正确！");
        }
        // 创建sheet对象
        Sheet sheet1 = (Sheet) wb.createSheet("sheet1");
        //表头
        Row title = (Row) sheet1.createRow(0);
        for(int i=0; i<titleList.size(); i++){
            Cell cell = title.createCell(i);
            cell.setCellValue(titleList.get(i));
        }

        //表内数据
        for(int i=0; i<list.size(); i++){
            Row row = (Row) sheet1.createRow(i+1);
            List<String> l = list.get(i);
            for (int j = 0; j < l.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(l.get(j));
            }
        }
        File file = new File(outPath);
        // 创建文件流
        OutputStream stream = new FileOutputStream(outPath);
        // 写入数据
        wb.write(stream);
        // 关闭文件流
        stream.close();
        logger.info("数据导出成功");
    }

}
