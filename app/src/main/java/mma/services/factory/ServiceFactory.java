package mma.services.factory;

import mma.services.GroupMessageService;
import mma.services.GroupService;
import mma.services.impl.GroupMessageServiceImpl;
import mma.services.impl.GroupServiceImpl;

public  class ServiceFactory {
    private ServiceFactory(){}
    public static ServiceFactory getInstance() {
        return ServiceFactory.InstanceProvider.INSTANCE;
    }
    private static final  class InstanceProvider {
        private static final ServiceFactory INSTANCE = new ServiceFactory();
    }
    public GroupMessageService getGroupMessageService() {
        return GroupMessageServiceImpl.getInstance();
    }

    public GroupService getGroupService() {
        return GroupServiceImpl.getInstance();
    }
}
