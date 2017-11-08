package com.mx.demo.service.impl;

import com.mx.demo.dao.DataBaseDao.DataBaseDao;
import com.mx.demo.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataBaseServiceImpl implements DataBaseService{

    @Autowired
    private DataBaseDao dataBaseDao;
    @Override
    public List<Map<String, String>> dataBaseService(Map<String, String> map) throws Exception {
        return dataBaseDao.dataBaseQuery(map);
    }
}
