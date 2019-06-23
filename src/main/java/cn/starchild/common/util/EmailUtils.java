package cn.starchild.common.util;

import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailUtils {
    private final static String EMAIL_FROM = "sc.lqcai@qq.com";
    private final static String EMAIL_AUTH_CODE = "exfvmweycimebehf";
    private final static String EMAIL_PROTOCOL = "smtp";
    private final static String EMAIL_SMTP_HOST = "smtp.qq.com";
    private final static String EMAIL_SMTP_PORT = "465";
    private final static String EMAIL_SMTP_AUTH = "true";

    private Logger logger = Logger.getLogger(this.getClass());


    /**
     * 发送消息邮件
     * @param to
     * @param subject
     * @param msg
     * @return
     */
    public boolean send(String to, String subject, String msg) {

        Properties props = new Properties();
        //邮件传输的协议
        props.put("mail.transport.protocol", EMAIL_PROTOCOL);
        //连接的邮件服务器
        props.put("mail.host", EMAIL_SMTP_HOST);
        //发送人
        props.put("mail.from", EMAIL_FROM);

        //第一步：创建session
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", "true");
            //第二步：获取邮件传输对象
            Transport ts = session.getTransport();
            //连接邮件服务器
            ts.connect(EMAIL_FROM, EMAIL_AUTH_CODE);
            //第三步：创建邮件消息体
            MimeMessage message = new MimeMessage(session);
            //设置邮件的内容
            message.setSubject(subject);
            //设置邮件的内容
            message.setContent(msg, "text/html;charset=utf-8");
            //第四步：设置发送昵称
            String nick = "";
            nick = javax.mail.internet.MimeUtility.encodeText("Free课堂");
            message.setFrom(new InternetAddress(nick + "<" + EMAIL_FROM + ">"));
            //第五步：设置接收人信息
            ts.sendMessage(message, InternetAddress.parse(to));

        } catch (Exception e) {
            logger.error("发送邮件失败:" + e.getMessage());
            return false;
        }

        return true;

    }

    /**
     * 发送附件邮件
     * @param to
     * @param subject
     * @param msg
     * @param annex
     * @return
     */
    public boolean sendWithFile(String to, String subject, String msg, File annex) {

        Properties props = new Properties();
        //邮件传输的协议
        props.put("mail.transport.protocol", EMAIL_PROTOCOL);
        //连接的邮件服务器
        props.put("mail.host", EMAIL_SMTP_HOST);
        //发送人
        props.put("mail.from", EMAIL_FROM);

        //第一步：创建session
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", "true");
            //第二步：获取邮件传输对象
            Transport ts = session.getTransport();
            //连接邮件服务器
            ts.connect(EMAIL_FROM, EMAIL_AUTH_CODE);
            //第三步：创建邮件消息体
            MimeMessage message = new MimeMessage(session);
            //设置邮件的内容
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();

            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(msg, "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);

            // 添加附件
            if (annex != null) {
                BodyPart annexBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(annex);
                annexBodyPart.setDataHandler(new DataHandler(source));

                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");

                //MimeUtility.encodeWord可以避免文件名乱码
                annexBodyPart.setFileName(MimeUtility.encodeWord(annex.getName()));
                multipart.addBodyPart(annexBodyPart);
            }

            //设置邮件的内容
            message.setContent(multipart);
            message.saveChanges();

            //第四步：设置发送昵称
            String nick = "";
            nick = javax.mail.internet.MimeUtility.encodeText("Free课堂");
            message.setFrom(new InternetAddress(nick + "<" + EMAIL_FROM + ">"));
            //第五步：设置接收人信息
            ts.sendMessage(message, InternetAddress.parse(to));

        } catch (Exception e) {
            logger.error("发送邮件失败:" + e.getMessage());
            return false;
        }

        return true;

    }

    public static void main(String[] args) {
        EmailUtils emailUtils = new EmailUtils();
        File annex = new File("C:/Users/LQCai/freeClass/upload.zip");
        emailUtils.sendWithFile("1696432754@qq.com", "一封信", "恭喜测试成功！哈哈哈", annex);
    }
}
