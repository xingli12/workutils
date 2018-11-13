package com.xingli.utilTest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author xingli12
 * @projectName workutils
 * @package com.xingli.utilTest
 * @description
 * @date Created in 2018-08-08 17:59
 * @modified By
 * @updateDate
 */
public class NetWorkTest {
    public static void main(String[] args) {
        try {
            Enumeration<NetworkInterface> interfaces=null;
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresss = ni.getInetAddresses();
                while(addresss.hasMoreElements())
                {
                    InetAddress nextElement = addresss.nextElement();
                    String hostAddress = nextElement.getHostAddress();
                    System.out.println("本机IP地址为：" +hostAddress);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
