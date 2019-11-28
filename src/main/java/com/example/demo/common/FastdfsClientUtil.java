package com.example.demo.common;


import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Fastdfs工具类
 */
@Component
public class FastdfsClientUtil {

    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private ThumbImageConfig thumbImageConfig;


    //上传文件
    public String upload(MultipartFile myfile) throws Exception {
        //文件名
        String originalFilename = myfile.getOriginalFilename().substring(myfile.getOriginalFilename().lastIndexOf(".") + 1);
        // 文件扩展名
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
        StorePath storePath = this.storageClient.uploadFile(myfile.getInputStream(), myfile.getSize(), originalFilename, null);
        String path = storePath.getFullPath();
        return path;
    }

    /**
     * 删除文件
     *
     * @Param fileUrl 文件访问地址
     */
    public void deleteFile(String fileUrl) {
        StorePath storePath = StorePath.parseFromUrl(fileUrl);
        storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
    }
}