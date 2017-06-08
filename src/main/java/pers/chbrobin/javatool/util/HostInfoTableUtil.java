package pers.chbrobin.javatool.util;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/8 0008.
 */
public class HostInfoTableUtil {
    public static String genHtml(Map<String, String> hostInfoMap) {
        StringBuffer sb = new StringBuffer();
        sb.append("");
        for(String host : hostInfoMap.keySet()) {
            sb.append("<form method=post target=\"_blank\" id=\"proxyForm_"+host+"\">");
            sb.append("<input type=\"hidden\" name=\"host\" value=\""+hostInfoMap.get(host)+"\">");
            sb.append("<table><tr><td>host</td><td>");
            sb.append(hostInfoMap.get(host));
            sb.append("==>");
            sb.append(host);
            sb.append(":80");
            sb.append("</td></tr>");
            sb.append("<tr><td>action</td> <td><input type=\"text\" name=\"action\" value=\"\" width=\"200px;\"/></td></tr>");
            sb.append("<tr><td>param</td><td><textarea name=\"param\" rows=\"5\"></textarea></td></tr>");
            sb.append("<tr><td>operate</td><td><input type=\"button\" class=\"submitForm\" data-host=" + host + " value=\"execute\"></td></tr>");
            sb.append("<tr><td>result</td><td><span id=\"result\"></span></td></tr>");
            sb.append("</table></from><br/><br/>");
        }
        return sb.toString();
    }
}
