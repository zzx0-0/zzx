package com.example.mnnu.controller;

import com.example.mnnu.service.IFileService;
import com.example.mnnu.util.Util;
import com.example.mnnu.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

@Slf4j
@RestController
@Api(value = "文件管理类")
public class FileController {

    @Autowired
    private IFileService fileService;

    @Autowired
    private IndexController indexController;

    @ApiOperation(value = "上传照片, f=true, 照片覆盖， 用于用户头像上传；" +
            "                       f=false, 照片不覆盖， 用于人脸收集")
    @PostMapping("/i/file/upload")
    public ResponseVO uploadPh(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "f", defaultValue = "true") boolean f,
                               @RequestParam(required = false) String userCode,
                               HttpSession session) {
        if (Util.getCurrentUserRole(session) == 0 || userCode == null)
            userCode = Util.getCurrentUserCode(session);
        return fileService.uploadPh(file, f, userCode);
    }

    @ApiOperation(value = "上传Excel")
    @PostMapping("/2/file/upload")
    public ResponseVO uploadExcel(@RequestParam("file") MultipartFile file,
                            HttpSession session) throws IOException {
        String userCode = Util.getCurrentUserCode(session);
        ResponseVO responseVo = fileService.uploadExcel(file, userCode);
        List list = fileService.readExcel((File) responseVo.getData());
        return indexController.registerList(list);
    }

    @ApiOperation(value = "下载模板Excel")
    @GetMapping("/2/file/download")
    public void download(HttpServletResponse response) throws IOException {
        fileService.download(response);
    }

}
