package com.jvm;

import java.util.Arrays;
import java.util.Properties;

/**
 * test
 *
 * @author xiluo
 * @createTime 2019-09-04 16:58
 **/
public class JVMVersion {
    // -agentpath:/Applications/YourKit-Java-Profiler-2019.8.app/Contents/Resources/bin/mac/libyjpagent.dylib=delay=10000
    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperties());


        if(
        "sun.rmi.transport.tcp.TCPTransport$ConnectionHandler$$Lambda$4/0000000000000000;"
                .contains("$$Lambda$")){
            System.out.println("$$Lambda$");
        }

        while(true) {
            Properties cfgKv =new Properties();
            cfgKv.load(JVMVersion.class.getResourceAsStream("filter.cfg"));

            String filterValue = cfgKv.getProperty("classesFilter");
            String[] classes = filterValue.split(",");
            Arrays.asList(classes);

            Properties properties = System.getProperties();
            properties.list(System.out);
            Thread.sleep(5000);
        }
    }
}
