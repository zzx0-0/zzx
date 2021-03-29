package com.example.mnnu.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ToString
@TableName("problem")
public class Problem {

    @TableId(type = IdType.AUTO)
    private Long problemId;

    @NotBlank
    private String problemText;

    @NotBlank
    private String problemAnswer;

    // 试题类型：1-选择题；2-填空题；3-判断题；4-综合题；5-书写
    @Range(min = 1, max = 5)
    @NotNull
    private Integer problemType;

    private String problemImportMethod;

    @Range(min = 0, max = 1)
    private Byte problemIsExport;

    private Integer problemRankIntact;

    @JsonIgnore
    private LocalDateTime createTime;

    @JsonIgnore
    private LocalDateTime updateTime;

    public Problem() {}

}
