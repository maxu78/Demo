package com.mx.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public void getDownload(String fullPath, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        // Get your file stream from wherever.
        File downloadFile = new File(fullPath);
        ServletContext context = request.getServletContext();

        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
            logger.warn("context getMimeType is null");
        }
        logger.info("MIME type: " + mimeType);

        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        String filename =  downloadFile.getName();
        if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
            filename = URLEncoder.encode(filename, "UTF-8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
        }

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", filename);
        response.setHeader(headerKey, headerValue);

        // Copy the stream to the response's output stream.
        try {
            OutputStream os = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);

            InputStream is = null;

            is = new FileInputStream(fullPath);
            BufferedInputStream bis = new BufferedInputStream(is);

            int length = 0;
            byte[] temp = new byte[1 * 1024 * 10];

            while ((length = bis.read(temp)) != -1) {
                bos.write(temp, 0, length);
            }
            bos.flush();
            bis.close();
            bos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //单文件上传

    /**
     *
     * @param multiRequest
     * @param inputName    页面input对应的的name
     * @param path         文件存储路径  C://upload/
     * @param fileName     保存服务器上的文件名(不带后缀)
     * @return     文件保存路径
     */

    public String singleUploadFile(MultipartHttpServletRequest multiRequest, String inputName, String path, String fileName) {
        // 获取上传文件的路径
        String uploadFilePath = multiRequest.getFile(inputName).getOriginalFilename();
        judgeDirExists(getFilePath(path));
        logger.info("上传文件名:" + uploadFilePath);
        // 截取上传文件的文件名
//        String uploadFileName = uploadFilePath.substring(uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.indexOf('.'));
//        logger.info("上传文件名:" + uploadFileName);
        // 截取上传文件的后缀
        String uploadFileSuffix = uploadFilePath.substring(uploadFilePath.indexOf('.') + 1, uploadFilePath.length());
        logger.info("上传文件类型:" + uploadFileSuffix);
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fis = (FileInputStream) multiRequest.getFile(inputName).getInputStream();
            fos = new FileOutputStream(new File(path + fileName + "." + uploadFileSuffix));
            byte[] temp = new byte[1024];
            int i = fis.read(temp);
            while (i != -1){
                fos.write(temp,0,temp.length);
                fos.flush();
                i = fis.read(temp);
            }
            logger.info("文件"+uploadFilePath+"上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path+fileName+"."+uploadFileSuffix;
    }

    //多文件上传
    public List<String> multiFileUpload(MultipartHttpServletRequest request, String inputName, String path, List<String> fileNames) {
        List<String> result = new ArrayList<String>();
        judgeDirExists(getFilePath(path));
        List<MultipartFile> files = request.getFiles(inputName);
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            String uploadFileSuffix = "";
            String uploadFilePath = "";
            file = files.get(i);
            String fileName = fileNames.get(i);
            if (!file.isEmpty()) {
                try {
                    uploadFilePath = file.getOriginalFilename();
                    logger.info("上传文件名:" + uploadFilePath);
                    // 截取上传文件的文件名
//                    String uploadFileName = uploadFilePath.substring(uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.indexOf('.'));
//                    logger.info("上传文件名:" + uploadFileName);
                    // 截取上传文件的后缀
                    uploadFileSuffix = uploadFilePath.substring(uploadFilePath.indexOf('.') + 1, uploadFilePath.length());
                    logger.info("上传文件类型:" + uploadFileSuffix);
                    stream = new BufferedOutputStream(new FileOutputStream(new File(path + fileName + "." + uploadFileSuffix)));
                    byte[] bytes = file.getBytes();
                    stream.write(bytes,0,bytes.length);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (stream != null) {
                            stream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                logger.info("文件"+uploadFilePath+"上传成功");
                result.add(path+fileName+"."+uploadFileSuffix);
            } else {
                logger.warn("上传文件为空");
            }
        }
        logger.info("文件接受成功了");
        return result;
    }

    // 判断文件是否存在(全路径+文件名)
    public static void judgeFileExists(String fullPath) {
        File file = new File(fullPath);
        if (file.exists()) {
            logger.warn("文件已存在");
        } else {
            logger.info("文件不存在,创建文件...");
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    // 判断文件夹是否存在(全路径)
    public static void judgeDirExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                logger.warn("文件夹已存在");
            } else {
                logger.warn("文件夹已存在");
            }
        } else {
            logger.info("文件夹不存在,创建文件...");
            file.mkdirs();
        }

    }

    //根据路径+文件名分离出路径
    public static String getFilePath(String fullPath){
        String filePath = fullPath.substring(0, fullPath.lastIndexOf('/') + 1);
        return filePath;
    }



    public static void main(String[] args){
        System.out.println(getFilePath("E://down/abc/abc.txt"));
        //judgeFileExists("E://down/abc/abc.txt");
    }
}
