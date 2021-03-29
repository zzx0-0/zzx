package com.example.mnnu.service.impl;

import com.aliyun.oss.OSSClient;

import com.aliyun.oss.model.CannedAccessControlList;
import com.example.mnnu.config.Constant;
import com.example.mnnu.config.YmlConfig;
import com.example.mnnu.enums.ResponseEnum;
import com.example.mnnu.service.IFileService;
import com.example.mnnu.service.IUserService;
import com.example.mnnu.utils.TimeUtil;
import com.example.mnnu.vo.ResponseVO;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private YmlConfig ymlConfig;

    @Autowired
    private IUserService userService;

    @Override
    public ResponseVO uploadExcel(MultipartFile file, String uploadEr) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("您未选择任何上传文件");
        }
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String fileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (!fileNameSuffix.equalsIgnoreCase(".XLS") && !fileNameSuffix.equalsIgnoreCase(".XLSX")) {
            throw new RuntimeException("仅支持 .xls  .xlsx 的文件上传");
        }

        fileName = uploadEr + "-" + TimeUtil.getTime() + fileNameSuffix;
        String path = ymlConfig.getUpPath() + File.separator + fileName;
        log.info("上传到：{}", path);
        File dest = new File(path);
        file.transferTo(dest);
        return ResponseVO.success(dest);
    }

    @Override
    public ResponseVO uploadPh(MultipartFile file, boolean f, String userCode) {
        if (file.isEmpty()) {
            return ResponseVO.error(ResponseEnum.OTHERS_ERROR, "您未选择任何上传文件");
        }
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String fileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (!fileNameSuffix.equalsIgnoreCase(".JPG") && !fileNameSuffix.equalsIgnoreCase(".JPEG")
                && !fileNameSuffix.equalsIgnoreCase(".PNG")) {
            return ResponseVO.error(ResponseEnum.OTHERS_ERROR,"仅支持 .JPG  .JPEG .PNG 的文件上传");
        }

        String uploadUrl;
        try {
            OSSClient ossClient = new OSSClient(ymlConfig.getOSS_EndPoint(), ymlConfig.getOSS_AccessKeyId(), ymlConfig.getOSS_AccessKeySecret());
            ossClient.createBucket(ymlConfig.getOSS_BucketName());   //创建bucket
            ossClient.setBucketAcl(ymlConfig.getOSS_BucketName(), CannedAccessControlList.PublicRead);   //设置oss实例的访问权限：公共读

            InputStream inputStream = file.getInputStream();    //获取上传文件流
            String fileUrl;

            if (f) {
                fileUrl = Constant.AVATAR + "/" + userCode + "/" + TimeUtil.getTime() + fileNameSuffix;
            } else {
                fileUrl = Constant.FACE + "/" + userCode + "/" + TimeUtil.getTime() + fileNameSuffix;
            }

            ossClient.putObject(ymlConfig.getOSS_BucketName(), fileUrl, inputStream);     //文件上传至阿里云
            ossClient.shutdown();    // 关闭OSSClient
            uploadUrl = "http://" + ymlConfig.getOSS_BucketName() + "." + ymlConfig.getOSS_EndPoint() + "/" + fileUrl;   //获取url地址
        } catch (IOException e) {
            throw new RuntimeException("阿里云镜像服务器错误");
        }

//        if (f) {
//            return userService.setAvatar(userCode, uploadUrl);
//        }
        return ResponseVO.success(uploadUrl);
    }

    @Override
    public void download(HttpServletResponse response) throws IOException {
        final String fileName = "model.xls";
        String path = ymlConfig.getDownPath() + File.separator + fileName;
        log.info("下载：{}", path);
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在了");
        }
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        OutputStream os = response.getOutputStream();
        int i = bis.read(buffer);
        while (i != -1) {
            os.write(buffer, 0, i);
            i = bis.read(buffer);
        }
    }

    @Override
    public List readExcel(File file) throws IOException {
        List<List<String>> outerList = new ArrayList<>();
        try {
            InputStream is = new FileInputStream(file.getAbsolutePath());
            Workbook wb = Workbook.getWorkbook(is);

            Sheet sheet = wb.getSheet(0);
            // sheet.getRows()返回该页的总行数
            for (int i = 7; i < sheet.getRows(); i++) {
                List<String> innerList = new ArrayList<>();
                // sheet.getColumns()返回该页的总列数
                for (int j = 0; j < 5; j++) {
                    String cellinfo = sheet.getCell(j, i).getContents().trim();
                    innerList.add(cellinfo);
                }
                outerList.add(innerList);
            }
            log.info("文件内容：{}", outerList);
        } catch (FileNotFoundException | BiffException e) {
            e.printStackTrace();
            throw new RuntimeException("读取有问题...");
        }
        return outerList;
    }

}
