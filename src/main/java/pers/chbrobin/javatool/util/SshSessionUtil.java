package pers.chbrobin.javatool.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.List;

/**
 * Created by Administrator on 2017/6/8 0008.
 */
public class SshSessionUtil {
    public static void createSshSession(String pwd) throws JSchException {
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
    }
}
