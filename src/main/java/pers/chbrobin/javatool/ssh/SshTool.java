package pers.chbrobin.javatool.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import pers.chbrobin.javatool.socket.SimpleHttpServer;
import pers.chbrobin.javatool.util.ConfigProperties;
import pers.chbrobin.javatool.util.FileUtil;
import pers.chbrobin.javatool.util.HostInfoTableUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/7 0007.
 */
public class SshTool {
    public static void main(String[] args) {
        if(args == null || args.length < 1) {
            System.out.println("请输入跳转机密码");
        }

        List<String> hosts = ConfigProperties.getPropertyNames();
        Map<String, String> hostInfoMap = new HashedMap();
        for(String host : hosts) {
            if (host.startsWith("ssh_config_")) {
                continue;
            }
            hostInfoMap.put(host,"http://127.0.0.1:" + ConfigProperties.getProperty(host));
        }
        String serverHtml = FileUtil.readFileString(SshTool.class.getClassLoader().getResourceAsStream("server.html"));
        String hostInfoTableHtml = HostInfoTableUtil.genHtml(hostInfoMap);
        serverHtml = serverHtml.replaceAll("XXXXXXXX", hostInfoTableHtml);
        // 开启8080浏览器
        SimpleHttpServer simpleHttpServer = new SimpleHttpServer(serverHtml);
        new Thread(simpleHttpServer).start();

        // 打开浏览器
        try {
//            Runtime.getRuntime().exec("");
//             Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://127.0.0.1/");
        } catch (Exception e) {
            e.printStackTrace();
        }

        aa(args[0]);
    }

    public static void aa(String pwd) {
        try {

            JSch jsch = new JSch();
            String sshConfigJumpHost = ConfigProperties.getProperty("ssh_config_jump_host");
            Session session = jsch.getSession("iwjw", sshConfigJumpHost, 22);
            session.setPassword(pwd);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            System.out.println("Login SSH:" + session.getServerVersion());//这里打印SSH服务器版本信息

            List<String> hosts = ConfigProperties.getPropertyNames();
            for(String host : hosts) {
                if(host.startsWith("ssh_config_")) {
                    continue;
                }
                Integer port = Integer.valueOf(ConfigProperties.getProperty(host));
                int assinged_port = session.setPortForwardingL("127.0.0.1", port, host, 80);//端口映射 转发
                System.out.println("127.0.0.1:" + assinged_port + "==> " + host + ":80");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
