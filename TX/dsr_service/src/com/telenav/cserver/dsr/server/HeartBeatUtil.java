package com.telenav.cserver.dsr.server;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User: llzhang
 * Date: 2010-4-21
 * Time: 14:12:38
 */
public class HeartBeatUtil {
    public static String getHeartbeatXml() {
        StringBuilder buf = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        buf.append("<admin xmlns=\"http://www.telenavtrack.net/schema\" \n")
                .append("       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n")
                .append("       xsi:schemaLocation=\"http://www.telenavtrack.net/schema monitor.xsd\">\n");
        buf.append(serverXml());
        buf.append(JVMStatusXml());
        buf.append("</admin>");
        return buf.toString();
    }

    private static String serverXml() {
        List<NameValue> list = new ArrayList<NameValue>();
        list.add(new NameValue("Server", "OK"));
        return composeStatusXml("ServerStatus", list);
    }

    private static String JVMStatusXml() {
        Runtime runtime = Runtime.getRuntime();
        double maxMemory = runtime.maxMemory() / (1.0 * 1024 * 1024);
        double freeMemory = runtime.freeMemory() / (1.0 * 1024 * 1024);
        double totalMemory = runtime.totalMemory() / (1.0 * 1024 * 1024);
        List<NameValue> list = new ArrayList<NameValue>();
        list.add(new NameValue("Free memory", getMemorySize(freeMemory)));
        list.add(new NameValue("Total memory", getMemorySize(totalMemory)));
        list.add(new NameValue("Max memory", getMemorySize(maxMemory)));
        return composeStatusXml("JVMStatus", list);
    }

    private static String getMemorySize(double size) {
        final DecimalFormat FMT = new DecimalFormat("0.#### Mega Bytes");
        return FMT.format(size);
    }

    private static String composeStatusXml(String group, List<NameValue> contentList) {
        StringBuilder buf = new StringBuilder("  <status group=\"");
        buf.append(group).append("\">\n");
        if (contentList != null) {
            for (NameValue nv : contentList) {
                buf.append("    <name>").append(nv.name).append("</name>\n");
                buf.append("    <value>").append(nv.value).append("</value>\n");
            }
        }
        buf.append("  </status>\n");
        return buf.toString();
    }

    static class NameValue {
        public String name;
        public String value;

        public NameValue(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
