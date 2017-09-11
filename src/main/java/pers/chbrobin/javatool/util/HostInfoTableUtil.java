package pers.chbrobin.javatool.util;

import java.util.Map;

/**
 * Created by Administrator on 2017/6/8 0008.
 */
public class HostInfoTableUtil {
    public static String genHtml(Map<String, HostPortInfo> hostInfoMap) {
        StringBuffer sb = new StringBuffer();
        sb.append("");
        for(String host : hostInfoMap.keySet()) {
            HostPortInfo hostPortInfo = hostInfoMap.get(host);
            Integer localPort = hostPortInfo.getLocalPort();
            Integer remotePort = hostPortInfo.getRemotePort();
            sb.append("<table width='100%'>\n");
            sb.append("<tr>\n");
            sb.append("<td width='100%'>127.0.0.1:"+ localPort +"==>" + host + ":"+remotePort+"</td>\n");
            sb.append("</tr>\n");
            sb.append("</table><br/><br/>\n");
        }
        return sb.toString();
    }
}
