package com.example.mnnu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.mnnu.form.ExamSaveForm;
import com.example.mnnu.pojo.Exam;
import com.example.mnnu.vo.ResponseVO;

public interface IExamService {

    Exam changeToExam(ExamSaveForm examSaveForm);

    ResponseVO add(Exam exam);

    ResponseVO upd(Exam exam);

    ResponseVO delete(Long examId);

    ResponseVO show(Integer pageNum, Integer pageSize);

    ResponseVO detail(Long examId);

    ResponseVO<IPage> enter(Long examId, String examPassword, Integer pageNum, Integer pageSize);

}
