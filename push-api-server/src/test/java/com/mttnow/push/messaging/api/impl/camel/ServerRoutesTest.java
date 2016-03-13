package com.mttnow.push.messaging.api.impl.camel;

import org.junit.Test;

public class ServerRoutesTest {
    @Test
    public void testConfigure() throws Exception {
        ServerRoutes routes = new ServerRoutes();
        routes.configure();
        //TODO experiment on what can be asserted on the configuration

    }
}
