package com.mx.demo.dao.masterDao;

import com.mx.demo.pojo.User;

import java.util.List;
import java.util.Map;

public interface DataBaseDao {

    public List<User> findByMap(Map<String, String> map);
}
