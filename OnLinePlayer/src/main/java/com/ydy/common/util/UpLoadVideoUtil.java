package com.ydy.common.util;

import com.ydy.sys.entity.UpLoadVideoInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

/**
 * 视频上传工具类
 *
 * @author dengyan.yao
 */
public class UpLoadVideoUtil {

    //应许上传的最大容量5G
    private static long upload_maxsize = 5120 * 1024 * 1024;

    // 文件允许格式
    private static String[] allowFiles = {".rar", ".doc", ".docx", ".zip",
            ".pdf", ".txt", ".swf", ".xlsx", ".gif", ".png", ".jpg", ".jpeg",
            ".bmp", ".xls", ".mp4", ".flv", ".ppt", ".avi", ".mpg", ".wmv",
            ".3gp", ".mov", ".asf", ".asx", ".vob", ".wmv9", ".rm", ".rmvb"};
    // 允许转码的视频格式（ffmpeg）
    private static String[] allowFLV = {".avi", ".mpg", ".wmv", ".3gp", ".mov", ".asf", ".asx", ".vob"};
    // 允许的视频转码格式(mencoder)
    private static String[] allowAVI = {".wmv9", ".rm", ".rmvb"};


    public UpLoadVideoInfo createFile(String logoPathDir, MultipartFile multipartFile, HttpServletRequest request) {
        UpLoadVideoInfo entity = new UpLoadVideoInfo();
        boolean bflag = false;
        //获取上次文件名
        String fileName = multipartFile.getOriginalFilename().toString();
        System.out.println("文件全称：" + fileName + "~~~~~~~~~~~~~~");
        // 判断文件不为空
        if (multipartFile.getSize() != 0 && !multipartFile.isEmpty()) {
            bflag = true;
            // 判断文件大是否超出
            if (multipartFile.getSize() <= upload_maxsize) {
                bflag = true;
                // 文件类型判断
                if (this.checkFileType(fileName)) {
                    bflag = true;
                } else {
                    bflag = false;
                    System.out.println("文件类型不允许");
                }
            } else {
                bflag = false;
                System.out.println("文件大小超范围");
            }
        } else {
            bflag = false;
            System.out.println("文件为空");
        }
        if (bflag) {
            //String logoPathDir = "/video/";
            //String logoRealPathDir = request.getSession().getServletContext().getRealPath(logoPathDir);
            /*String logoRealPathDir = "E:/upload";*/
            //文件保存的文件夹地址
            //String logoRealPathDir = "D:\\Videos";
            // 上传到本地磁盘
            String logoPathDir1 = "static\\video";
            System.out.println("全路径不加项目名：" + logoPathDir1 + "*************************");

            String logoRealPathDir = request.getSession().getServletContext().getRealPath(logoPathDir1);
            //创建保存文件的地址file对象
            File logoSaveFile = new File(logoRealPathDir);
            //如果不存在，创建他
            if (!logoSaveFile.exists()) {
                logoSaveFile.mkdirs();
            }
            //截取扩展名前面的文件名称
            String name = fileName.substring(0, fileName.lastIndexOf("."));
            System.out.println("文件名称：" + name);
            // 新的文件名（UUID+文件名称）
            String newFileName = this.getName(name);
            // 文件扩展名
            String fileEnd = this.getFileExt(fileName);
            // 绝对路径
            String fileNamedirs = logoRealPathDir + File.separator + newFileName + fileEnd;
            System.out.println("这是啥：fileEnd：" + fileEnd);
            System.out.println("这是啥：File.separator：" + File.separator);
            System.out.println("保存的绝对路径：" + fileNamedirs);
            //创建上传文件的file
            File filedirs = new File(fileNamedirs);
            // 转入文件
            try {
                //将文件内容写入指定文件下
                multipartFile.transferTo(filedirs);
                //将文件格式转换为MP4

            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //设置文件的扩展名
            entity.setType(fileEnd);
            // 相对路径
            String fileDir = logoPathDir1 + File.separator + newFileName + fileEnd;
            /*StringBuilder builder = new StringBuilder(fileDir);
            String finalFileDir = builder.substring(1);*/
            // size存储为String
            String size = this.getSize(filedirs);
            // 源文件保存路径
            String aviPath = filedirs.getAbsolutePath();
            if (aviPath != null) {
                //设置文件大小
                entity.setSize(size);
                entity.setPath(fileDir);
                entity.setTitleOrig(name);
                entity.setTitleAlter(newFileName);
                //设置相对路径
                entity.setFileNamedirs(fileDir);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                entity.setUploadTime(timestamp);
                return entity;
            }
        } else {
            return null;
        }
        return entity;


    }


    /**
     * 文件类型判断
     *
     * @param fileName
     * @return
     */
    private boolean checkFileType(String fileName) {
        Iterator<String> type = Arrays.asList(allowFiles).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            //判断文件名知否以指定类型结尾
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }


        return false;
    }


    /**
     * 视频类型判断(flv)
     *
     * @param fileEnd
     * @return
     */
    private boolean checkMediaType(String fileEnd) {
        //通过数组创建一个集合，返回集合的迭代器对象
        Iterator<String> iter = Arrays.asList(allowFLV).iterator();
        //迭代集合，查询filename是否符合集合中一种
        while (iter.hasNext()) {
            String ext = iter.next();
            if (fileEnd.equals(ext)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 视频类型判断(AVI)
     *
     * @param fileEnd
     * @return
     */
    private boolean checkAVIType(String fileEnd) {
        Iterator<String> type = Arrays.asList(allowAVI).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileEnd.equals(ext)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取文件扩展名
     *
     * @return string
     */
    private String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    /**
     * 依据原始文件名生成新文件名
     * UUID：全局唯一标识符，由一个十六位的数字组成,由三部分组成：当前日期和时间、时钟序列、全局唯一的IEEE机器识别号
     *
     * @return string
     */
    private String getName(String fileName) {
        Random random = new Random();
        /*return "" + random.nextInt(10000) + System.currentTimeMillis();*/
        return UUID.randomUUID().toString() + "_" + fileName;


    }


    /**
     * 文件大小，返回kb.mb
     *  
     *
     * @return
     */
    private String getSize(File file) {
        String size = "";
        long fileLength = file.length();
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileLength < 1024) {
            size = df.format((double) fileLength) + "BT";
        } else if (fileLength < 1048576) {
            size = df.format((double) fileLength / 1024) + "KB";
        } else if (fileLength < 1073741824) {
            size = df.format((double) fileLength / 1048576) + "MB";
        } else {
            size = df.format((double) fileLength / 1073741824) + "GB";
        }


        return size;


    }


}
