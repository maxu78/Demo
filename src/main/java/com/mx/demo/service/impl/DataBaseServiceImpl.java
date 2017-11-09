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

import java.util.List;
import java.util.Map;

@Service
public class DataBaseServiceImpl implements DataBaseService{

    private Logger logger = LoggerFactory.getLogger(DataBaseService.class);

    @Autowired
    private DataBaseDao dataBaseDao;
    @Override
    public PageInfo<User> dataBaseService(Map<String, String> map, int pageNow, int pageSize){
        Page<User> page = PageHelper.startPage(1, 2);
        List<User> list = dataBaseDao.findByMap(map);
        logger.info(list.toString());
        return new PageInfo<>(list);
    }

}
