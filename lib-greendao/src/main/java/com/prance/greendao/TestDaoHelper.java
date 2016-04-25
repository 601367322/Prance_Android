package com.prance.greendao;

import android.text.TextUtils;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * DaoHelper简单示例
 * Created by shenbingbing on 16/4/18.
 */
public class TestDaoHelper implements THDaoHelperInterface {
    private static TestDaoHelper instance;
    private TestBeanDao testBeanDao;

    private TestDaoHelper() {
        try {
            testBeanDao = MyDatabaseLoader.getDaoSession().getTestBeanDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TestDaoHelper getInstance() {
        if(instance == null) {
            instance = new TestDaoHelper();
        }

        return instance;
    }

    @Override
    public <T> void addData(T bean) {
        if(testBeanDao != null && bean != null) {
            testBeanDao.insertOrReplace((TestBean) bean);
        }
    }

    @Override
    public void deleteData(String id) {
        if(testBeanDao != null && !TextUtils.isEmpty(id)) {
            testBeanDao.deleteByKey(id);
        }
    }

    @Override
    public TestBean getDataById(String id) {
        if(testBeanDao != null && !TextUtils.isEmpty(id)) {
            return testBeanDao.load(id);
        }
        return null;
    }

    @Override
    public List getAllData() {
        if(testBeanDao != null) {
            return testBeanDao.loadAll();
        }
        return null;
    }

    @Override
    public boolean hasKey(String id) {
        if(testBeanDao == null || TextUtils.isEmpty(id)) {
            return false;
        }

        QueryBuilder<TestBean> qb = testBeanDao.queryBuilder();
        qb.where(TestBeanDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0 ? true : false;
    }

    @Override
    public long getTotalCount() {
        if(testBeanDao == null) {
            return 0;
        }

        QueryBuilder<TestBean> qb = testBeanDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if(testBeanDao != null) {
            testBeanDao.deleteAll();
        }
    }

    public void testQueryBy() {
        /*List joes = testBeanDao.queryBuilder()
                .where(TestBeanDao.Properties.Phone.eq("Joe"))
                .orderAsc(TestBeanDao.Properties.Phone)
                .list();

        QueryBuilder<TestBean> qb = testBeanDao.queryBuilder();
        qb.where(qb.or(TestBeanDao.Properties.Phone.gt(10698.85),
                qb.and(TestBeanDao.Properties.Phone.eq("id"),
                        TestBeanDao.Properties.Phone.eq("xx"))));

        qb.orderAsc(TestBeanDao.Properties.Id);// 排序依据
        qb.list();*/
    }
}