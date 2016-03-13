package com.mttnow.acceptancetests.utils;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import static java.lang.System.getProperty;

public final class JettyStarter {

  private static Server server;

  public static void start(int port, String contextPath, String contextResourceBase) throws Exception {
    if (server == null) {
      server = new Server(port);
      WebAppContext context = new WebAppContext();
      context.setDescriptor(contextResourceBase + "/WEB-INF/web.xml");
      context.setResourceBase(contextResourceBase);
      context.setContextPath("/" + contextPath);
      context.setParentLoaderPriority(true);
      server.setHandler(context);
    }

    if (server.isStopped()) {
      server.start();
    }
  }

  public static boolean isStarted() {
    return server.isStarted();
  }

  public static boolean stop() throws Exception {
    if (server.isRunning()){
      server.stop();
    }
    return server.isStopped();
  }

  public static void main(String[] args) throws Exception {
    JettyStarter.start(18080, getProperty("acceptance.tests.context"), getProperty("acceptance.tests.war.dir"));
    if (server.isStarted()){
      server.join();
    }
  }

}
