package com.prance.greendao;

import java.util.List;

/**
 * 通用dao接口
 * Created by shenbingbing on 16/4/18.
 */
public interface THDaoHelperInterface {

    public <T> void addData(T t);
    public void deleteData(String id);
    public <T> T getDataById(String id);
    public List getAllData();
    public boolean hasKey(String id);
    public long getTotalCount();
    public void deleteAll();

}
