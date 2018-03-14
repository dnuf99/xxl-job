package com.xxl.job.adminbiz;

import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.ShutdownBiz;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.RegistryConfig;
import com.xxl.job.core.rpc.netcom.NetComClientProxy;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * admin api test
 *
 * @author xuxueli 2017-07-28 22:14:52
 */
public class ShutdownBizTest {

    // admin-client
    private static String addressUrl = "http://10.50.115.73:9999";
    private static String accessToken = "htf-dev-token";

    /**
     * shutdown executor
     *
     * @throws Exception
     */
    @Test
    public void registryTest() throws Exception {
        ShutdownBiz shutdownBiz = (ShutdownBiz) new NetComClientProxy(ShutdownBiz.class, addressUrl, accessToken).getObject();

        // test shutdownBiz
        ReturnT<String> returnT = shutdownBiz.shutdownAndWaitingExcutor();
        Assert.assertTrue(returnT.getCode() == ReturnT.SUCCESS_CODE);
    }



}
