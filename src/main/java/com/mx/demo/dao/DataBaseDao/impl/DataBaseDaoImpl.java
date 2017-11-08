package com.mx.demo.dao.DataBaseDao.impl;

import com.mx.demo.dao.DAOSupport;
import com.mx.demo.dao.DataBaseDao.DataBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DataBaseDaoImpl extends DAOSupport implements DataBaseDao {

    @Override
    public List<Map<String, String>> dataBaseQuery(Map<String, String> map) throws Exception {
        System.out.println(findForList("dataBaseDao.findByMap", map));
        return (List<Map<String, String>>) findForList("dataBaseDao.findByMap", map);
    }

}
