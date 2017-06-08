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
            sb.append("<form method=post target=\"_blank\" id=\"proxyForm_"+host+"\">\r\n");
            sb.append("<input type=\"hidden\" name=\"host\" value=\""+hostInfoMap.get(host)+"\">\n");
            sb.append("<table width='100%'>\n");
            sb.append("<tr>\n");
            sb.append("<td width='10%'>host</td>\n");
            sb.append("<td width='90%'>"+ hostInfoMap.get(host) +"==>" + host + ":80</td>\n");
            sb.append("</tr>\n");
            sb.append("<tr><td>action</td><td><input type=\"text\" name=\"action\" value=\"\" style=\"width:400px;\"/></td></tr>\n");
            sb.append("<tr><td>param</td><td><textarea name=\"param\" style=\"width:400px;height:40px;\"></textarea></td></tr>\n");
            sb.append("<tr><td>operate</td><td><input type=\"button\" class=\"submitForm\" data-host=\"" + host + "\" value=\"execute\"></td></tr>\n");
            sb.append("<tr><td>result</td><td><span id=\"result_"+host+"\"></span></td></tr>\n");
            sb.append("</table></form><br/><br/>\n");
        }
        return sb.toString();
    }
}
