package com.mx.demo.service;

import com.github.pagehelper.PageInfo;
import com.mx.demo.pojo.User;

import java.util.List;
import java.util.Map;

public interface DataBaseService {

    public PageInfo<Map<String, String>> dataBaseService(Map<String, String> map, int pageNow, int pageSize);

    public void updateUser(Map<String, String> map);

    public Map<String, String> checkSame(String username);

    public void addUser(String data);
}
