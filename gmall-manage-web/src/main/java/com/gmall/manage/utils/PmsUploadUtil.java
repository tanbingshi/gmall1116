package com.gmall.manage.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil {

    public static String upload(MultipartFile multipartFile) {

        String imgUrl = "http://192.168.133.148";

        String file = PmsUploadUtil.class.getResource("/tracker.conf").getPath();
        try {
            ClientGlobal.init(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StorageClient storageClient = new StorageClient(trackerServer, null);
        try {
            byte[] bytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            String fileExtName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String[] uploadInfos = storageClient.upload_file(bytes, fileExtName, null);
            for (String uploadInfo : uploadInfos) {
                imgUrl += "/"+uploadInfo;
                System.out.println("imgUrl"+ imgUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
}