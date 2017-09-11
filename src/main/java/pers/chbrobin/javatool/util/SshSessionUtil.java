package pers.chbrobin.javatool.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/8 0008.
 */
public class SshSessionUtil {
    public static void createSshSession(String pwd, Map<String, HostPortInfo> hostPortInfoMap) throws JSchException {
        JSch jsch = new JSch();
        String sshConfigJumpHost = ConfigProperties.getProperty("ssh_config_jump_host");
        Session session = jsch.getSession("iwjw", sshConfigJumpHost, 22);
        session.setPassword(pwd);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        System.out.println("Login SSH:" + session.getServerVersion());//这里打印SSH服务器版本信息

        for(String host : hostPortInfoMap.keySet()) {
            String portInfo = ConfigProperties.getProperty(host);
            String[] portInfos = StringUtils.split(portInfo,":");
            Integer localPort = Integer.valueOf(portInfos[0]);
            Integer remotePort = Integer.valueOf(portInfos[1]);
            int assinged_port = session.setPortForwardingL("127.0.0.1", localPort, host, remotePort);//端口映射 转发
            System.out.println("127.0.0.1:" + assinged_port + "==> " + host + ":" + remotePort);
        }
    }
}
