package pers.chbrobin.javatool.tool;

import com.jcraft.jsch.JSchException;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import pers.chbrobin.javatool.socket.SimpleHttpServer;
import pers.chbrobin.javatool.util.*;

import java.io.Console;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/7 0007.
 */
public class SshTool {
    public static void main(String[] args) {
        // get password from terminal
        String sshConfigEnv = ConfigProperties.getProperty("ssh_config_env");
        String password = null;
        if("dev".equals(sshConfigEnv)) {
            if(args == null || args.length < 1) {
                System.out.println("please set password params in IDE");
                System.exit(0);
            }
            password = args[0];
        } else {
            Console console = System.console();
            if (console == null) {
                System.out.println("Couldn't get Console instance, maybe you're running this in IDE");
                System.exit(0);
            }
            char passwordArray[] = console.readPassword("please enter your password: ");
            password = new String(passwordArray);
        }

        // fetch port info
        List<String> hosts = ConfigProperties.getPropertyNames();
        Map<String, HostPortInfo> hostPortInfoMap = new HashMap<String, HostPortInfo>();
        for(String host : hosts) {
            if (host.startsWith("ssh_config_")) {
                continue;
            }
            String[] portInfos = StringUtils.split(ConfigProperties.getProperty(host),":");
            HostPortInfo hostPortInfo = new HostPortInfo();
            hostPortInfo.setLocalPort(Integer.valueOf(portInfos[0]));
            hostPortInfo.setRemotePort(Integer.valueOf(portInfos[1]));
            hostPortInfoMap.put(host, hostPortInfo);
        }

        // create ssh session
        try {
            SshSessionUtil.createSshSession(password, hostPortInfoMap);
        } catch (JSchException e) {
            e.printStackTrace();
            System.out.println("create ssh session error");
            System.exit(0);
        }

        // fetch ssh tunnel config
        String port = ConfigProperties.getProperty("ssh_config_http_server_port");
        String serverIndexHtml = FileUtil.readFileString(SshTool.class.getClassLoader().getResourceAsStream("server.html"));
        String hostInfoTableHtml = HostInfoTableUtil.genHtml(hostPortInfoMap);
        serverIndexHtml = serverIndexHtml.replaceAll("XXXXXXXX", hostInfoTableHtml);
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        serverIndexHtml = serverIndexHtml.replaceAll("########", pid);

        // create http web server
        SimpleHttpServer simpleHttpServer = new SimpleHttpServer(Integer.valueOf(port), serverIndexHtml);
        new Thread(simpleHttpServer).start();

        // open web browser
        try {
            String os = System.getProperty("os.name");
            String portAppendString = (port == null || "80".equals(port)) ? "" : ":" + port;
            if(os.startsWith(EPlatform.Windows.name())) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://127.0.0.1"+portAppendString+"/");
            } else {
                System.out.println("you can manage by web browser, please enter: http://127.0.0.1"+portAppendString+"/");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}