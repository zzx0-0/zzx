package com.example.mnnu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.mnnu.vo.ResponseVO;

public interface IClassesService {

    int addStu(String classCode, String stuCode);

    int delStu(String classCode, String stuCode);

    ResponseVO setClassName(String classCode, String className);

    ResponseVO<IPage> showClass(String clas, Integer pageNum, Integer pageSize);

    ResponseVO getClassPension(String student, String classCode);

}
