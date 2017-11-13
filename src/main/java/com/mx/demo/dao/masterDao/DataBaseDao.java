package com.mx.demo.dao.masterDao;

import com.mx.demo.pojo.User;

import java.util.List;
import java.util.Map;

public interface DataBaseDao {

    public List<Map<String, String>> findByMap(Map<String, String> map);

    public void updateUser(Map<String, String> map);
}
