package mma.dao.factory;

import mma.dao.GroupDao;
import mma.dao.GroupMessageDao;
import mma.dao.factory.impl.MockDaoFactoryImpl;

public abstract class DaoFactory {
    public static final DaoFactory getInstance() {
        return InstanceProvider.INSTANCE;
    }

    private static final class InstanceProvider {
        private static final DaoFactory INSTANCE = new MockDaoFactoryImpl();
    }

    public abstract GroupMessageDao getGroupMessageDao();

    public abstract GroupDao getGroupDao();
}
