package com.chinapalms.kwobox.video;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinapalms.kwobox.utils.ResponseStatus;

public class VideoService {
    Log log = LogFactory.getLog(VideoService.class);

    public String doUploadVideo(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String startUploadTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(System.currentTimeMillis()));
            log.info("doUploadLog:->startUploadTime=" + startUploadTime);
            // 获取文件需要上传到的路径
            // String path = request.getRealPath("/upload");
            // String path = Thread.currentThread().getContextClassLoader()
            // .getResource("/").getPath();
            String path = request.getSession().getServletContext()
                    .getRealPath("/")
                    + "../"
                    + "ROOT"
                    + File.separator
                    + "videos"
                    + File.separator + "monitorVideos";
            log.info(path);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 获得磁盘文件条目工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // 如果没以下两行设置的话,上传大的文件会占用很多内存，
            // 设置暂时存放的存储室,这个存储室可以和最终存储文件的目录不同
            /**
             * 原理: 它是先存到暂时存储室，然后再真正写到对应目录的硬盘上， 按理来说当上传一个文件时，其实是上传了两份，第一个是以 .tem
             * 格式的 然后再将其真正写到对应目录的硬盘上
             */
            factory.setRepository(dir);
            // 设置缓存的大小，当上传文件的容量超过该缓存时，直接放到暂时存储室
            factory.setSizeThreshold(1024 * 1024);
            // 高水平的API文件上传处理
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> list = upload.parseRequest(request);
            List<FileItem> videoList = new ArrayList<FileItem>();
            FileItem videoFile = null;
            for (FileItem item : list) {
                // 获取表单的属性名字
                String name = item.getFieldName();
                // 如果获取的表单信息是普通的 文本 信息
                if (item.isFormField()) {
                    // 获取用户具体输入的字符串
                    String value = item.getString();
                    request.setAttribute(name, value);
                } else {
                    videoFile = item;
                    videoList.add(videoFile);
                }
            }

            // 自定义上传图片的名字为userId.jpg
            String boxId = request.getAttribute("boxId").toString();
            String startTime = request.getAttribute("startTime").toString();
            String endTime = request.getAttribute("endTime").toString();
            path = path + File.separator + boxId;
            path = path
                    + File.separator
                    + new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .parse(startTime))
                    + "-"
                    + new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .parse(endTime));

            dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileItem rawDataLogFile = null;
            for (int i = 0; i < videoList.size(); i++) {
                rawDataLogFile = videoList.get(i);
                String filePre = rawDataLogFile.getName().substring(0,
                        rawDataLogFile.getName().lastIndexOf("."));
                String fileSuffix = rawDataLogFile.getName().substring(
                        rawDataLogFile.getName().lastIndexOf("."));

                // 判断文件中是否包含.ignore 可以忽略的文件,如果包含则直接忽略
                if (fileSuffix != null && fileSuffix.endsWith("ignore")) {
                    continue;
                }

                String fileName = filePre + fileSuffix;
                String destPath = path + File.separator + fileName;

                // 真正写到磁盘上
                File file = new File(destPath);
                OutputStream out = new FileOutputStream(file);
                InputStream in = rawDataLogFile.getInputStream();
                int length = 0;
                byte[] buf = new byte[1024];
                // in.read(buf) 每次读到的数据存放在buf 数组中
                while ((length = in.read(buf)) != -1) {
                    // 在buf数组中取出数据写到（输出流）磁盘上
                    out.write(buf, 0, length);
                }
                in.close();
                in = null;
                out.close();
                out = null;
            }
            return ResponseStatus.SUCCESS;
        } catch (Exception e) {
            log.info("doUploadLog exception;->" + e.toString());
            log.error("try->catch exception:", e);
            e.printStackTrace();
        }
        return ResponseStatus.FAIL;
    }

}
