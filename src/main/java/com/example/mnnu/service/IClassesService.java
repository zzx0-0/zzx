package com.example.mnnu.service;

import com.example.mnnu.vo.ResponseVO;
import com.github.pagehelper.PageInfo;

public interface IClassesService {

    int addStu(String classCode, String stuCode);

    int delStu(String classCode, String stuCode);

    ResponseVO setClassName(String classCode, String className);

    ResponseVO<PageInfo> showClass(String clas, Integer pageNum, Integer pageSize);

    ResponseVO getClassPension(String student, String classCode);

}
