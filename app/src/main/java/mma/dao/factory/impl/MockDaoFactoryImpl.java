package mma.dao.factory.impl;

import mma.dao.GroupDao;
import mma.dao.GroupMessageDao;
import mma.dao.factory.DaoFactory;
import mma.dao.impl.mock.MockGroupDaoImpl;
import mma.dao.impl.mock.MockGroupMessageDaoImpl;

public class MockDaoFactoryImpl extends DaoFactory {

    @Override
    public GroupMessageDao getGroupMessageDao() {
        return MockGroupMessageDaoImpl.getInstance();
    }

    @Override
    public GroupDao getGroupDao() {return MockGroupDaoImpl.getInstance(); };
}
