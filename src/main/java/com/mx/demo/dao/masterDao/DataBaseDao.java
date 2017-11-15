package com.mx.demo.dao.masterDao;

import com.mx.demo.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DataBaseDao {

    public List<Map<String, String>> findByMap(Map<String, String> map);

    public void updateUser(Map<String, String> map);

    //这里一定要用@Param,否则会报找不到getter
    public Map<String, String> checkSame(@Param("username") String username);

    public void addUser(Map<String, String> map);

    public void deleteUser(List<String> list);
}
