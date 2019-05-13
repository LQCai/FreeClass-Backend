package cn.starchild.common.util;

import java.io.*;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
    public final static int FILE_TYPE_HOMEWORK_ANNEX = 0;
    public final static int FILE_TYPE_ANNOUNCEMENT_ANNEX = 1;
    public final static int FILE_TYPE_ARTICLE_IMAGE = 2;
    public final static int FILE_TYPE_TOILET = 3;
    public final static int FILE_TYPE_SKIL_BAG = 4;
    public final static int FILE_TYPE_USER_OPINION = 5;
    public static String homeworkAnnex = "/home/lqcai/nginx-image/freeClass/homeworkAnnex";
    public static String announcementAnnex = "/home/lqcai/nginx-image/freeClass/announcementAnnex";
    public static String homeworkSubmitCommonUrl = "/home/lqcai/nginx-image/freeClass/homeworkSubmit/";
    public static String articleImage = "/home/lqcai/nginx-image/freeClass/articleImage/";
    //    public static String homeworkAnnex = "C://Users/LQCai/freeClass/upload/homeworkAnnex";
    public final static String HOMEWORK_ANNEX_DOMAIN = "http://www.starchild.cn/freeClass/homeworkAnnex/";
    public final static String HOMEWORK_SUBMIT_COMMON_DOMAIN = "http://www.starchild.cn/freeClass/homeworkSubmit/";
    public final static String ANNOUNCEMENT_ANNEX_DOMAIN = "http://www.starchild.cn/freeClass/announcementAnnex/";
    public final static String ARTICLE_IMAGE_DOMAIN = "http://www.starchild.cn/freeClass/articleImage/";

    /**
     * 文件名加UUID
     *
     * @param filename
     * @return
     */
    public static String makeFileName(String filename) {
        return UUID.randomUUID().toString() + "_" + filename;
    }

    /**
     * 文件名特殊字符过滤
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*+=|{}':; ',//[//]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    /**
     * @param file     上传的文件
     * @param type     存储的位置  FileUtils.
     * @param fileName 上传后的文件名.
     * @return 文件名
     */
    public static String saveFile(MultipartFile file, int type, String fileName) {
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."), originalFileName.length());
        fileName = fileName + "_" + RandomUtils.getRandomStr(5) + suffix;
        String uploadPath = null;
        switch (type) {
            case FILE_TYPE_HOMEWORK_ANNEX:
                uploadPath = homeworkAnnex;
                break;
            case FILE_TYPE_ANNOUNCEMENT_ANNEX:
                uploadPath = announcementAnnex;
                break;
            case FILE_TYPE_ARTICLE_IMAGE:
                uploadPath = articleImage;
                break;
//            case FILE_TYPE_FIRMWAVE:
//                uploadPath = firmwaveUrlUplaod;
//                break;
//            case FILE_TYPE_SKIL_BAG:
//                uploadPath = skilBagUrlUplaod;
//                break;
//            case FILE_TYPE_TOILET:
//                uploadPath = toiletUrlUplaod;
//                break;
//            case FILE_TYPE_USER_ICON:
//                uploadPath = userIconUrlUpload;
//                break;
//            case FILE_TYPE_USER_OPINION:
//                uploadPath = userOpinionUrlUpload;
//                break;
            default:
                return "";
        }
        File file1 = new File(uploadPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        try {
            FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(uploadPath + File.separator + fileName));
            byte[] bs = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bs)) != -1) {
                bos.write(bs, 0, len);
            }
            bos.flush();
            bos.close();

            Runtime.getRuntime().exec("chmod -R 777 /home/lqcai/nginx-image/freeClass");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }


    public static String submitHomework(MultipartFile file, String classId, String homeworkId, String fileName) {
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."), originalFileName.length());
        fileName = fileName + suffix;
        String uploadPath = homeworkSubmitCommonUrl + classId + "/" + homeworkId;
        File file1 = new File(uploadPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        try {
            FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(uploadPath + File.separator + fileName));
            byte[] bs = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bs)) != -1) {
                bos.write(bs, 0, len);
            }
            bos.flush();
            bos.close();

            Runtime.getRuntime().exec("chmod -R 777 /home/lqcai/nginx-image/freeClass");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    /**
     * @param metaName
     * @return
     */
    public static String getFileSuffix(String metaName) {
        String[] split = metaName.split("//.");
        return "." + split[split.length - 1];
    }

}