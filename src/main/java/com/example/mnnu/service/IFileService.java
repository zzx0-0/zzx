package com.example.mnnu.service;

import com.example.mnnu.vo.ResponseVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IFileService {

    ResponseVO uploadExcel(MultipartFile file, String uploadEr) throws IOException;

    ResponseVO uploadPh(MultipartFile file, boolean f, String userCode);

    void download(HttpServletResponse response) throws IOException;

    List readExcel(File file) throws IOException;
}
