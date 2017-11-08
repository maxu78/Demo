package com.mx.demo.dao.DataBaseDao;

import java.util.List;
import java.util.Map;

public interface DataBaseDao {

    public List<Map<String, String>> dataBaseQuery(Map<String, String> map) throws Exception ;
}
