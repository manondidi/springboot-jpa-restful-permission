package com.example.demo.controller;

import com.example.demo.common.FastdfsClientUtil;
import com.example.demo.config.property.FileServer;
import com.example.demo.expection.BusinessException;
import com.example.demo.expection.BusinessExceptionEnum;
import com.example.demo.pojo.vo.FilePath;
import com.example.demo.pojo.vo.Result;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@Getter
@Setter
public class FileController {
    @Autowired
    private FileServer fileServer;

    @PostMapping("/files")
    public Result<FilePath> upload(@RequestParam("file") MultipartFile file) throws BusinessException {
        if (file.isEmpty()) {
            throw new BusinessException(BusinessExceptionEnum.UPLOAD_FAIL_NOT_FILE);
        }
        try {
            FilePath filePath = FilePath.generate(fileServer.getPath(), fastdfsClientUtil.upload(file));
            return Result.success(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionEnum.UPLOAD_FAIL, e.getMessage());
        }
    }

    @Autowired
    private FastdfsClientUtil fastdfsClientUtil;


}
