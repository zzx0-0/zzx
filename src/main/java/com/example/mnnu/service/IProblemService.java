package com.example.mnnu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.mnnu.pojo.Problem;
import com.example.mnnu.vo.ResponseVO;

import java.util.List;

public interface IProblemService {


    List<Long> getProIdByType(Integer problemType);

    ResponseVO addPro(Problem problem);

    ResponseVO updatePro(Problem problem);

    ResponseVO delete(Long problemId);

    ResponseVO<IPage> showPro(Integer problemType, Integer pageNum, Integer pageSize);

    ResponseVO showOne(Long problemId);

    Integer getProType(Long problemId);

}
