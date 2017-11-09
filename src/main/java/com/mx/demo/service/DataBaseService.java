package com.mx.demo.service;

import com.github.pagehelper.PageInfo;
import com.mx.demo.pojo.User;

import java.util.Map;

public interface DataBaseService {

    public PageInfo<User> dataBaseService(Map<String, String> map, int pageNow, int pageSize);
}
