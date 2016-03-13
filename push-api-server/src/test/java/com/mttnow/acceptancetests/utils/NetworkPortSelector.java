package com.mttnow.acceptancetests.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public final class NetworkPortSelector {

  public static int fromRange(int rangeFrom, int rangeTo) throws UnreachablePortRangeException {
    if (rangeTo <= rangeFrom)
      throw new IllegalArgumentException("Invalid port range");

    for (int port = rangeFrom; port <= rangeTo; port++) {
      try {
        Socket socket = new Socket(InetAddress.getByName("localhost").getHostName(), port);
        socket.close();
      } catch (IOException e) {
         // if fail to create socket, the port is available
        return port;
      }
    }

    throw new UnreachablePortRangeException("No open ports in range");
  }

  private NetworkPortSelector() {
  }
}
