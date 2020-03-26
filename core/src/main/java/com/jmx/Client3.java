package com.jmx;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;
import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaoyiluo
 * @createTime 2018/6/9 23:40
 **/
public class Client3 {

   /* public ConcurrentHashMap<String, Object> parseJMS(String tempid, ConcurrentHashMap<String, Object> paraMap) {
        MBeanServerConnection mbsc = null;
        try {
            mbsc = connector.getMBeanServerConnection();
            // ------------------------- system ----------------------
            ObjectName runtimeObjName = new ObjectName("java.lang:type=Runtime");
            TabularDataSupport system = (TabularDataSupport) mbsc.getAttribute(runtimeObjName, "SystemProperties");
            //JVM版本
            paraMap.put(tempid + "_JVMVERSION", mbsc.getAttribute(runtimeObjName, "VmVersion"));
            //JVM厂商
            paraMap.put(tempid + "_JVMMANUFACTURER", mbsc.getAttribute(runtimeObjName, "VmVendor"));
            //系统结构
            paraMap.put(tempid + "_SYSTEMSTRUCTURE", system.get(new Object[] {"os.arch"}).get("value"));
            //操作系统
            paraMap.put(tempid + "_OPERATIONSYSTEM", system.get(new Object[] {"os.name"}).get("value"));
            //操作系统版本
            paraMap.put(tempid + "_OPERATIONSYSTEMVERSION", system.get(new Object[] {"os.version"}).get("value"));
            //Tomcat版本
            String t_version = (String) system.get(new Object[] {"org.apache.catalina.startup.TldConfig.jarsToSkip"}).get("value");
            paraMap.put(tempid + "_TOMCATVERSION", t_version == null ? "" : t_version.substring(0, t_version.indexOf("-")));
            //链接Tomcat服务器的响应时间 -------------


            ObjectName heapObjName = new ObjectName("java.lang:type=Memory");
            MemoryUsage heapMemoryUsage =  MemoryUsage.from((CompositeDataSupport)mbsc.getAttribute(heapObjName, "HeapMemoryUsage"));
            //JVM已用内存
            paraMap.put(tempid + "_JVMUSERDMEMORY", heapMemoryUsage.getUsed());
            //JVM可用内存
            paraMap.put(tempid + "_JVMAVAILABLEMEMORY", heapMemoryUsage.getCommitted() - heapMemoryUsage.getUsed());
            //JVM内存总数
            paraMap.put(tempid + "_JVMTOTALMEMORY", heapMemoryUsage.getCommitted());

            //----------------- GlobalRequestProcessor ----------------
            ObjectName requestProcessor = new ObjectName("Catalina:type=GlobalRequestProcessor,*");
            Set<ObjectName> s2 = mbsc.queryNames(requestProcessor, null);
            long bytesSents = 0;
            long bytesReceiveds = 0;
            Integer errorCounts = 0;
            Integer requestCounts = 0;
            long processingTimes = 0;
            long maxTimes = 0;
            for (ObjectName obj : s2) {
                ObjectName objname = new ObjectName(obj.getCanonicalName());
                long bytesSent = (long) mbsc.getAttribute( objname, "bytesSent");
                long bytesReceived = (long) mbsc.getAttribute( objname, "bytesReceived");
                Integer errorCount = (Integer) mbsc.getAttribute( objname, "errorCount");
                Integer requestCount = (Integer) mbsc.getAttribute( objname, "requestCount");
                long processingTime = (long) mbsc.getAttribute( objname, "processingTime");
                long maxTime = (long) mbsc.getAttribute( objname, "maxTime");
                bytesSents += bytesSent;
                bytesReceiveds += bytesReceived;
                errorCounts += errorCount;
                requestCounts += requestCount;
                processingTimes += processingTime;
                maxTimes += maxTime;
            }
            //发送字节
            paraMap.put(tempid + "_SENDBYTE", bytesSents);
            //接收字节
            paraMap.put(tempid + "_RECIEVEDBYTE", bytesReceiveds);
            //错误个数
            paraMap.put(tempid + "_ERRORNUMBER", errorCounts);
            //请求个数
            paraMap.put(tempid + "_REQUESTNUMBER", requestCounts);
            //处理时间
            paraMap.put(tempid + "_TREATMENTTIME", processingTimes);
            //最大处理时间
            paraMap.put(tempid + "_MAXTREATMENTTIME", maxTimes);

            //----------------- Thread Pool ----------------
            ObjectName threadpoolObjName = new ObjectName("Catalina:type=ThreadPool,*");
            Set<ObjectName> tp = mbsc.queryNames(threadpoolObjName, null);
            int currentThreadsBusys = 0;
            int maxThreadss = 0;
            for (ObjectName obj : tp) {
                ObjectName objname = new ObjectName(obj.getCanonicalName());
                int currentThreadsBusy = (int) mbsc.getAttribute( objname, "currentThreadsBusy");
                int maxThreads = (int) mbsc.getAttribute( objname, "maxThreads");
                currentThreadsBusys += currentThreadsBusy;
                maxThreadss += maxThreads;
            }
            //当前忙碌线程数
            paraMap.put(tempid + "_CURRENTBUSYTHREADSNUMBER", currentThreadsBusys);
            //最大线程数
            paraMap.put(tempid + "_MAXTHREADSNUMBER", maxThreadss);

            //----------------- RequestProcessor ----------------
            ObjectName requestProcessor1 = new ObjectName("Catalina:type=RequestProcessor,*");
            Set<ObjectName> rp = mbsc.queryNames(requestProcessor1, null);
            long requestProcessingTimes = 0;
            int portCount = 0;
            for (ObjectName obj : rp) {
                portCount++;
                ObjectName objname = new ObjectName(obj.getCanonicalName());
                long requestProcessingTime = (long) mbsc.getAttribute(objname, "requestProcessingTime");
                requestProcessingTimes += requestProcessingTime;
            }
            //端口平均响应时间
            paraMap.put(tempid + "_AVERAGERESPONSETIME", portCount == 0 ? requestProcessingTimes : Math.round(requestProcessingTimes/portCount));



        } catch (IOException e) {
            e.printStackTrace();
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (MBeanException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return paraMap;
    }

    public static void main(String[] args) {
        DoJob job = new DoJob();
        String tempid = "MW_WS_TOMCAT_AGENT";
        job.connector = TomcatUtil.getJMXOfTomcat("service:jmx:rmi:///jndi/rmi://191.168.2.31:8999/jmxrmi", "", "");
        ConcurrentHashMap<String, Object> paraMap = new ConcurrentHashMap<String, Object>();
        paraMap = job.parseJMS(tempid, paraMap);
        Iterator<String> it = paraMap.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            System.out.println(key + " : " + paraMap.get(key));
        }
    }*/
}
