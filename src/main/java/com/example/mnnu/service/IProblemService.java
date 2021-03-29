package com.example.mnnu.service;

import com.example.mnnu.pojo.Problem;
import com.example.mnnu.vo.ResponseVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IProblemService {


    List<Long> getProIdByType(Integer problemType);

    ResponseVO addPro(Problem problem);

    ResponseVO updatePro(Problem problem);

    ResponseVO delete(Long problemId);

    ResponseVO<PageInfo> showPro(Integer problemType, Integer pageNum, Integer pageSize);

    ResponseVO showOne(Long problemId);

    Integer getProType(Long problemId);

}
