package com.example.mnnu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mnnu.dao.ProblemMapper;
import com.example.mnnu.enums.ResponseEnum;
import com.example.mnnu.pojo.Problem;
import com.example.mnnu.service.IProblemService;
import com.example.mnnu.utils.Util;
import com.example.mnnu.vo.ResponseVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class ProblemServiceImpl implements IProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    @Override
    @Cacheable(cacheNames = "pro", key = "#problemType")
    public List<Long> getProIdByType(Integer problemType){
        return problemMapper.getAllProIdByType(problemType);
    }

    @Override
    @CacheEvict(cacheNames = "pro", key = "#problem.problemType")
    public ResponseVO addPro(Problem problem) {
        return Util.ff(problemMapper.insert(problem));
    }

    @Override
    @CacheEvict(cacheNames = "pro", key = "#result.data")
    public ResponseVO delete(Long problemId) {
        Problem pro = problemMapper.selectById(problemId);
        Integer type = pro.getProblemType();
        problemMapper.deleteById(problemId);
        return ResponseVO.success(type);
    }

    @Override
    public ResponseVO updatePro(Problem problem) {
        // 更新不支持改 problemType
        int type = getProType(problem.getProblemId());
        if (problem.getProblemType() != type) {
            return ResponseVO.error(ResponseEnum.ERROR, "不支持直接改类型");
        }
        return Util.ff(problemMapper.updateById(problem));
    }

    @Override
    public ResponseVO<PageInfo> showPro(Integer problemType, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Problem> problemList ;
        if (problemType == -1){
            problemList = problemMapper.selectAll();
        } else {
            QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("problem_type", problemType);
            problemList = problemMapper.selectList(queryWrapper);
        }
        return ResponseVO.success(Util.pageInfo(problemList));
    }

    @Override
    public ResponseVO showOne(Long problemId) {
        Problem problem = problemMapper.selectById(problemId);
        if (problem == null) {
            throw new RuntimeException("试题ID有误");
        }
        return ResponseVO.success(problem);
    }

    @Override
    public Integer getProType(Long problemId) {
        Problem pro = problemMapper.selectById(problemId);
        return pro.getProblemType();
    }

}
