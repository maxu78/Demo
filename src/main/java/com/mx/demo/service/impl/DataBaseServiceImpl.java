package com.mx.demo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mx.demo.dao.masterDao.DataBaseDao;
import com.mx.demo.pojo.User;
import com.mx.demo.service.DataBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
